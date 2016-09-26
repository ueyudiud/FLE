package fargen.compact.fle;

import farcore.FarCore;
import farcore.util.U;
import fargen.core.FarGen;
import fargen.core.worldgen.v.FarVoidProvider;
import fle.api.FLEAPI;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class SubCompact
{
	public static void init()
	{
		if(U.Mod.isModLoaded(FarCore.MAIN_MOD_ID))
		{
			FLEAPI.voidDimID = 10;
			FarGen.FAR_VOID = DimensionType.register("FAR_VOID", "far_void", FLEAPI.voidDimID, FarVoidProvider.class, false);
			DimensionManager.registerDimension(FLEAPI.voidDimID, FarGen.FAR_VOID);
		}
	}
}
