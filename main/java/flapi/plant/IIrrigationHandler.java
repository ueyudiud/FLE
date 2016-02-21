package flapi.plant;

import net.minecraftforge.common.util.ForgeDirection;

public interface IIrrigationHandler
{
	boolean canIrrigate(ForgeDirection dir);
	
	int doIrrigate(ForgeDirection dir, int amount);
}