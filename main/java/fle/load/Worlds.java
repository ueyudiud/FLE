package fle.load;

import fle.core.world.gen.FleSurfaceProvider;
import fle.core.world.gen.FleWorldType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class Worlds
{
	public static void init()
	{
		FleWorldType.DEFAULT = new FleWorldType(WorldType.DEFAULT.getWorldTypeID(), "DEFAULT", "FLE Default");
		
		DimensionManager.unregisterDimension(0);
		
	    DimensionManager.unregisterProviderType(0);
	    
	    DimensionManager.registerProviderType(0, FleSurfaceProvider.class, true);
	    
	    DimensionManager.registerDimension(0, 0);
	}
}