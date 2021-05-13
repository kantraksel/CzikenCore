package pl.kantraksel.cziken.server;

import java.util.Date;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListIPBans;
import net.minecraft.server.management.UserListIPBansEntry;
import net.minecraft.util.text.TextComponentTranslation;
import pl.kantraksel.cziken.CzikenConfig;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.authentication.AuthenticationSystem;
import pl.kantraksel.cziken.authentication.IModule;
import pl.kantraksel.cziken.authentication.Token;
import pl.kantraksel.cziken.network.messages.RequestAuthMessage;

public class ServerModule implements IModule {
	private boolean isActive = false;
	private boolean checkBanSheulded = false;
	private final Lobby lobby = new Lobby();
	
	private AuthenticationStorage authStorage = null;
	private NewPlayerStorage newPlayerStorage = null;
	private final IdentityStealStorage identityStealStorage = new IdentityStealStorage();
	
	private UserListIPBans ipBans = null;
	private UserListBans playerBans = null;
	
	@Override
	public boolean initialize(AuthenticationSystem parent) {
		PlayerList list = CzikenCore.getServerInstance().getPlayerList();
		ipBans = list.getBannedIPs();
		playerBans = list.getBannedPlayers();
		authStorage = AuthenticationStorage.initialize(parent.getConfigurationPath());
		if (authStorage != null) {
			newPlayerStorage = NewPlayerStorage.initialize(parent.getConfigurationPath(), authStorage);
			if (newPlayerStorage != null) isActive = true;
			else authStorage = null;
		}
		return isActive;
	}

	@Override
	public void shutdown() {
		if (CzikenConfig.SaveOnStop) {
			newPlayerStorage.save();
			authStorage.save();
		}
		
		newPlayerStorage = null;
		authStorage = null;
		lobby.clear();
		isActive = false;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isPlayerAuthenticated(EntityPlayerMP player) {
		return !lobby.isWaiting(player);
	}
	
	public void authenticate(EntityPlayerMP player, Token token) {
		if (isActive) {
			if (token == null) authFailed(player);
			else if (newPlayerStorage.isPlayerOnList(player.getName())) {
				authStorage.addUser(player.getName(), token);
				newPlayerStorage.removePlayer(player.getName());
				lobby.remove(player);
			}
			else if (authStorage.isValidUser(player.getName(), token)) {
				lobby.remove(player);
			}
			else authFailed(player);
		}
	}
	
	void authFailed(EntityPlayerMP player) {
		disconnect(player, "text.czikencore.authfailed", true);
		CzikenCore.logger.warn("User '" + player.getName() + "':" + player.getPlayerIP() + 
				" tried to log in");
	}
	
	void disconnect(EntityPlayerMP player, String reason, boolean canBan) {
		if (canBan && identityStealStorage.checkLimit(player.getPlayerIP())) {
			ban(player.getPlayerIP());
			identityStealStorage.removeInfractions(player.getPlayerIP());
		}
		player.connection.disconnect(new TextComponentTranslation(reason));
	}
	
	void ban(String address) {
		UserListIPBansEntry userlistipbansentry = new UserListIPBansEntry(address, (Date)null, "CzikenCore", (Date)null, "Identity is not valid. Limit exceeded");
		ipBans.addEntry(userlistipbansentry);
		CzikenCore.logger.warn(address + " has exceeded authentication limit!");
	}
	
	public boolean reloadStorage() {
		return isActive && authStorage.reload() && newPlayerStorage.reload();
	}
	
	public void sheuldeCheckBans() {
		checkBanSheulded = true;
	}
	
	void checkBans() {
		checkBanSheulded = false;
		if (CzikenConfig.RemovePlayerOnBan) {
			String[] players = playerBans.getKeys();
			for (String name : players) {
				authStorage.removeUser(name);
				newPlayerStorage.removePlayer(name);
			}
		}
	}
	
	//Events
	public void onPlayerConnected(EntityPlayerMP player) {
		if (isActive) {
			lobby.add(player, new UserSession(player.getPositionVector()));
			CzikenCore.INSTANCE.NetworkChannel.sendTo(new RequestAuthMessage(), player);
		}
	}
	
	public void onPlayerDisconnected(EntityPlayerMP player) {
		if (isActive) {
			lobby.remove(player);
		}
	}
	
	public void onTick() {
		if (isActive) {
			for (Entry<EntityPlayerMP, UserSession> entry : lobby.getSet()) {
				UserSession data = entry.getValue();
				EntityPlayerMP player = entry.getKey();
				if (player != null) {
					if (data.ticksWithoutAuth == CzikenConfig.AuthenticationTime) 
						disconnect(player, "text.czikencore.timeout", CzikenConfig.CountNoMessage);
					else {
						++data.ticksWithoutAuth;
						if (!data.initPosition.equals(player.getPositionVector())) 
							player.setPosition(data.initPosition.x, data.initPosition.y, data.initPosition.z);
					}
				}
			}
			if (checkBanSheulded) checkBans();
			if (newPlayerStorage.changesPending()) newPlayerStorage.save();
			if (authStorage.changesPending()) authStorage.save();
		}
	}
	
	public NewPlayerStorage getNewPlayerStorage() {
		return newPlayerStorage;
	}
}
