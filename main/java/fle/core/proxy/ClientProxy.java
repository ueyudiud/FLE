package fle.core.proxy;

import fle.core.init.Renders;

public class ClientProxy extends CommonProxy
{
	@Override
	public void onPreload()
	{
		super.onPreload();
		Renders.preinit();
	}
	
	@Override
	public void onCompleteLoad()
	{
		super.onCompleteLoad();
		Renders.init();
	}
}