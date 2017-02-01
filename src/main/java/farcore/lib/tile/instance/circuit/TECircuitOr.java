package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitOr extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Back = 0x6;
	private static final int Right = 0x7;
	
	@Override
	protected void updateCircuit()
	{
		boolean flag1 = getWeakPower(Facing.BACK) != 0 || getStrongPower(Facing.BACK) != 0;
		boolean flag2 = getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		boolean flag3 = getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		set(Left, flag2);
		set(Back, flag1);
		set(Right, flag3);
		if(flag1 || flag2 || flag3)
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
		return optional(Right, "e", "d") + optional(Back, "e", "d") + optional(Left, "e", "d");
	}
}