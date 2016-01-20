package farcore.util;

import static farcore.util.Facing.BACK;
import static farcore.util.Facing.BOTTOM;
import static farcore.util.Facing.FRONT;
import static farcore.util.Facing.SIDE;
import static farcore.util.Facing.TOP;

import java.util.Arrays;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import farcore.block.properties.FlePropertyDirection;

/**
 * The forge removed ForgeDirection in 1.7.10.
 * Use class Direction to instead Minecraft direction type.
 * @author ueyudiud
 * @see net.minecraft.util.EnumFacing
 */
public enum Direction
{
    /** -Y */
    DOWN(0, -1, 0),
    /** +Y */
    UP(0, 1, 0),
    /** -Z */
    NORTH(0, 0, -1),
    /** +Z */
    SOUTH(0, 0, 1),
    /** -X */
    WEST(-1, 0, 0),
    /** +X */
    EAST(1, 0, 0),
    /** -T */
    LAST(0, 0, 0, -1),
    /** +T */
    NEXT(0, 0, 0, +1),
    /**  0 */
    UNKNOWN(0, 0, 0);
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
    	BASIC
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
    public static final FlePropertyDirection FACING 		= new FlePropertyDirection("fle.facingI"	, BASIC);
    public static final FlePropertyDirection FACING_MACHINE = new FlePropertyDirection("fle.facingII"	, new Direction[]{NORTH, SOUTH, WEST, EAST});
    public static final FlePropertyDirection FACING_LOG 	= new FlePropertyDirection("fle.facingIII"	, new Direction[]{UP, SOUTH, EAST});
    private static class PropertyDirection extends PropertyEnum
    {
		public PropertyDirection(String name, Direction...directions)
		{
			super(name, Direction.class, Arrays.asList(directions));
		}    	
    }
    public static final Direction toDirection(EnumFacing facing)
    {
    	return values()[facing.ordinal()];
    }
    public static final EnumFacing[] getAccessFacing(Direction...directions)
    {
    	if(directions == null || directions.length == 0) return new EnumFacing[0];
    	EnumFacing[] ret = new EnumFacing[directions.length];
    	try
    	{
    		for(int i = 0; i < ret.length; ret[i] = directions[i].toFacing(), ++i);
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

    Direction(int xO, int yO, int zO)
    {
    	this(xO, yO, zO, 0);
    }
    Direction(int xO, int yO, int zO, int dO)
    {
    	x = xO;
    	y = yO;
    	z = zO;
    	d = dO;
    	flag = 1 << ordinal();
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
    
    public BlockPos toPos()
    {
    	return new BlockPos(x, y, z);
    }
    
    public BlockPos toPos(BlockPos pos)
    {
    	return pos == null ? toPos() : pos.add(x, y, z);
    }
    
    public EnumFacing toFacing()
    {
    	return ordinal() >= EnumFacing.values().length ? EnumFacing.NORTH : EnumFacing.VALUES[ordinal()];
    }
    
	public boolean isAdvanced()
	{
		return ordinal() > 5;
	}
}