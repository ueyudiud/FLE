/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitRepeater extends TECircuitFrontBack
{
	private static final int Actived = 24;
	
	@Override
	protected void updateBody()
	{
		super.updateBody();
		if (this.updateDelay == 0)
		{
			int power = getRedstonePower(Facing.BACK);
			if (power != 0)
			{
				setRedstonePower(15);
				enable(Actived);
			}
			else
			{
				setRedstonePower(0);
				disable(Actived);
			}
			this.updateDelay = -1;
		}
	}
	
	@Override
	protected void onScrewDriverUsed(EntityPlayer player, Direction side, float hitX, float hitY, float hitZ)
	{
		this.mode++;
		if (this.mode == 4)
		{
			this.mode = 0;
		}
		syncToNearby();
	}
	
	@Override
	protected void updateCircuit()
	{
		if (this.updateDelay > 0) return;
		int power = getRedstonePower(Facing.BACK);
		if ((power != 0) ^ is(Actived))
		{
			markForDelayUpdate((this.mode + 1) * 2);
		}
	}
	
	@Override
	public String getState()
	{
		return "t" + this.mode + "_" + (is(Actived) ? "on" : "off");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return this.power;
	}
}
