package pl.kantraksel.cziken;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = CzikenCore.MODID)
public class CzikenConfig {
	@RangeInt(min = 20)
	@RequiresMcRestart
	public static int AuthenticationTime = 1200;
	
	@RequiresMcRestart
	public static boolean EnableAutoban = true;
	
	@RangeInt(min = 1)
	@RequiresMcRestart
	public static int AuthenticationTries = 3;
	
	@RequiresMcRestart
	public static boolean CountNoMessage = true;
	
	@RequiresMcRestart
	public static boolean RemovePlayerOnBan = true;
	
	@RequiresMcRestart
	public static boolean EnableDirectoryStream = false;
}
