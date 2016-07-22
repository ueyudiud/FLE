package farcore.lib.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IUpdatableTile
{
	void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate);
}
