package pl.kantraksel.cziken.authentication;

import java.security.SecureRandom;
import javax.crypto.KeyGenerator;

public class Token {
	//it's static final, because DES is an 8-byte key
	public static final int LENGTH = 8;
	
	private byte[] key;
	
	public Token(byte[] key) {
		this.key = key;
	}
	
	public static Token generate() {
		Token token = null;
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom());
			token = new Token(generator.generateKey().getEncoded());
		} catch (Exception e) {}
		return token;
	}
	
	public byte[] toBytes() {
		return key;
	}
	
	public int length() {
		int length = 0;
		if (key != null) length = key.length;
		return length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		else {
			if (obj instanceof Token) {
				Token token = (Token)obj;
				if (key == token.key) return true;
				else if (key != null && token.key != null) {
					if (key.length == token.key.length) {
						for (int i = 0; i < key.length; ++i) {
							if (key[i] != token.key[i]) return false;
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
