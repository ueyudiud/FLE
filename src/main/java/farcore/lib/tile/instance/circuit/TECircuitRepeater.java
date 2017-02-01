package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.entity.player.EntityPlayer;

public class TECircuitRepeater extends TECircuitFrontBack
{
	private static final int Actived = 24;

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
				enable(Actived);
			}
			else
			{
				setStrongPower(0);
				setWeakPower(0);
				disable(Actived);
			}
		}
	}

	@Override
	protected void onScrewDriverUsed(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		mode++;
		if(mode == 4)
		{
			mode = 0;
		}
		syncToNearby();
	}
	
	@Override
	protected void updateCircuit()
	{
		if(updateDelay > 0) return;
		int weak = getWeakPower(Facing.BACK);
		int strong = getStrongPower(Facing.BACK);
		if((weak != 0 || strong != 0) != is(Actived))
		{
			markForDelayUpdate((mode + 1) * 2);
		}
	}
	
	@Override
	public String getState()
	{
		return "t" + mode + "_" + (is(Actived) ? "on" : "off");
	}
}