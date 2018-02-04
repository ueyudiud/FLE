/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author ueyudiud
 */
public interface IFallingStaySupport
{
	int getFallWeight(IBlockAccess world, BlockPos pos, IBlockState state, boolean stableOnly);
}
