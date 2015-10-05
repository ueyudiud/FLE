package fle.core.init;

import fle.core.world.dim.FLESurfaceManager;
import fle.core.world.dim.FLESurfaceProvider;
import fle.core.world.dim.FLEWorldType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class WorldCof
{
	public static void init()
	{
		FLEWorldType.DEFAULT = new FLEWorldType(WorldType.DEFAULT.getWorldTypeID(), "DEFAULT");
		FLEWorldType.FLAT = new FLEWorldType(WorldType.FLAT.getWorldTypeID(), "FLAT");
		FLEWorldType.LARGE_BIOMES = new FLEWorldType(WorldType.LARGE_BIOMES.getWorldTypeID(), "LARGE");
		
		//DimensionManager.unregisterDimension(-1);
		DimensionManager.unregisterDimension(0);
		//DimensionManager.unregisterDimension(1);

		//DimensionManager.unregisterProviderType(-1);
		DimensionManager.unregisterProviderType(0);
		//DimensionManager.unregisterProviderType(1);

		DimensionManager.registerProviderType(0, FLESurfaceProvider.class, true);
		
	    //DimensionManager.registerDimension(-1, -1);
	    DimensionManager.registerDimension(0, 0);
	    //DimensionManager.registerDimension(1, 1);
	}
}