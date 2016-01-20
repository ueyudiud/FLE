package flapi.energy;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IThermalNet extends IEnergyNet
{
	void emmit(World world, BlockPos pos);
	
	long getEnviormentTemperature(World world, BlockPos pos);
}