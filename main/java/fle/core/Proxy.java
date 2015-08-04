package fle.core;

import cpw.mods.fml.common.network.IGuiHandler;

public abstract class Proxy implements IGuiHandler
{
	public abstract void onPreload();
	
	public abstract void onLoad();
	
	public abstract void onPostload();
	
	public abstract void onCompleteLoad();
	
	public void onIconRegister(){}
}
