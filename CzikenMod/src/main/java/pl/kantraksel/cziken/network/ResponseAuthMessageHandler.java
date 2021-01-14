package pl.kantraksel.cziken.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.network.messages.ResponseAuthMessage;

public class ResponseAuthMessageHandler implements IMessageHandler<ResponseAuthMessage, IMessage> {
	@Override
	public IMessage onMessage(ResponseAuthMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		player.getServerWorld().addScheduledTask(() -> {
			CzikenCore.INSTANCE.AuthSystem.Server.authenticate(player, message.token);
		});
		return null;
	}
}
