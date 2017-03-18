package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;

public class TECircuitTripleInput extends TECircuitCompacted
{
	private static final Facing[] OUT = {Facing.FRONT};
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.horizontal;
	}
	
	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side.getOpposite() == this.facing ? this.power : 0;
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side.getOpposite() == this.facing ? this.power : 0;
	}
	
	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
}