package pl.kantraksel.cziken.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import pl.kantraksel.cziken.CzikenCore;

public class CommandReloadAuth extends CommandBase {
	@Override
	public String getName() {
		return "reloadCzikens";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/reloadCzikens";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		String responseStr = "text.czikencore.reloadonlymp";
		if (CzikenCore.getPhysicalSide() == Side.SERVER) 
			if (CzikenCore.INSTANCE.AuthSystem.Server.reloadStorage()) responseStr = "text.czikencore.reloadsuccess";
			else responseStr = "text.czikencore.reloaderror";
		
		sender.sendMessage(new TextComponentTranslation(responseStr));
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 3;
    }
}
