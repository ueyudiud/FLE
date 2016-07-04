package fle.load;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.lib.render.RenderTreeSapling;
import farcore.lib.render.RenderVine;
import farcore.util.FleLog;
import fle.core.render.RenderCampfire;
import fle.core.render.RenderDryingTable;
import fle.core.render.RenderFire;
import fle.core.render.RenderOre;
import fle.core.render.RenderTerrine;
import fle.core.render.RenderTorch;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class Renders
{
	public static void init()
	{
		FleLog.getLogger().info("Start register renderers.");
		FarCore.handlerA.register(BlockItems.torch, OreDictionary.WILDCARD_VALUE, RenderTorch.class);
		FarCore.handlerA.register(BlockItems.fire, OreDictionary.WILDCARD_VALUE, RenderFire.class);
		FarCore.handlerA.register(BlockItems.vine1, OreDictionary.WILDCARD_VALUE, RenderVine.class);
		FarCore.handlerA.register(BlockItems.campfire, OreDictionary.WILDCARD_VALUE, RenderCampfire.class);
		FarCore.handlerA.register(BlockItems.argil, 0, RenderTerrine.class);
		FarCore.handlerA.register(BlockItems.argilUnsmelted, 0, RenderTerrine.class);
		FarCore.handlerB.register(BlockItems.ore, OreDictionary.WILDCARD_VALUE, RenderOre.class);
		FarCore.handlerB.register(BlockItems.machineIIAlpha, 0, RenderDryingTable.class);
	}
}