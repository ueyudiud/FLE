package fle.load;

import farcore.util.FleLog;
import fle.core.handler.WaterBehaviorHandler;
import net.minecraftforge.common.MinecraftForge;

public class Handlers
{
	public static void init()
	{
		FleLog.getLogger().info("Start register handlers.");
		MinecraftForge.EVENT_BUS.register(new WaterBehaviorHandler());
	}
}