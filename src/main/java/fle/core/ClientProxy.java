package fle.core;

import farcore.FarCore;
import farcore.FarCoreRegistry;
import fle.load.Icons;
import fle.load.Renders;

public class ClientProxy extends CommonProxy
{
	public ClientProxy()
	{
		
	}
	
	@Override
	public void load()
	{
		super.load();
		FarCore.bottonTextureMap.registerTextureListener(new Icons.Bottoms());
		FarCore.conditionTextureMap.registerTextureListener(new Icons.Conditions());
		Renders.init();
	}
}