package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitOr extends TECircuitTripleInput
{
	@Override
	protected void updateCircuit()
	{
		boolean flag = getWeakPower(Facing.BACK) != 0 || getStrongPower(Facing.BACK) != 0;
		flag |= getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		flag |= getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		setWeakPower(flag ? 15 : 0);
		setStrongPower(flag ? 15 : 0);
	}
}
