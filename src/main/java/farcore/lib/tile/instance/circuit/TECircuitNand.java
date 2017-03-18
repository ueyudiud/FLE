package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitNand extends TECircuitTripleInput
{
	private static final int Left = 0x5;
	private static final int Back = 0x6;
	private static final int Right = 0x7;
	
	@Override
	protected void updateCircuit()
	{
		boolean flag1 = getRedstonePower(Facing.LEFT) != 0;
		boolean flag2 = getRedstonePower(Facing.RIGHT) != 0;
		boolean flag3 = getRedstonePower(Facing.BACK) != 0;
		set(Left, flag2);
		set(Back, flag3);
		set(Right, flag1);
		setRedstonePower(flag1 && flag2 && flag3 ? 0 : 15);
	}
	
	@Override
	public String getState()
	{
		return optional(Left, "e", "d") + optional(Back, "e", "d") + optional(Right, "e", "d");
	}
}