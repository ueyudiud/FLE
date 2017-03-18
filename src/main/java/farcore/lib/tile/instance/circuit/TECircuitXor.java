package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;

public class TECircuitXor extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Right = 0x6;
	
	@Override
	protected void updateCircuit()
	{
		boolean flag1 = getRedstonePower(Facing.LEFT) != 0;
		boolean flag2 = getRedstonePower(Facing.RIGHT) != 0;
		set(Left, flag1);
		set(Right, flag2);
		setRedstonePower(flag1 ^ flag2 ? 15 : 0);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side != Facing.FRONT.toDirection(this.facing) && super.canConnectRedstone(state, side);
	}
	
	@Override
	public String getState()
	{
		return optional(Left, "e", "d") + optional(Right, "e", "d");
	}
}