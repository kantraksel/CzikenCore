package pl.kantraksel.cziken.server;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map.Entry;

public class Lobby {
	private final HashMap<EntityPlayerMP, UserSession> users = new HashMap<>();
	
	public void add(EntityPlayerMP player, UserSession data) {
		users.put(player, data);
	}
	
	public void remove(EntityPlayerMP name) {
		users.remove(name);
	}
	
	public UserSession getData(EntityPlayerMP name) {
		return users.get(name);
	}
	
	public boolean isWaiting(EntityPlayerMP name) {
		return users.containsKey(name);
	}
	
	public Set<Entry<EntityPlayerMP, UserSession>> getSet() {
		return users.entrySet();
	}
	
	public void clear() {
		users.clear();
	}
}
