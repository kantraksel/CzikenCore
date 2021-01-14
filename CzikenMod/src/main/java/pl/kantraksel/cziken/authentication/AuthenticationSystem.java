package pl.kantraksel.cziken.authentication;

import java.io.File;
import java.io.IOException;

import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.client.TokenStorage;
import pl.kantraksel.cziken.server.ServerModule;

public class AuthenticationSystem {
	public final ServerModule Server = new ServerModule();
	public final TokenStorage Client = new TokenStorage();
	
	private File configPath = null;
	
	public void initialize(String configDir)  {
		configPath = new File(configDir + File.separatorChar + ".." + File.separatorChar + "Cziken");
		configPath.mkdir();
		try {
			configPath = configPath.getCanonicalFile();
		} catch (IOException e) {
			CzikenCore.logger.error("Could not get config directory");
		}
	}
	
	public boolean initializeServer() {
		return Server.initialize(this);
	}
	
	public boolean initalizeClient() {
		return Client.initialize(this);
	}
	
	public void shutdown() {
		Server.shutdown();
		Client.shutdown();
		configPath = null;
	}
	
	public String getConfigurationPath() {
		String path = null;
		if (configPath != null) path = configPath.getPath();
		return path;
	}
}
