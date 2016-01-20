package farcore.block;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import farcore.biology.plant.IPlant;
import farcore.biology.plant.IPlantSpecies;

/**
 * A interface about dirt type block. Use to check if a propagatedable block can
 * grow new plant.
 * 
 * @author ueyudiud
 * 		
 */
public interface IPropagatedableBlock
{
	/**
	 * Match a plant can propagate on this block.
	 * 
	 * @param world
	 * @param pos
	 * @param specie
	 * @return
	 */
	boolean canPropagatedable(World world, BlockPos pos, IPlantSpecies specie);
	
	void onPropagatedable(World world, BlockPos pos, IPlant specie);
	
	IPlant getSpecie(World world, BlockPos pos);
	
	boolean setDead(World world, BlockPos pos);
}