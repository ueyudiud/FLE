/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public interface IBlockStayabilityCheck
{
	boolean canBlockStayAt(World world, BlockPos pos, IBlockState state);
}
