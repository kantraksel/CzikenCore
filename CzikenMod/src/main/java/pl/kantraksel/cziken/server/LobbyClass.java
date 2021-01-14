package pl.kantraksel.cziken.server;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class LobbyClass {
	private final HashMap<String, UserSession> users = new HashMap<>();
	
	public void add(String name, UserSession data) {
		users.put(name, data);
	}
	
	public void remove(String name) {
		users.remove(name);
	}
	
	public UserSession getData(String name) {
		return users.get(name);
	}
	
	public boolean isWaiting(String name) {
		return users.containsKey(name);
	}
	
	public Set<Entry<String, UserSession>> getSet() {
		return users.entrySet();
	}
	
	public void clear() {
		users.clear();
	}
}
