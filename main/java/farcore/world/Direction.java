package farcore.world;

import static farcore.world.Facing.*;

import java.util.Arrays;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * The forge removed ForgeDirection in 1.8.<br>
 * The class is preparing for forge update.
 * Use class Direction to instead Minecraft direction type.
 * @author ueyudiud
 * @see net.minecraft.util.EnumFacing
 */
public enum Direction
{
    /** -Y */
    DOWN(0, -1, 0, ForgeDirection.DOWN),
    /** +Y */
    UP(0, 1, 0, ForgeDirection.UP),
    /** -Z */
    NORTH(0, 0, -1, ForgeDirection.NORTH),
    /** +Z */
    SOUTH(0, 0, 1, ForgeDirection.SOUTH),
    /** -X */
    WEST(-1, 0, 0, ForgeDirection.WEST),
    /** +X */
    EAST(1, 0, 0, ForgeDirection.EAST),
    /** -T */
    LAST(0, 0, 0, -1),
    /** +T */
    NEXT(0, 0, 0, +1),
    /**  0 */
    UNKNOWN(0, 0, 0, ForgeDirection.UNKNOWN);
    public static final Direction[] BASIC = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
    public static final Direction[] ADVANCED = {DOWN, UP, NORTH, SOUTH, WEST, EAST, LAST, NEXT};
    public static final int[] OPPOSITES = {1, 0, 3, 2, 5, 4, 7, 6, 8};
    public static final Direction[][] NEAR_BY = 
    	{
    	{NORTH, WEST, SOUTH, EAST}, 
    	{NORTH, EAST, SOUTH, WEST},
    	{UP, EAST, DOWN, WEST}, 
    	{UP, WEST, DOWN, EAST}, 
    	{UP, NORTH, DOWN, SOUTH}, 
    	{UP, SOUTH, DOWN, NORTH},
    	{UNKNOWN},
    	{UNKNOWN},
    	ADVANCED
    	};
    // Left hand rule rotation matrix(without 4-dim) for all possible axes of rotation
    public static final int[][] ROTATION_MATRIX =
    	{
        {0, 1, 4, 5, 3, 2, 6, 7, 8},
        {0, 1, 5, 4, 2, 3, 6, 7, 8},
    	{5, 4, 2, 3, 0, 1, 6, 7, 8},
    	{4, 5, 2, 3, 1, 0, 6, 7, 8},
    	{2, 3, 1, 0, 4, 5, 6, 7, 8},
    	{3, 2, 0, 1, 4, 5, 6, 7, 8},
    	{0, 1, 2, 3, 4, 5, 6, 7, 8},
    	{0, 1, 2, 3, 4, 5, 6, 7, 8},
    	{0, 1, 2, 3, 4, 5, 6, 7, 8}
    	};
    public static final Facing[][] MACHINE_FACING =
    	{
    	{BOTTOM, TOP, BACK, FRONT, SIDE, SIDE, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, BACK, FRONT, SIDE, SIDE, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, FRONT, SIDE, SIDE, SIDE, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, BACK, FRONT, SIDE, SIDE, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, SIDE, SIDE, FRONT, BACK, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, SIDE, SIDE, BACK, FRONT, SIDE, SIDE, SIDE},
    	{BOTTOM, TOP, SIDE, SIDE, SIDE, SIDE, FRONT, BACK, SIDE},
    	{BOTTOM, TOP, BACK, SIDE, SIDE, SIDE, BACK, FRONT, SIDE},
    	{BOTTOM, TOP, SIDE, SIDE, SIDE, SIDE, SIDE, SIDE,  SIDE}
    	};

    public static final Direction toDirection(ForgeDirection direction)
    {
    	switch(direction)
    	{
		case DOWN:
			return DOWN;
		case UP:
			return UP;
		case NORTH:
			return NORTH;
		case WEST:
			return WEST;
		case SOUTH:
			return SOUTH;
		case EAST:
			return EAST;
		case UNKNOWN:
			return UNKNOWN;
		default:
			return null;
    	}
    }
    public static final Direction toDirection(EnumFacing facing)
    {
    	return values()[facing.ordinal()];
    }
    public static final ForgeDirection[] getAccessFacing(Direction...directions)
    {
    	if(directions == null || directions.length == 0) return new ForgeDirection[0];
    	ForgeDirection[] ret = new ForgeDirection[directions.length];
    	try
    	{
    		for(int i = 0; i < ret.length; ret[i] = directions[i].toDir(), ++i);
    	}
    	catch(NullPointerException e)
    	{
    		throw new RuntimeException("Directions can not be null! Dirs : " + directions.toString(), e);
    	}
    	return ret;
    }
    
    public final int x;
    public final int y;
    public final int z;
    public final int d;
    public final int flag;
    public final ForgeDirection forgeDirection;

    Direction(int xO, int yO, int zO, ForgeDirection direction)
    {
    	this(xO, yO, zO, 0, direction);
    }
    Direction(int xO, int yO, int zO, int dO)
    {
    	this(xO, yO, zO, dO, ForgeDirection.UNKNOWN); 
    }
    Direction(int xO, int yO, int zO, int dO, ForgeDirection direction)
    {
    	x = xO;
    	y = yO;
    	z = zO;
    	d = dO;
    	flag = 1 << ordinal();
    	this.forgeDirection = direction;
    }
    
    public Direction getOpposite()
    {
    	return values()[OPPOSITES[ordinal()]];
    }
    
    public Direction getRotation(int axis)
    {
    	return values()[ROTATION_MATRIX[axis][ordinal()]];
    }
    
    public Direction getRotation(Direction axis)
    {
    	return getRotation(axis.ordinal());
    }
    
    public Direction[] getNearBy()
    {
    	return NEAR_BY[ordinal()];
    }
    
    public Facing getMachineFacing(int axis)
    {
    	return MACHINE_FACING[axis][ordinal()];
    }
    
    public Facing getMachineFacing(Direction axis)
    {
    	return getMachineFacing(axis.ordinal());
    }
    
    public BlockPos pos()
    {
    	return new BlockPos(null, x, y, z);
    }
    
    public BlockPos pos(BlockPos pos)
    {
    	return pos == null ? pos() : pos.offset(x, y, z);
    }
    
    public ForgeDirection toDir()
    {
    	return forgeDirection;
    }
    
	public boolean isAdvanced()
	{
		return ordinal() > 5;
	}
}