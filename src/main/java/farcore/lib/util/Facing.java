package farcore.lib.util;

import static farcore.lib.util.Direction.D;
import static farcore.lib.util.Direction.E;
import static farcore.lib.util.Direction.N;
import static farcore.lib.util.Direction.S;
import static farcore.lib.util.Direction.U;
import static farcore.lib.util.Direction.W;

import net.minecraft.util.EnumFacing;

public enum Facing
{
	DOWN,
	UP,
	FRONT,
	BACK,
	LEFT,
	RIGHT,
	UNKNOWN;

	public static final byte length = (byte) (values().length - 1);
	public static final Direction[][] FACING_AXIS = {
			{N, S, D, U, W, E},
			{S, N, U, D, W, E},
			{D, U, N, S, W, E},
			{D, U, S, N, E, W},
			{D, U, W, E, N, S},
			{D, U, E, W, S, N}};
	public static final Facing[][] T_FACING_AXIS = {
			{FRONT, BACK, DOWN, UP, LEFT, RIGHT},
			{BACK, FRONT, UP, DOWN, LEFT, RIGHT},
			{DOWN, UP, FRONT, BACK, LEFT, RIGHT},
			{DOWN, UP, BACK, FRONT, RIGHT, LEFT},
			{DOWN, UP, LEFT, RIGHT, FRONT, BACK},
			{DOWN, UP, RIGHT, LEFT, BACK, FRONT}};
	public static final Facing[] OPPISITE = {UP, DOWN, BACK, Facing.FRONT, RIGHT, LEFT, UNKNOWN};

	public static Facing toFacing(EnumFacing front, EnumFacing facing)
	{
		return facing == null ? Facing.UNKNOWN : T_FACING_AXIS[front.ordinal()][facing.ordinal()];
	}

	public Direction toDirection(Direction direction) { return toDirection(direction.of());}
	public Direction toDirection(EnumFacing front)
	{
		return front == null ? U : FACING_AXIS[front.ordinal()][ordinal()];
	}

	public Facing getOppisite()
	{
		return OPPISITE[ordinal()];
	}
}