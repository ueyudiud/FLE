/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common;

import fle.core.handler.FleEntityHandler;
import fle.core.tile.ditchs.DefaultDitchFactory;
import fle.loader.Configs;
import fle.loader.Entities;
import fle.loader.FW;
import fle.loader.Fuels;
import fle.loader.IBFS;
import fle.loader.Lang;
import fle.loader.Materials;
import fle.loader.Recipes;
import fle.loader.Tools;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author ueyudiud
 */
public class CommonLoader
{
	public void init(FMLPreInitializationEvent event)
	{
		Configs.init();
		new DefaultDitchFactory();
		Materials.preinit();
		IBFS.registerItemsAndBlocks();
		Tools.initalizeTools();
		IBFS.setBlocksItemsProperties();
		MinecraftForge.EVENT_BUS.register(new FleEntityHandler());
		Entities.commonInit();
	}
	
	public void init(FMLInitializationEvent event)
	{
		Fuels.init();
		FW.init();
		Recipes.init();
		Lang.init();
	}
}
