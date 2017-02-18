package fle.core;

import fle.core.common.CommonLoader;
import nebula.common.util.ModCompator;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FLE.MODID, version = FLE.VERSION, name = FLE.NAME, dependencies = "required-after:farcore")
public class FLE
{
	public static final String MODID = "fle";
	public static final String NAME = "Far Land Era";
	public static final String VERSION = FLEVersion.MAJOR_VERSION + "." + FLEVersion.MINOR_VERSION + "." + FLEVersion.SUB_VERSION;
	
	@Instance(FLE.MODID)
	public static FLE mod;
	
	@SidedProxy(serverSide = "fle.core.common.CommonLoader", clientSide = "fle.core.client.ClientLoader")
	public static CommonLoader loader;
	
	public static ModCompator compator;
	
	public FLE()
	{
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = NAME;
		modMetadata.credits = "ueyudiud";
		modMetadata.version = FLEVersion.isSnapshotVersion() ? VERSION : VERSION + "-pre" + FLEVersion.SNAPSHOT_VERSION;
		compator = ModCompator.newCompactor();
		Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
		configuration.load();
		loader.init(event, configuration);
		configuration.save();
		compator.addCompatible("fg", "fargen.compact.fle.SubCompact");
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		loader.init(event);
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		compator.call("init");
	}
}