package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitNot extends TECircuitFrontBack
{
	private static final int Actived = 0x5;
	
	@Override
	protected void updateCircuit()
	{
		int weak = getWeakPower(Facing.BACK);
		int strong = getStrongPower(Facing.BACK);
		boolean flag = weak != 0 || strong != 0;
		set(Actived, flag);
		int mix = Math.max(weak, strong);
		int value = (mode & 0x1) != 0 ? 15 - mix : (mix != 0 ? 0 : 15);
		setWeakPower(value);
		setStrongPower(value);
	}

	@Override
	public String getState()
	{
		return optional(Actived, "on", "off");
	}
}