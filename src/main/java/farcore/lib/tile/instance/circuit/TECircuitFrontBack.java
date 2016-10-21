package farcore.lib.tile.instance.circuit;

import farcore.lib.util.Direction;
import farcore.lib.util.Facing;
import net.minecraft.block.state.IBlockState;

public class TECircuitFrontBack extends TECircuitCompacted
{
	private static final Facing[] OUT = {Facing.FRONT};
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side == facing || side == facing.getOpposite();
	}

	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side.getOpposite() == facing ? weakPower : 0;
	}

	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side.getOpposite() == facing ? strongPower : 0;
	}

	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
}