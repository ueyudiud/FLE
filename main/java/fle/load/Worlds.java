package fle.load;

import farcore.util.FleLog;
import fle.core.world.gen.FleSurfaceProvider;
import fle.core.world.gen.FleWorldType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class Worlds
{
	public static void init()
	{
		FleLog.getLogger().info("Start override and register world type.");
		
		FleWorldType.DEFAULT = new FleWorldType(WorldType.DEFAULT.getWorldTypeID(), "DEFAULT");
		
		DimensionManager.unregisterDimension(0);
		
	    DimensionManager.unregisterProviderType(0);
	    
	    DimensionManager.registerProviderType(0, FleSurfaceProvider.class, true);
	    
	    DimensionManager.registerDimension(0, 0);
	}
}