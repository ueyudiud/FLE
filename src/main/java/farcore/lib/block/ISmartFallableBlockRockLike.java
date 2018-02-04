/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block;

import nebula.common.block.ISmartFallableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public interface ISmartFallableBlockRockLike extends ISmartFallableBlock, IFallingStaySupport
{
	default int getFallWeight(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return 100;
	}
	
	boolean onCaveInCheck(World world, BlockPos pos, IBlockState state);
	
	default void scheduleFalling(World world, BlockPos pos, IBlockState state, int delayMultiplier)
	{
	}
}
