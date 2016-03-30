package fle.core;

import farcore.FarCoreSetup;
import fle.core.render.RenderTorch;
import fle.load.BlockItems;
import fle.load.Renders;
import net.minecraftforge.oredict.OreDictionary;

public class ClientProxy extends CommonProxy
{
	@Override
	public void load()
	{
		super.load();
		Renders.init();
	}
}