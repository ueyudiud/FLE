package fle.cwg;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import farcore.FarCore;
import fle.api.FleAPI;
import fle.core.world.gen.FleSurfaceProvider;
import fle.core.world.gen.FleWorldType;
import fle.cwg.world.FCWGWorldInfo;
import fle.cwg.world.FCWGWorldType;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

@Mod(modid = FarCore.CUSTOM_WORLD_GEN_ID, name = "Far Custom World Generation", version = "0.1", 
dependencies = "required-after:" + FarCore.ID + "; required-after:fle")
public class FarCustomWorldGen
{
	@Instance(FarCore.CUSTOM_WORLD_GEN_ID)
	public static FarCustomWorldGen instance;
	
	public FarCustomWorldGen()
	{
		instance = this;
	}
	
	@EventHandler
	public void init(FMLPostInitializationEvent event)
	{
		FCWGWorldType.FCWG = new FCWGWorldType("Customlize");
	}
}