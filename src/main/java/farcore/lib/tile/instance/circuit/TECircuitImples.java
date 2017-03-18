package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitImples extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Right = 0x6;
	
	@Override
	protected void updateCircuit()
	{
		boolean flag = (this.mode & 0x1) != 0;
		boolean flag1 = getRedstonePower(Facing.LEFT) != 0;
		boolean flag2 = getRedstonePower(Facing.RIGHT) != 0;
		set(Left, flag1);
		set(Right, flag2);
		if(flag)
		{
			flag1 ^= flag2;
			flag2 ^= flag1;
			flag1 ^= flag2;
		}
		setRedstonePower(flag1 || !flag2 ? 15 : 0);
	}
	
	@Override
	public String getState()
	{
		return ((this.mode & 0x1) != 0 ? "r" : "l") + optional(Left, "e", "d") + optional(Right, "e", "d");
	}
}