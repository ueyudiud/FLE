/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;

public class TECircuitFrontBack extends TECircuitCompacted
{
	private static final Facing[] OUT = { Facing.FRONT };
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.axis == this.facing.axis;
	}
	
	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side.opposite() == this.facing ? this.power : 0;
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side.opposite() == this.facing ? this.power : 0;
	}
	
	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
}
