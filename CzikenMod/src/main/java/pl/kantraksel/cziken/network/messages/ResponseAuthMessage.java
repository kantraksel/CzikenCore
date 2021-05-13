package pl.kantraksel.cziken.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.authentication.Token;

public class ResponseAuthMessage implements IMessage {
	public Token token;
	
	public ResponseAuthMessage() {
		token = null;
	}
	
	public ResponseAuthMessage(Token token) {
		this.token = token;
	}
	
	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		byte[] buff = new byte[Token.LENGTH];
		try {
			buffer.readBytes(buff);
		} catch (Exception e) {
			CzikenCore.logger.warn("Could not receive client auth token!");
			return;
		}
		
		token = new Token(buff);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		if (token != null) buffer.writeBytes(token.toBytes());
		else { CzikenCore.logger.warn("No token was given!"); }
	}
}
