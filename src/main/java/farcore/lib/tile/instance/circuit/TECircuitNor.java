package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitNor extends TECircuitTripleInput
{
	@Override
	protected void updateCircuit()
	{
		boolean flag = getWeakPower(Facing.BACK) != 0 || getStrongPower(Facing.BACK) != 0;
		flag |= getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		flag |= getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		setWeakPower(flag ? 0 : 15);
		setStrongPower(flag ? 0 : 15);
	}
}
