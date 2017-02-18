package fle.core.common;

import fle.core.handler.FleEntityHandler;
import fle.core.tile.ditchs.DefaultDitchFactory;
import fle.loader.BlocksItems;
import fle.loader.Entities;
import fle.loader.FLEConfig;
import fle.loader.Materials;
import fle.loader.Recipes;
import fle.loader.Tools;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonLoader
{
	public void init(FMLPreInitializationEvent event, Configuration config)
	{
		FLEConfig.init(config);
		new DefaultDitchFactory();
		Materials.preinit();
		BlocksItems.registerItemsAndBlocks();
		Tools.initalizeTools();
		BlocksItems.setBlocksItemsProperties();
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
		Entities.commonInit();
	}
	
	public void init(FMLInitializationEvent event)
	{
		Recipes.init();
	}
}