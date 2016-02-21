package farcore.util;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public interface IInitialization
{
	void load(FMLPreInitializationEvent event);
	
	void load(FMLInitializationEvent event);
	
	void load(FMLPostInitializationEvent event);
	
	void load(FMLLoadCompleteEvent event);
	
	void load(FMLServerStartingEvent event);
}