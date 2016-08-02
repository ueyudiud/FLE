package fargen.core.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWorldPropProvider
{
	float getHumidity(World world, BlockPos pos);

	float getTemperature(World world, BlockPos pos);
	
	float getSunshine(World world, BlockPos pos);

	float getRainstrength(World world, BlockPos pos);
	
	float getSkylight(World world);
}