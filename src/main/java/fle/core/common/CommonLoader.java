package fle.core.common;

import fle.core.handler.FleEntityHandler;
import fle.core.tile.ditchs.DefaultDitchFactory;
import fle.loader.BlocksItems;
import fle.loader.Entities;
import fle.loader.Materials;
import fle.loader.Tools;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonLoader
{
	public void init(FMLPreInitializationEvent event)
	{
		new DefaultDitchFactory();
		Materials.preinit();
		BlocksItems.registerItemsAndBlocks();
		Tools.initalizeTools();
		BlocksItems.setBlocksItemsProperties();
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
		Entities.commonInit();
	}
}