package fargen.core;

import farcore.asm.ClientOverride;
import farcore.lib.util.Log;
import farcore.lib.world.WorldPropHandler;
import fargen.core.render.RenderSurface;
import fargen.core.world.WorldPropSurface;
import fargen.core.worldgen.FarWorldType;
import fargen.core.worldgen.surface.FarSurfaceProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = FarGen.ID, version = FarGen.VERSION, name = FarGen.NAME,
dependencies = "required-after:farcore")
public class FarGen
{
	public static final String ID = "fg";
	public static final String VERSION = "0.2";
	public static final String NAME = "Far Generation";
	
	public static DimensionType FAR_OVERWORLD;

	@SidedProxy(serverSide = "fargen.core.FarGen$Common", clientSide = "fargen.core.FarGen$Client")
	public static Common proxy;
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.version = VERSION;
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = NAME;
		modMetadata.credits = "ueyudiud";
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		FarGenBiomes.init();
		Log.info("Far generation start override world type.");
		FarWorldType.DEFAULT = new FarWorldType(0, "far_default", "Far Default");
		DimensionManager.unregisterDimension(0);
		FAR_OVERWORLD = DimensionType.register("FAR_OVERWORLD", "far_overworld", 0, FarSurfaceProvider.class, false);
		DimensionManager.registerDimension(0, FAR_OVERWORLD);
		Log.info("Far generation overrided world type.");
		WorldPropHandler.addWorldProperty(0, new WorldPropSurface());
		proxy.load();
	}

	public static class Common
	{
		void load()
		{

		}
	}

	@SideOnly(Side.CLIENT)
	public static class Client extends Common
	{
		@Override
		void load()
		{
			ClientOverride.RENDERS.add(new RenderSurface());
		}
	}
}