package fle.core.proxy;

import net.minecraftforge.fml.common.network.IGuiHandler;

public abstract class Proxy implements IGuiHandler
{
	public abstract void onPreload();
	
	public abstract void onLoad();
	
	public abstract void onPostload();
	
	public abstract void onCompleteLoad();
	
	public void onIconRegister(){}

	public void onServerLoad(){	}
}