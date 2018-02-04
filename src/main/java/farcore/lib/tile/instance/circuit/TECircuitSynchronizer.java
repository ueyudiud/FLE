/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitSynchronizer extends TECircuitTripleInput
{
	private static final int	right_input	= 0x8;
	private static final int	left_input	= 0x9;
	private static final int	reseted		= 0xA;
	
	@Override
	protected void updateBody()
	{
		super.updateBody();
		if (this.updateDelay == 0)
		{
			setRedstonePower(0);
		}
	}
	
	@Override
	protected void updateCircuit()
	{
		boolean flag3 = getRedstonePower(Facing.BACK) != 0;
		if (flag3)
		{
			if (!is(reseted))
			{
				enable(reseted);
				disable(right_input);
				disable(left_input);
			}
		}
		else
		{
			disable(reseted);
		}
		boolean flag1 = getRedstonePower(Facing.LEFT) != 0;
		boolean flag2 = getRedstonePower(Facing.RIGHT) != 0;
		if (flag1)
		{
			enable(left_input);
		}
		if (flag2)
		{
			enable(right_input);
		}
		if (is(left_input) && is(right_input))
		{
			setRedstonePower(15);
			markForDelayUpdate(4);
		}
	}
	
	@Override
	public String getState()
	{
		return this.power != 0 ? "on" : "off";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? getRedstonePower(Facing.BACK) : i == 1 ? getRedstonePower(Facing.LEFT) : i == 2 ? getRedstonePower(Facing.RIGHT) : i == 3 ? this.power : 0;
	}
}
