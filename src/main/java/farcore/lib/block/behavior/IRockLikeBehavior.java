/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.block.behavior;

import nebula.common.block.IBlockBehavior;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public interface IRockLikeBehavior<B> extends IBlockBehavior<B>
{
	void scheduleFalling(B block, World world, BlockPos pos, IBlockState state, int delayMultiplier);
	
	boolean onCaveInCheck(B block, World world, BlockPos pos, IBlockState state);
}
