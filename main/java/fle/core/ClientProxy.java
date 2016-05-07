package fle.core;

import farcore.FarCore;
import farcore.FarCoreRegistry;
import fle.api.FleAPI;
import fle.load.Icons;
import fle.load.Renders;

public class ClientProxy extends CommonProxy
{
	public ClientProxy()
	{
		FleAPI.conditionTextureMap = FarCoreRegistry.newTextureMap("conditions");
	}
	
	@Override
	public void load()
	{
		super.load();
		FarCore.bottonTextureMap.registerTextureListener(new Icons.Bottoms());
		FleAPI.conditionTextureMap.registerTextureListener(new Icons.Conditions());
		Renders.init();
	}
}