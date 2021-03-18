package pl.kantraksel.cziken.server;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pl.kantraksel.cziken.CzikenCore;

public class NewPlayerStorage {
	private List<String> players;
	private File configFile;
	private AuthenticationStorage authStorage;
	private boolean hasChanged = false;
	
	private NewPlayerStorage(String configDir, AuthenticationStorage authStorage) {
		players = new ArrayList<>();
		configFile = new File(configDir + File.separatorChar + CzikenCore.MODID + "_appointmentlist");
		this.authStorage = authStorage;
	}
	
	public static NewPlayerStorage initialize(String configDir, AuthenticationStorage authStorage) {
		NewPlayerStorage instance = new NewPlayerStorage(configDir, authStorage);
		if (!instance.load()) instance = null;
		return instance;
	}
	
	private boolean load() {
		CzikenCore.logger.info("Loading NewPlayerStorage...");
		Scanner scanner = null;
		try {
			if (!configFile.exists()) configFile.createNewFile();
			scanner = new Scanner(configFile);
			while (scanner.hasNextLine()) {
				players.add(scanner.nextLine());
			}
			hasChanged = false;
			scanner.close();
			CzikenCore.logger.info("NewPlayerStorage has been loaded");
		} catch (Exception e) {
			if (scanner != null) scanner.close();
			CzikenCore.logger.error("Failed to load NewPlayerStorage");
			return false;
		}
		validate();
		return true;
	}
	
	public boolean save() {
		CzikenCore.logger.info("Saving NewPlayerStorage...");
		configFile.delete();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(configFile);
			for (String player : players) {
				writer.println(player);
			}
			writer.flush();
			writer.close();
			CzikenCore.logger.info("NewPlayerStorage has been saved");
			hasChanged = false;
		} catch (Exception e) {
			if (writer != null) writer.close();
			CzikenCore.logger.error("Failed to save NewPlayerStorage");
			return false;
		}
		return true;
	}
	
	public boolean reload() {
		boolean returnValue = true;
		List<String> players = this.players;
		this.players = new ArrayList<>();
		if (!load()) {
			returnValue = false;
			this.players = players;
		}
		return returnValue;
	}
	
	public boolean addPlayer(String name) {
		boolean returnValue = !players.contains(name) && !authStorage.hasUser(name);
		if (returnValue) {
			players.add(name);
			hasChanged = true;
		}
		return returnValue;
	}
	
	public boolean removePlayer(String name) {
		boolean returnValue = players.remove(name);
		if (returnValue) hasChanged = true;
		return returnValue;
	}
	
	public boolean isPlayerOnList(String name) {
		return players.contains(name);
	}
	
	public boolean changesPending() {
		return hasChanged;
	}
	
	void validate() {
		for (int i = 0; i < players.size(); ++i) {
			String player = players.get(i);
			if (authStorage.hasUser(player)) {
				CzikenCore.logger.warn("Removed user '" + player + "' from NewPlayerList. The player is already known!");
				players.remove(i);
				--i;
			}
		}
		hasChanged = true;
	}
}
