package farcore.biology.plant;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import farcore.block.IPropagatedableBlock;

/**
 * The species belong to coverable block surface plant (or fungus), such as
 * grass block or mycelium.
 * 
 * @author ueyudiud Entity Kingdom >> Plant Phylum >> Coverable Family
 */
public interface ICoverablePlantSpecies extends IPlantSpecies
{
	@Override
	default String family()
	{
		return "lichen";
	}
	
	@Override
	default boolean canPlant(World world, BlockPos pos)
	{
		if (pos.getY() < 10)
			return false;
		if (world.getBlockState(pos).getBlock() instanceof IPropagatedableBlock)
		{
			return ((IPropagatedableBlock) world.getBlockState(pos).getBlock())
					.canPropagatedable(world, pos, this);
		}
		return false;
	}
	
	/**
	 * Get plant spread loop.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	int getSpreadLoop(World world, BlockPos pos);
	
	/**
	 * Get plant spread range.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	int getSpreadRange(World world, BlockPos pos);
	
	/**
	 * Match this plant can cover on this block.
	 * 
	 * @param world
	 * @param pos
	 * @param state
	 * @return
	 */
	boolean matchBlock(World world, BlockPos pos, IBlockState state);
}