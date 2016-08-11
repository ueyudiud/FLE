package farcore.lib.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWorldPropProvider
{
	float getHumidity(World world, BlockPos pos);

	float getAverageHumidity(World world, BlockPos pos);
	
	float getTemperature(World world, BlockPos pos);

	float getAverageTemperature(World world, BlockPos pos);

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