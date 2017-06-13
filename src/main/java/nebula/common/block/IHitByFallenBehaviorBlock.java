/*
 * copyright© 2016-2017 ueyudiud
 */

/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The block has custom behavior when hit by fallen block.
 * @author ueyudiud
 * @see nebula.common.entity.EntityFallingBlockExtended
 */
public interface IHitByFallenBehaviorBlock
{
	/**
	 * Get should block fall down from this block, return true to ignore stay checking.
	 * @param world
	 * @param pos
	 * @param state
	 * @param fallen
	 * @return
	 */
	boolean isPermeatableBy(World world, BlockPos pos, IBlockState state, IBlockState fallen);
	
	/**
	 * Called when block fall on, if return true, the falling block will try to replace block at this position.
	 * @param world
	 * @param pos
	 * @param state
	 * @param fallen
	 * @param height
	 * @return
	 */
	boolean onFallingOn(World world, BlockPos pos, IBlockState state, IBlockState fallen, int height);
}