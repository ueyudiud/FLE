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
public abstract class ICoverablePlantSpecies extends IPlantSpecies
{
	@Override
	public String family()
	{
		return "lichen";
	}
	
	@Override
	public boolean canPlant(World world, BlockPos pos)
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
	public abstract int getSpreadLoop(World world, BlockPos pos);
	
	/**
	 * Get plant spread range.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public abstract int getSpreadRange(World world, BlockPos pos);
	
	/**
	 * Match this plant can cover on this block.
	 * 
	 * @param world
	 * @param pos
	 * @param state
	 * @return
	 */
	public abstract boolean matchBlock(World world, BlockPos pos,
			IBlockState state);
}