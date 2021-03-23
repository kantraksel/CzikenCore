package pl.kantraksel.cziken.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import pl.kantraksel.cziken.CzikenConfig;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.Utilities;
import pl.kantraksel.cziken.authentication.Token;

public class AuthenticationStorage {
	private HashMap<String, Token> tokens;
	private File configDir;
	private boolean hasChanged = false;
	
	private AuthenticationStorage(String configDir) {
		tokens = new HashMap<>();
		this.configDir = new File(configDir + File.separatorChar + CzikenCore.MODID + "_serverauthdata");
	}
	
	public static AuthenticationStorage initialize(String configDir) {
		AuthenticationStorage instance = new AuthenticationStorage(configDir);
		if (!instance.load()) instance = null;
		return instance;
	}
	
	private boolean load() {
		return CzikenConfig.EnableDirectoryStream ? load2() : load1();
	}
	
	private boolean load1() {
		CzikenCore.logger.info("Loading AuthenticationStorage...");
		File[] files = configDir.listFiles();
		if (files != null) {
			for(File file : files) {
				loadFile(file);
			}
			CzikenCore.logger.info("AuthenticationStorage has been loaded");
		}
		else CzikenCore.logger.warn("AuthenticationStorage directory does not exist. Ommitting...");
		
		hasChanged = false;
		return true;
	}
	
	private boolean load2() {
		CzikenCore.logger.info("Loading AuthenticationStorage...");
		try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(configDir.toPath());
			for (Path entry : dirStream) {
				loadFile (entry.toFile());
			}
			CzikenCore.logger.info("AuthenticationStorage has been loaded");
		} catch (IOException e1) {
			CzikenCore.logger.warn("AuthenticationStorage directory does not exist. Ommitting...");
		}
		
		hasChanged = false;
		return true;
	}
	
	private void loadFile(File file) {
		FileInputStream stream = null;
		try {
			byte[] buffer = new byte[(int)file.length()];
			stream = new FileInputStream(file);
			stream.read(buffer);
			Utilities.closeStream(stream);
			stream = null;
			
			Token token = new Token(buffer);
			if (!checkTokenLength(token)) CzikenCore.logger.warn(file.getName() + " has invalid size");
			else {
				tokens.put(file.getName(), token);
				CzikenCore.logger.info("Read " + file.getName());
			}
		} catch (Exception e) {
			Utilities.closeStream(stream);
			CzikenCore.logger.warn("Could not read " + file.getName());
		}
	}
	
	public boolean isValidUser(String name, Token token) {
		boolean returnValue = false;
		Token currToken = tokens.get(name);
		if (currToken != null) returnValue = currToken.equals(token);
		return returnValue;
	}
	
	public boolean hasUser(String name) {
		return tokens.get(name) != null;
	}
	
	public boolean reload() {
		boolean returnValue = true;
		HashMap<String, Token> tokens = this.tokens;
		this.tokens = new HashMap<>();
		if (!load()) {
			returnValue = false;
			this.tokens = tokens;
		}
		return returnValue;
	}
	
	boolean checkTokenLength(Token token) {
		return token.length() == Token.LENGTH;
	}
	
	public boolean addUser(String name, Token token) {
		boolean returnValue = checkTokenLength(token) && !tokens.containsKey(name);
		if (returnValue) {
			tokens.put(name, token);
			hasChanged = true;
		}
		return returnValue;
	}
	
	public boolean removeUser(String name) {
		boolean returnValue = tokens.remove(name) != null;
		if (returnValue) hasChanged = true;
		return returnValue;
	}
	
	public boolean changesPending() {
		return hasChanged;
	}
	
	public boolean save() {
		CzikenCore.logger.info("Saving AuthenticationStorage...");
		try {
			if (configDir.exists()) FileUtils.deleteDirectory(configDir);
		} catch (IOException e1) {
			CzikenCore.logger.error("Could not remove AuthenticationStorage directory. Save has been canceled");
			hasChanged = false;
			return false;
		}
		configDir.mkdir();
		for (Entry<String, Token> entry : tokens.entrySet()) {
			
			File file = new File(configDir.getPath() + File.separatorChar + entry.getKey());
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				stream.write(entry.getValue().toBytes());
				stream.flush();
				Utilities.closeStream(stream);
				CzikenCore.logger.info("Saved " + entry.getKey());
			} catch (Exception e) {
				Utilities.closeStream(stream);
				CzikenCore.logger.error("Could not save " + file.getName());
			}
		}
		CzikenCore.logger.info("AuthenticationStorage has been saved");
		
		hasChanged = false;
		return true;
	}
}
