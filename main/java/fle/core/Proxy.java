package fle.core;

public abstract class Proxy
{
	public abstract void onPreload();
	
	public abstract void onLoad();
	
	public abstract void onPostload();
	
	public abstract void onCompleteLoad();
}
