package flapi.energy;

import net.minecraftforge.common.util.ForgeDirection;
import flapi.world.BlockPos;

public abstract class ThermalNet extends IEnergyNet
{
	@Override
	public final EnumEnergyType getEnergyType()
	{
		return EnumEnergyType.THERMAL;
	}
	
	public abstract int getEnvironmentTemperature(BlockPos pos);

	public abstract void emmitHeat(BlockPos pos);
	
	public abstract void emmitHeatTo(BlockPos pos, ForgeDirection dir);
}