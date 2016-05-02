package fle.core;

import farcore.FarCore;
import farcore.FarCoreSetup;
import fle.core.render.RenderTorch;
import fle.load.BlockItems;
import fle.load.Icons;
import fle.load.Renders;
import net.minecraftforge.oredict.OreDictionary;

public class ClientProxy extends CommonProxy
{
	public ClientProxy()
	{
		
	}
	
	@Override
	public void load()
	{
		super.load();
		FarCore.bottonTextureMap.registerTextureListener(new Icons());
		Renders.init();
	}
}