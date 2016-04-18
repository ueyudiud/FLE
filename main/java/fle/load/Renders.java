package fle.load;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.lib.render.RenderTreeSapling;
import farcore.util.FleLog;
import fle.core.render.RenderFire;
import fle.core.render.RenderTorch;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class Renders
{
	public static void init()
	{
		FleLog.getLogger().info("Start register renderers.");
		FarCoreSetup.handlerA.register(BlockItems.torch, OreDictionary.WILDCARD_VALUE, RenderTorch.class);
		FarCoreSetup.handlerA.register(BlockItems.fire, OreDictionary.WILDCARD_VALUE, RenderFire.class);
		FarCoreSetup.handlerA.register(BlockItems.sapling, OreDictionary.WILDCARD_VALUE, RenderTreeSapling.class);
	}
}