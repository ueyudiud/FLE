package fla.api;

import fla.api.util.IColorMapManager;
import fla.api.world.IWorldManager;

public interface Mod 
{
	public String getModName();
	
	public IWorldManager getWorldManager();
	
	public IColorMapManager getColorMapManager();
}
