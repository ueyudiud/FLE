package nebula.common.util;

import static nebula.common.util.Direction.D;
import static nebula.common.util.Direction.E;
import static nebula.common.util.Direction.N;
import static nebula.common.util.Direction.S;
import static nebula.common.util.Direction.U;
import static nebula.common.util.Direction.W;

import net.minecraft.util.EnumFacing;

public enum Facing
{
	DOWN, UP, FRONT, BACK, RIGHT, LEFT, UNKNOWN;
	
	public static final byte			length			= (byte) (values().length - 1);
	public static final Direction[][]	FACING_AXIS		= { { N, S, D, U, W, E }, { S, N, U, D, W, E }, { D, U, N, S, W, E }, { D, U, S, N, E, W }, { D, U, W, E, S, N }, { D, U, E, W, N, S } };
	public static final Facing[][]		T_FACING_AXIS	= { { FRONT, BACK, DOWN, UP, LEFT, RIGHT }, { BACK, FRONT, UP, DOWN, LEFT, RIGHT }, { DOWN, UP, FRONT, BACK, LEFT, RIGHT }, { DOWN, UP, BACK, FRONT, RIGHT, LEFT }, { DOWN, UP, RIGHT, LEFT, FRONT, BACK }, { DOWN, UP, LEFT, RIGHT, BACK, FRONT } };
	public static final Facing[]		OPPISITE		= { UP, DOWN, BACK, FRONT, LEFT, RIGHT, UNKNOWN };
	
	public static Facing toFacing(EnumFacing front, EnumFacing facing)
	{
		return facing == null ? Facing.UNKNOWN : T_FACING_AXIS[front.ordinal()][facing.ordinal()];
	}
	
	public Direction toDirection(Direction direction)
	{
		return toDirection(direction.of());
	}
	
	public Direction toDirection(EnumFacing front)
	{
		return front == null ? U : FACING_AXIS[front.ordinal()][ordinal()];
	}
	
	public Facing getOppisite()
	{
		return OPPISITE[ordinal()];
	}
}
