package fle.core.common;

import farcore.FarCore;
import fle.core.handler.FleEntityHandler;
import fle.core.items.ItemToolFar;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void init(FMLPreInitializationEvent event)
	{
		new ItemToolFar().setCreativeTab(FarCore.tabTool);
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
	}
}