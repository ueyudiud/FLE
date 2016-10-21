package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitAnd extends TECircuitTripleInput
{
	@Override
	protected void updateCircuit()
	{
		boolean flag1 = getWeakPower(Facing.LEFT) != 0 || getStrongPower(Facing.LEFT) != 0;
		boolean flag2 = getWeakPower(Facing.RIGHT) != 0 || getStrongPower(Facing.RIGHT) != 0;
		boolean flag3 = getWeakPower(Facing.BACK) != 0 || getStrongPower(Facing.BACK) != 0;
		if(flag1 && flag2 && flag3)
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
}