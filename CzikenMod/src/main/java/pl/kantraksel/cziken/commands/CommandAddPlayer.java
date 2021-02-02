package pl.kantraksel.cziken.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import pl.kantraksel.cziken.CzikenCore;
import pl.kantraksel.cziken.server.NewPlayerStorage;

public class CommandAddPlayer extends CommandBase {
	@Override
	public String getName() {
		return "addCziken";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/addCziken <name>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		String response = "text.czikencore.addplayeronlymp";
		if (args.length < 1) response = "text.czikencore.notenoughargs";
		else if (CzikenCore.getPhysicalSide() == Side.SERVER)  {
			NewPlayerStorage storage = CzikenCore.INSTANCE.AuthSystem.Server.getNewPlayerStorage();
			if (storage.addPlayer(args[0])) response = "text.czikencore.addplayersuccess";
			else response = "text.czikencore.addplayerexists";
		}
		sender.sendMessage(new TextComponentTranslation(response));
	}
	
	@Override
	public int getRequiredPermissionLevel() {
        return 3;
    }
}
