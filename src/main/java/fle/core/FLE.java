package fle.core;

import fle.core.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FLE.MODID, version = FLE.VERSION, name = FLE.NAME, dependencies = "required-after:farcore")
public class FLE
{
	public static final String MODID = "fle";
	public static final String NAME = "Far Land Era";
	public static final String VERSION = FLEVersion.MAJOR_VERSION + "." + FLEVersion.MINOR_VERSION + "." + FLEVersion.SUB_VERSION;

	@Instance(FLE.MODID)
	public static FLE mod;
	
	@SidedProxy(serverSide = "fle.core.common.CommonProxy", clientSide = "fle.core.client.ClientProxy")
	public static CommonProxy proxy;

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
		proxy.init(event);
	}
}