package pl.kantraksel.cziken.server;

import java.util.HashMap;

import pl.kantraksel.cziken.CzikenConfig;

public class IdentityStealStorage {
	private HashMap<String, Integer> users;
	
	public IdentityStealStorage() {
		users = new HashMap<>();
	}
	
	//unused
	public boolean hasLimitExceeded(String address) {
		boolean returnValue = false;
		if (CzikenConfig.EnableAutoban) {
			Integer limit = users.get(address);
			if (limit != null) returnValue = limit >= CzikenConfig.AuthenticationTries;
		}
		return returnValue;
	}
	
	public boolean checkLimit(String address) {
		Integer limit = 0;
		if (CzikenConfig.EnableAutoban) {
			limit = users.get(address);
			if (limit == null) limit = 0;
			++limit;
			users.put(address, limit);
		}
		return limit >= CzikenConfig.AuthenticationTries;
	}
	
	public void removeInfractions(String address) {
		users.remove(address);
	}
}
