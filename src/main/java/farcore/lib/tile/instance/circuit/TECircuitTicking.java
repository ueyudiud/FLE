/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitTicking extends TECircuitFrontBack
{
	private static final int	Enabled	= 0x5;
	private static final int	Actived	= 0x6;
	
	@Override
	protected void updateBody()
	{
		super.updateBody();
		if (is(Enabled))
		{
			if (this.updateDelay == 0)
			{
				if (this.power != 0)
				{
					setRedstonePower(0);
					disable(Actived);
				}
				else
				{
					setRedstonePower(15);
					enable(Actived);
				}
				markForDelayUpdate(4 * (1 + (this.mode & 0xF)));
			}
		}
		else
		{
			setRedstonePower(0);
		}
	}
	
	@Override
	protected void updateCircuit()
	{
		boolean flag = getRedstonePower(Facing.BACK) != 0;
		if (flag)
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
		this.mode++;
		if (this.mode == 16)
		{
			this.mode = 0;
		}
		syncToNearby();
	}
	
	@Override
	public String getState()
	{
		return (is(Enabled) ? "on_" + (is(Actived) ? "a" : "b") : "off");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? is(Enabled) ? getRedstonePower(Facing.BACK) : 0 : i == 1 ? this.power : 0;
	}
}
