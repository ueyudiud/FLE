/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import nebula.common.world.ICoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IUpdatableTile extends ICoord
{
	void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate);
}