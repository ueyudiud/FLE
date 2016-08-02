package fargen.core;

import farcore.lib.util.Log;
import fargen.core.util.Util;
import fargen.core.world.WorldPropHandler;
import fargen.core.world.WorldPropSurface;
import fargen.core.worldgen.FarWorldType;
import fargen.core.worldgen.surface.FarSurfaceProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FarGen.ID, version = FarGen.VERSION, name = "Far Generation",
dependencies = "required-after:farcore")
public class FarGen
{
	public static final String ID = "fg";
	public static final String VERSION = "0.1";
	
	public static DimensionType FAR_OVERWORLD;
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.version = VERSION;
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = "Far Core";
		modMetadata.credits = "ueyudiud";
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		Log.info("Far generation start override world type.");
		FarWorldType.DEFAULT = new FarWorldType(0, "far_default", "Far Default");
		DimensionManager.unregisterDimension(0);
		FAR_OVERWORLD = Util.newDimensionType("FAR_OVERWORLD", 0, "far_overworld", "", FarSurfaceProvider.class);
		DimensionManager.registerDimension(0, FAR_OVERWORLD);
		Log.info("Far generation overrided world type.");
		WorldPropHandler.addWorldProperty(0, new WorldPropSurface());
	}
}