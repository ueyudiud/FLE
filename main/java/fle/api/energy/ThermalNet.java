package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

public abstract class ThermalNet extends IEnergyNet
{
	public abstract int getEnvironmentTemperature(BlockPos pos);

	public abstract void emmitHeat(BlockPos pos);
	
	public abstract void emmitHeatTo(BlockPos pos, ForgeDirection dir);
}