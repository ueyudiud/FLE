package fle.core.common;

import fle.core.handler.FleEntityHandler;
import fle.loader.BlocksItems;
import fle.loader.Tools;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void init(FMLPreInitializationEvent event)
	{
		BlocksItems.registerItemsAndBlocks();
		Tools.initalizeTools();
		BlocksItems.setBlocksItemsProperties();
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
		fargen.compact.fle.SubCompact.init();
	}
}