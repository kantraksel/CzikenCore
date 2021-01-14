package pl.kantraksel.cziken.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.Utilities;
import pl.kantraksel.cziken.authentication.AuthenticationSystem;
import pl.kantraksel.cziken.authentication.IModule;
import pl.kantraksel.cziken.authentication.Token;

public class TokenStorage implements IModule {
	private File configPath = null;
	private boolean isActive = false;

	@Override
	public boolean initialize(AuthenticationSystem parent) {
		configPath = new File(parent.getConfigurationPath() + File.separatorChar + CzikenCore.MODID + "_clienttoken");
		isActive = true;
		generateIfNotExist();
		return true;
	}

	@Override
	public void shutdown() {
		configPath = null;
		isActive = false;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}
	
	public Token load() {
		Token token = null;
		if (isActive) {
			FileInputStream stream = null;
			try {
				byte[] buffer = new byte[(int)configPath.length()];
				stream = new FileInputStream(configPath);
				stream.read(buffer, 0, buffer.length);
				Utilities.closeStream(stream);
				token = new Token(buffer);
			} catch(Exception e) {
				Utilities.closeStream(stream);
				CzikenCore.logger.warn("Could not load token. Maybe it does not exist!");
			}
		}
		return token;
	}
	
	public boolean save(Token token) {
		if (isActive) {
			configPath.getParentFile().mkdir();
			configPath.delete();
			
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(configPath);
				stream.write(token.toBytes());
				stream.flush();
				Utilities.closeStream(stream);
				return true;
			} catch (Exception e) {
				Utilities.closeStream(stream);
				CzikenCore.logger.warn("Could not save token!");
			}
		}
		return false;
	}
	
	private void generateIfNotExist() {
		if (!configPath.exists()) {
			Token token = Token.generate();
			if (token == null) CzikenCore.logger.error("Could not generate token");
			else save(token);
		}
	}
}
