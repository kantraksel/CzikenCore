package pl.kantraksel.cziken.server;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pl.kantraksel.cziken.CzikenCore;

public class AppointedPlayersStorage {
	private List<String> players;
	private File configFile;
	private boolean hasChanged = false;
	
	private AppointedPlayersStorage(String configDir) {
		players = new ArrayList<>();
		configFile = new File(configDir + File.separatorChar + CzikenCore.MODID + "_appointmentlist");
	}
	
	public static AppointedPlayersStorage initialize(String configDir) {
		AppointedPlayersStorage instance = new AppointedPlayersStorage(configDir);
		if (!instance.load()) instance = null;
		return instance;
	}
	
	private boolean load() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(configFile);
			while (scanner.hasNextLine()) {
				players.add(scanner.nextLine());
			}
			hasChanged = false;
			scanner.close();
		} catch (Exception e) {
			if (scanner != null) scanner.close();
			CzikenCore.logger.error("Failed to read AppointedPlayersStorage");
			return false;
		}
		return true;
	}
	
	public boolean save() {
		configFile.delete();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(configFile);
			for (String player : players) {
				writer.println(player);
			}
			writer.flush();
			writer.close();
			hasChanged = false;
		} catch (Exception e) {
			if (writer != null) writer.close();
			CzikenCore.logger.error("Failed to save AppointedPlayersStorage");
			return false;
		}
		return true;
	}
	
	public boolean addPlayer(String name) {
		boolean returnValue = !players.contains(name);
		if (returnValue) {
			players.add(name);
			hasChanged = true;
		}
		return returnValue;
	}
	
	public boolean removePlayer(String name) {
		boolean returnValue = players.contains(name);
		if (returnValue) {
			players.remove(name);
			hasChanged = true;
		}
		return returnValue;
	}
	
	public boolean isPlayerOnList(String name) {
		return players.contains(name);
	}
	
	public boolean changesPending() {
		return hasChanged;
	}
}
