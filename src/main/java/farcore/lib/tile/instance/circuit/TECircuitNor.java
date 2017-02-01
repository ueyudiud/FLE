package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitNor extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Back = 0x6;
	private static final int Right = 0x7;

	@Override
	protected void updateCircuit()
	{
		boolean flag = getWeakPower(Facing.BACK) != 0 || getStrongPower(Facing.BACK) != 0;
		flag |= getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		flag |= getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		setWeakPower(flag ? 0 : 15);
		setStrongPower(flag ? 0 : 15);
	}

	@Override
	public String getState()
	{
		return optional(Left, "e", "d") + optional(Back, "e", "d") + optional(Right, "e", "d");
	}
}