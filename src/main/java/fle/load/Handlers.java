package fle.load;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import farcore.FarCoreRegistry;
import farcore.handler.FarCoreKeyHandler;
import farcore.util.FleLog;
import fle.core.handler.EntityHandler;
import fle.core.handler.PlayerHandler;
import fle.core.handler.WaterBehaviorHandler;
import fle.core.handler.WorldHandler;
import net.minecraftforge.common.MinecraftForge;

public class Handlers
{
	public static void init()
	{
		FleLog.getLogger().info("Start register handlers.");
		MinecraftForge.EVENT_BUS.register(new WaterBehaviorHandler());
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
		FMLCommonHandler.instance().bus().register(new PlayerHandler());
		FarCoreRegistry.registerMFEventHandler(new EntityHandler());
		FarCoreKeyHandler.register("crafting", Keyboard.KEY_C);
	}
}