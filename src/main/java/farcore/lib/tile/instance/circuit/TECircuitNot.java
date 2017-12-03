/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitNot extends TECircuitFrontBack
{
	private static final int Actived = 0x5;
	
	@Override
	protected void updateCircuit()
	{
		int power = getRedstonePower(Facing.BACK);
		boolean flag = power != 0;
		set(Actived, flag);
		setRedstonePower((this.mode & 0x1) != 0 ? 15 - power : (flag ? 0 : 15));
	}
	
	@Override
	public String getState()
	{
		return optional(Actived, "on", "off");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? getRedstonePower(Facing.BACK) : i == 1 ? this.power : 0;
	}
}
