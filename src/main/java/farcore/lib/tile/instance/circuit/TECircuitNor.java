/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitNor extends TECircuitTripleInput
{
	private static final int	Left	= 0x5;
	private static final int	Back	= 0x6;
	private static final int	Right	= 0x7;
	
	@Override
	protected void updateCircuit()
	{
		boolean flag = getRedstonePower(Facing.BACK) != 0;
		flag |= getRedstonePower(Facing.LEFT) != 0;
		flag |= getRedstonePower(Facing.RIGHT) != 0;
		setRedstonePower(flag ? 0 : 15);
	}
	
	@Override
	public String getState()
	{
		return optional(Left, "e", "d") + optional(Back, "e", "d") + optional(Right, "e", "d");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? getRedstonePower(Facing.BACK) : i == 1 ? getRedstonePower(Facing.LEFT) : i == 2 ? getRedstonePower(Facing.RIGHT) : i == 3 ? this.power : 0;
	}
}
