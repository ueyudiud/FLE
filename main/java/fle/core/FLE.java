package fle.core;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import farcore.FarCore;
import farcore.lib.container.GuiFactory;
import farcore.util.FleLog;
import fle.api.FleAPI;
import fle.core.util.FleGuiFactory;
import fle.load.Biomes;
import fle.load.BlockItems;
import fle.load.Drops;
import fle.load.Handlers;
import fle.load.Langs;
import fle.load.OreDicts;
import fle.load.Recipes;
import fle.load.Substances;
import fle.load.Worlds;

@Mod(modid = FleAPI.ID, name = FLE.NAME, version = FLE.VERSION, dependencies = "required-after:" + FarCore.ID)
public class FLE
{
	public static final String NAME = "Far Land Era";
	public static final String VERSION = "3.00a";

	@Instance(FleAPI.ID)
	public static FLE mod;
	
	@SidedProxy(serverSide = "fle.core.CommonProxy", clientSide = "fle.core.ClientProxy")
	public static CommonProxy proxy;
	
	public FLE()
	{
		mod = this;
		FleLog.logger = LogManager.getLogger(FleAPI.ID);
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		FleLog.getLogger().info("Start pre-loading.");
		ModMetadata meta = event.getModMetadata();
		meta.name = NAME;
		meta.version = VERSION;
		meta.credits = "ueyudiud";
		meta.authorList = Arrays.asList("ueyudiud", "huangziye812");
		meta.logoFile = "";
		FleAPI.guiFactory = new FleGuiFactory();
		Substances.init();
		BlockItems.init();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		FleLog.getLogger().info("Start loading.");
		Handlers.init();
		Langs.init();
		OreDicts.init();
		Drops.init();
		Worlds.init();
		Biomes.init();
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		FleLog.getLogger().info("Start post-loading.");
		Recipes.init();
		proxy.load();
	}
	
	@EventHandler
	public void load(FMLLoadCompleteEvent event)
	{
		FleLog.getLogger().info("Start complete-loading.");
	}
}