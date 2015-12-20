package fle.core.init;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import fle.core.world.dim.FLENetherProvider;
import fle.core.world.dim.FLESurfaceProvider;
import fle.core.world.dim.FLEWorldType;

public class WorldCof
{
	public static void init()
	{
		FLEWorldType.DEFAULT = new FLEWorldType(WorldType.DEFAULT.getWorldTypeID(), "DEFAULT", "FLE Default");
		FLEWorldType.FLAT = new FLEWorldType(WorldType.FLAT.getWorldTypeID(), "FLAT", "FLE Flat");
		FLEWorldType.LARGE_BIOMES = new FLEWorldType(WorldType.LARGE_BIOMES.getWorldTypeID(), "LARGE", "FLE Large Biome");
		
		DimensionManager.unregisterDimension(-1);
		DimensionManager.unregisterDimension(0);
		//DimensionManager.unregisterDimension(1);

		DimensionManager.unregisterProviderType(-1);
		DimensionManager.unregisterProviderType(0);
		//DimensionManager.unregisterProviderType(1);

		DimensionManager.registerProviderType(-1, FLENetherProvider.class, true);
		DimensionManager.registerProviderType(0, FLESurfaceProvider.class, true);
		
	    DimensionManager.registerDimension(-1, -1);
	    DimensionManager.registerDimension(0, 0);
	    //DimensionManager.registerDimension(1, 1);
	}
}