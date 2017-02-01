package nebula.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * For stability block uses, the block fallen
 * will check range of block falling. To prevent
 * too many block falling, the second notify
 * order will be prevent.
 * @author ueyudiud
 *
 */
public interface IUpdateDelayBlock
{
	default int getCheckRange(IBlockState state) { return 2; }
	
	default void notifyAfterTicking(IBlockState state, World world, BlockPos pos, IBlockState changed)
	{
		state.neighborChanged(world, pos, changed.getBlock());
	}
}