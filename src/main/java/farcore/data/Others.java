/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import java.util.function.Function;

import farcore.lib.util.Direction;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author ueyudiud
 *
 */
public class Others
{
	public static final IBlockState AIR = Blocks.AIR.getDefaultState();
	public static final Item ITEM_AIR = Item.getItemFromBlock(Blocks.AIR);
	
	public static final PropertyBool PROP_NORTH = PropertyBool.create("north");
	public static final PropertyBool PROP_EAST = PropertyBool.create("east");
	public static final PropertyBool PROP_SOUTH = PropertyBool.create("south");
	public static final PropertyBool PROP_WEST = PropertyBool.create("west");
	public static final PropertyBool PROP_UP = PropertyBool.create("up");
	public static final PropertyBool PROP_DOWN = PropertyBool.create("down");
	public static final PropertyBool[] PROPS_SIDE = {PROP_DOWN, PROP_UP, PROP_NORTH, PROP_SOUTH, PROP_WEST, PROP_EAST};
	public static final PropertyBool[] PROPS_SIDE_HORIZONTALS = {PROP_SOUTH, PROP_WEST, PROP_NORTH, PROP_EAST};
	public static final PropertyEnum<EnumFacing> PROP_FACING_ALL = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.VALUES);
	public static final PropertyEnum<EnumFacing> PROP_FACING_HORIZONTALS = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	public static final PropertyEnum<Direction> PROP_DIRECTION_ALL = PropertyEnum.create("direction", Direction.class, Direction.DIRECTIONS_3D);
	public static final PropertyEnum<Direction> PROP_DIRECTION_HORIZONTALS = PropertyEnum.create("direction", Direction.class, Direction.DIRECTIONS_2D);
	
	public static final IAttribute PROJECTILE_DAMAGE = (new RangedAttribute((IAttribute)null, "far.projectile.damage", 0.0D, 0, Double.MAX_VALUE)).setShouldWatch(true);
	
	public static final Function TO_NULL = arg -> null;
	
	public static final byte[] BYTES_EMPTY = new byte[0];
	public static final short[] SHORTS_EMPTY = new short[0];
	public static final int[] INTS_EMPTY = new int[0];
	public static final long[] LONGS_EMPTY = new long[0];
	public static final float[] FLOATS_EMPTY = new float[0];
	public static final double[] DOUBLES_EMPTY = new double[0];
}