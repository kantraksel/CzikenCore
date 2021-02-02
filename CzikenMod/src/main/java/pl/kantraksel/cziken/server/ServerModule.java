package pl.kantraksel.cziken.server;

import java.util.Date;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
	private final Lobby lobby = new Lobby();
	
	private AuthenticationStorage authStorage = null;
	private NewPlayerStorage newPlayerStorage = null;
	private final IdentityStealStorage identityStealStorage = new IdentityStealStorage();
	
	@Override
	public boolean initialize(AuthenticationSystem parent) {
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
		newPlayerStorage = null;
		authStorage = null;
		lobby.clear();
		isActive = false;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isPlayerAuthenticated(String name) {
		return !lobby.isWaiting(name);
	}
	
	public void authenticate(EntityPlayerMP player, Token token) {
		if (isActive) {
			if (token == null) authFailed(player);
			else if (newPlayerStorage.isPlayerOnList(player.getName())) {
				authStorage.addUser(player.getName(), token);
				newPlayerStorage.removePlayer(player.getName());
				lobby.remove(player.getName());
			}
			else if (authStorage.isValidUser(player.getName(), token)){
				lobby.remove(player.getName());
			}
			else authFailed(player);
		}
	}
	
	void authFailed(EntityPlayerMP player) {
		disconnect(player, "text.czikencore.authfailed");
		CzikenCore.logger.warn("User '" + player.getName() + "':" + player.getPlayerIP() + 
				" tried to log in");
	}
	
	void disconnect(EntityPlayerMP player, String reason) {
		if (identityStealStorage.checkLimit(player.getPlayerIP()))
			ban(player.getPlayerIP());
		player.connection.disconnect(new TextComponentTranslation(reason));
	}
	
	void ban(String address) {
		MinecraftServer server = CzikenCore.getServerInstance();
		UserListIPBansEntry userlistipbansentry = new UserListIPBansEntry(address, (Date)null, "CzikenCore", (Date)null, "Identity is not valid. Limit exceeded");
		server.getPlayerList().getBannedIPs().addEntry(userlistipbansentry);
		CzikenCore.logger.warn(address + " has exceeded authentication limit!");
	}
	
	public boolean reloadStorage() {
		return isActive && authStorage.reload() && newPlayerStorage.reload();
	}
	
	//Events
	public void onPlayerConnected(EntityPlayerMP player) {
		if (isActive) {
			lobby.add(player.getName(), new UserSession(player.getPositionVector()));
			CzikenCore.INSTANCE.NetworkChannel.sendTo(new RequestAuthMessage(), player);
		}
	}
	
	public void onPlayerDisconnected(EntityPlayerMP player) {
		if (isActive) lobby.remove(player.getName());
	}
	
	public void onTick() {
		if (isActive) {
			for (Entry<String, UserSession> entry : lobby.getSet()) {
				UserSession data = entry.getValue();
				EntityPlayerMP player = CzikenCore.getServerInstance().getPlayerList().getPlayerByUsername(entry.getKey());
				if (player != null) {
					if (data.ticksWithoutAuth == CzikenConfig.AuthenticationTime) 
						disconnect(player, "text.czikencore.timeout");
					else {
						++data.ticksWithoutAuth;
						if (!data.initPosition.equals(player.getPositionVector())) 
							player.setPosition(data.initPosition.x, data.initPosition.y, data.initPosition.z);
					}
				}
			}
			
			if (newPlayerStorage.changesPending()) newPlayerStorage.save();
			if (authStorage.changesPending()) authStorage.save();
		}
	}
	
	public NewPlayerStorage getNewPlayerStorage() {
		return newPlayerStorage;
	}
}
