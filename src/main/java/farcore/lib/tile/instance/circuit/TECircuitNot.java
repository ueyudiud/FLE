package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitNot extends TECircuitFrontBack
{
	@Override
	protected void updateCircuit()
	{
		int weak = getWeakPower(Facing.BACK);
		int strong = getStrongPower(Facing.BACK);
		boolean flag = weak != 0 || strong != 0;
		int mix = Math.max(weak, strong);
		int value = (mode & 0x1) != 0 ? 15 - mix : (mix != 0 ? 0 : 15);
		setWeakPower(value);
		setStrongPower(value);
	}
}