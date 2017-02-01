package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;

public class TECircuitXor extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Right = 0x6;

	@Override
	protected void updateCircuit()
	{
		boolean flag1 = getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		boolean flag2 = getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		set(Left, flag1);
		set(Right, flag2);
		if(flag1 ^ flag2)
		{
			setWeakPower(15);
			setStrongPower(15);
		}
		else
		{
			setWeakPower(0);
			setStrongPower(0);
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side != Facing.FRONT.toDirection(facing) && super.canConnectRedstone(state, side);
	}

	@Override
	public String getState()
	{
		return optional(Left, "e", "d") + optional(Right, "e", "d");
	}
}