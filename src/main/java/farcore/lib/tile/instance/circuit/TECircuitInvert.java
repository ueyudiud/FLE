package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class TECircuitInvert extends TECircuitSpatial
{
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		byte power = 0;
		power = getPowerHigherThan(powerFB, power, Facing.FRONT);
		power = getPowerHigherThan(powerFB, power, Facing.BACK);
		powerFB = power;
		boolean flag = power > 0;
		power = 0;
		power = getPowerHigherThan(powerLR, power, Facing.LEFT);
		power = getPowerHigherThan(powerLR, power, Facing.RIGHT);
		powerLR = !flag ? 15 : power;
		notifyNeighbors();
	}
}