package fle.load;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.util.FleLog;
import fle.core.handler.PlayerHandler;
import fle.core.handler.WaterBehaviorHandler;
import net.minecraftforge.common.MinecraftForge;

public class Handlers
{
	public static void init()
	{
		FleLog.getLogger().info("Start register handlers.");
		MinecraftForge.EVENT_BUS.register(new WaterBehaviorHandler());
		FMLCommonHandler.instance().bus().register(new PlayerHandler());
		FarCoreKeyHandler.register("crafting", Keyboard.KEY_C);
	}
}