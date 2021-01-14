package pl.kantraksel.cziken;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@Config(modid = CzikenCore.MODID)
public class CzikenConfig {
	@RangeInt(min = 20)
	@RequiresMcRestart
	public static int AuthenticationTime = 1200;
}
