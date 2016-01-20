package flapi.energy;

import farcore.world.IObjectInWorld;

public interface IEnergyStorage extends IObjectInWorld
{
	long getEnergyStorage();
	
	long getEnergyLastIO();
}