package pl.kantraksel.cziken.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.authentication.Token;
import pl.kantraksel.cziken.network.messages.RequestAuthMessage;
import pl.kantraksel.cziken.network.messages.ResponseAuthMessage;

public class RequestAuthMessageHandler implements IMessageHandler<RequestAuthMessage, IMessage> {
	@Override
	public IMessage onMessage(RequestAuthMessage message, MessageContext ctx) {
		ResponseAuthMessage returnMessage = new ResponseAuthMessage();
		Token token = CzikenCore.INSTANCE.AuthSystem.Client.load();
		if (token != null) returnMessage.setToken(token);
		else CzikenCore.logger.warn("Could not read token!");
		return returnMessage;
	}
}
