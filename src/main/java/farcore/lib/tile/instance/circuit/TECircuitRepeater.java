package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitRepeater extends TECircuitFrontBack
{
	@Override
	protected void updateBody()
	{
		super.updateBody();
		if(updateDelay == 0)
		{
			int weak = getWeakPower(Facing.BACK);
			int strong = getStrongPower(Facing.BACK);
			if(weak != 0 || strong != 0)
			{
				setStrongPower(15);
				setWeakPower(15);
			}
			else
			{
				setStrongPower(0);
				setWeakPower(0);
			}
		}
	}

	@Override
	protected void updateCircuit()
	{
		if(updateDelay > 0) return;
		int weak = getWeakPower(Facing.BACK);
		int strong = getStrongPower(Facing.BACK);
		if((weak != 0 || strong != 0) != (weakPower != 0))
		{
			markForDelayUpdate((mode + 1) * 2);
		}
	}
}