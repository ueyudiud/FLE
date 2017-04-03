package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;

public class TECircuitCross extends TECircuitSpatial
{
	@Override
	protected void updateCircuit()
	{
		byte power = 0;
		power = getPowerHigherThan(this.powerFB, power, Facing.FRONT);
		power = getPowerHigherThan(this.powerFB, power, Facing.BACK);
		this.powerFB = power;
		power = 0;
		power = getPowerHigherThan(this.powerLR, power, Facing.LEFT);
		power = getPowerHigherThan(this.powerLR, power, Facing.RIGHT);
		this.powerLR = power;
	}
	
	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return Worlds.isRedstoneChecking() ? 0 : super.getWeakPower(state, side);
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return Worlds.isRedstoneChecking() ? 0 : super.getStrongPower(state, side);
	}
}