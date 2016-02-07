package farcore.biology.plant;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import farcore.biology.ISpecies;
import farcore.block.IPropagatedableBlock;

/**
 * The species belong to plant (or fungus).
 * 
 * @author ueyudiud Entity Kingdom >> Plant Phylum
 */
public abstract class IPlantSpecies extends ISpecies
{
	@Override
	public String phylum()
	{
		return "plant";
	}
	
	/**
	 * Match whether a plant can grow in this condition.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public abstract boolean canGrow(World world, BlockPos pos);
	
	/**
	 * Match whether a plant can plant on the locate.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public boolean canPlant(World world, BlockPos pos)
	{
		if (pos.getY() < 10)
			return false;
		if (world.getBlockState(pos.down())
				.getBlock() instanceof IPropagatedableBlock)
		{
			return ((IPropagatedableBlock) world.getBlockState(pos.down())
					.getBlock()).canPropagatedable(world, pos.down(), this);
		}
		return false;
	}
	
	/**
	 * Match ths plant is belong to this specie.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public abstract boolean isBelongTo(World world, BlockPos pos);
	
	/**
	 * Plant the plant to the target block.
	 * 
	 * @param world
	 * @param pos
	 * @param iBiology
	 */
	public abstract void onPlant(World world, BlockPos pos, IPlant plant);
	
	/**
	 * The instance should be a plant.
	 */
	@Override
	public abstract IPlant instance();
}