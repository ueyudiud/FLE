package fargen.compact.fle;

import farcore.lib.util.ModCompator.ICompatible;
import fargen.core.FarGen;
import fargen.core.worldgen.v.FarVoidProvider;
import fle.api.FLEAPI;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class SubCompact implements ICompatible
{
	@Override
	public void call(String phase) throws Exception
	{
		if(phase.equals("init"))
		{
			FLEAPI.voidDimID = 10;
			FarGen.FAR_VOID = DimensionType.register("FAR_VOID", "far_void", FLEAPI.voidDimID, FarVoidProvider.class, false);
			DimensionManager.registerDimension(FLEAPI.voidDimID, FarGen.FAR_VOID);
		}
	}
}
