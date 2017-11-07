package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;

public class TESensorLight extends TESensor
{
	@Override
	protected int getValue()
	{
		return getLight(Direction.U, false) & 0xF;
	}
}
