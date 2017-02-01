package farcore.lib.world;

import nebula.common.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWorldPropProvider
{
	default float getHumidity(ICoord coord)
	{
		return getHumidity(coord.world(), coord.pos());
	}

	float getHumidity(World world, BlockPos pos);
	
	default float getAverageHumidity(ICoord coord)
	{
		return getAverageHumidity(coord.world(), coord.pos());
	}
	
	float getAverageHumidity(World world, BlockPos pos);
	
	default float getTemperature(ICoord coord)
	{
		return getTemperature(coord.world(), coord.pos());
	}

	float getTemperature(World world, BlockPos pos);
	
	default float getAverageTemperature(ICoord coord)
	{
		return getAverageTemperature(coord.world(), coord.pos());
	}
	
	float getAverageTemperature(World world, BlockPos pos);
	
	default float getSunshine(ICoord coord)
	{
		return getSunshine(coord.world(), coord.pos());
	}

	float getSunshine(World world, BlockPos pos);

	float getRainstrength(World world, BlockPos pos);
	
	float getSkylight(World world);
	
	/**
	 * Each world might have a main fluid block, such
	 * as water in overworld, lava in hell, etc. This
	 * method is to get the main fluid of this world.
	 * @return
	 */
	Block getMainFluidBlock();
	
	/**
	 * The fluid block should freeze at position in
	 * this world type.
	 * @param world
	 * @param pos
	 * @return
	 */
	boolean canMainFluidBlockFreeze(World world, BlockPos pos);

	void freezeMainFluidAt(World world, BlockPos pos);
}