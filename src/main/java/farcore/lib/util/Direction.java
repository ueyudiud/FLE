package farcore.lib.util;

import net.minecraft.util.EnumFacing;

public enum Direction
{
	D( 0, -1,  0,  0),
	U( 0,  1,  0,  0),
	N( 0,  0, -1,  0),
	S( 0,  0,  1,  0),
	W(-1,  0,  0,  0),
	E( 1,  0,  0,  0),
	A( 0,  0,  0, -1),
	B( 0,  0,  0,  1),
	Q( 0,  0,  0,  0);//Unknown direction.

	public static final Direction[][] machineRotation = {
			{D, U, N, S, W, E, A, B, Q},
			{D, U, S, N, E, W, A, B, Q},
			{D, U, S, N, E, W, A, B, Q},
			{D, U, N, S, W, E, A, B, Q},
			{D, U, W, E, S, N, A, B, Q},
			{D, U, E, W, N, S, A, B, Q},
			{D, U, N, S, W, E, A, B, Q},
			{U, D, S, N, E, W, A, B, Q},
			{D, U, N, S, W, E, A, B, Q}
	};

	public static final int[] oppsite = {1, 0, 3, 2, 5, 4, 7, 6, 8};
	public static final Direction[] directions_2D = {N, S, W, E};
	public static final Direction[] directions = {D, U, N, S, W, E};
	public static final Direction[] directions_adv = {D, U, N, S, W, E, A, B};
	//4D rotation next(B) and last(A) is real number
	public static final Direction[][] rotate_4D = {
			{D, U, W, E, S, N, A, B},
			{D, U, E, W, N, S, A, B},
			{E, W, N, S, D, U, A, B},
			{W, E, S, N, U, D, A, B},
			{S, N, U, D, W, E, A, B},
			{N, S, D, U, W, E, A, B},
			{D, U, S, N, W, E, A, B},
			{U, D, N, S, E, W, A, B}};
	//3D rotation use left hand rule.
	public static final Direction[][] rotate_3D = {
			{D, U, W, E, S, N},
			{D, U, E, W, N, S},
			{E, W, N, S, D, U},
			{W, E, S, N, U, D},
			{S, N, U, D, W, E},
			{N, S, D, U, W, E}};

	private static final int[] cast =
		{0, 1, 2, 3, 4, 5, 6, 6, 6};

	public static Direction of(EnumFacing direction)
	{
		return direction == null ? Q :
			values()[direction.ordinal()];
	}

	public static EnumFacing of(Direction direction)
	{
		return direction == null ? null :
			EnumFacing.VALUES[cast[direction.ordinal()]];
	}

	public final int x;
	public final int y;
	public final int z;
	public final int t;
	public final int boundX;
	public final int boundY;
	public final int boundZ;
	public final int flag;

	Direction(int x, int y, int z, int t)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
		boundX = x != 0 ? -1 : 1;
		boundY = y != 0 ? -1 : 1;
		boundZ = z != 0 ? -1 : 1;
		flag = 1 << ordinal();
	}

	public Direction getOpposite()
	{
		return this == Q ? Q : directions[ordinal() ^ 1];
	}

	public Direction getRotation4D(Direction axis)
	{
		return rotate_4D[axis.ordinal()][ordinal()];
	}

	public Direction getRotation3D(Direction axis)
	{
		return rotate_3D[axis.ordinal()][ordinal()];
	}

	public Direction validDirection3D()
	{
		return this == A || this == B ? Q : this;
	}

	public Direction validDirection2D()
	{
		return this == A || this == B || this == U || this == D ? Q : this;
	}

	public EnumFacing of()
	{
		return of(this);
	}
}