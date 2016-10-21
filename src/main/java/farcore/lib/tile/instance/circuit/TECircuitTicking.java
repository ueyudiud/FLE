package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Facing;

public class TECircuitTicking extends TECircuitFrontBack
{
	private static final int Enabled = 0x5;

	@Override
	protected void updateBody()
	{
		super.updateBody();
		if(is(Enabled))
		{
			if(updateDelay == 0)
			{
				if(weakPower != 0)
				{
					setStrongPower(0);
					setWeakPower(0);
				}
				else
				{
					setStrongPower(15);
					setWeakPower(15);
				}
				markForDelayUpdate(4 * (1 + (updateDelay & 0xF)));
			}
		}
		else
		{
			setStrongPower(0);
			setWeakPower(0);
		}
	}

	@Override
	protected void updateCircuit()
	{
		int weak = getWeakPower(Facing.BACK);
		int strong = getStrongPower(Facing.BACK);
		boolean flag = weak != 0 || strong != 0;
		if(flag)
		{
			enable(Enabled);
		}
		else
		{
			disable(Enabled);
		}
	}
}