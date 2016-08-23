package fle.core.common;

import fle.core.handler.FleEntityHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void init(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
	}
}