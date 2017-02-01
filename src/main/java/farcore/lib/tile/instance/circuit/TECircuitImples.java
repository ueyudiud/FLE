package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitImples extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Right = 0x6;

	@Override
	protected void updateCircuit()
	{
		boolean flag = (mode & 0x1) != 0;
		boolean flag1 = getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		boolean flag2 = getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		set(Left, flag1);
		set(Right, flag2);
		if(flag)
		{
			flag1 ^= flag2;
			flag2 ^= flag1;
			flag1 ^= flag2;
		}
		if(flag1 || !flag2)
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
	public String getState()
	{
		return ((mode & 0x1) != 0 ? "r" : "l") + optional(Left, "e", "d") + optional(Right, "e", "d");
	}
}