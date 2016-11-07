package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Direction;
import farcore.lib.util.Facing;
import net.minecraft.entity.player.EntityPlayer;

public class TECircuitTicking extends TECircuitFrontBack
{
	private static final int Enabled = 0x5;
	private static final int Actived = 0x6;

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
					disable(Actived);
				}
				else
				{
					setStrongPower(15);
					setWeakPower(15);
					enable(Actived);
				}
				markForDelayUpdate(4 * (1 + (mode & 0xF)));
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
			disable(Actived);
		}
	}

	@Override
	protected void onScrewDriverUsed(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		mode++;
		if(mode == 16)
		{
			mode = 0;
		}
		syncToNearby();
	}
	
	@Override
	protected int getRenderUpdateRange()
	{
		return is(Actived) ? 2 : 3;//Will be re-render frequently.
	}
	
	@Override
	public String getState()
	{
		return (is(Enabled) ? "on_" + (is(Actived) ? "a" : "b") : "off");
	}
}