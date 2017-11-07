/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import java.util.function.Function;

import javax.annotation.Nullable;

import nebula.common.util.Properties;
import nebula.common.util.Properties.EnumStateName;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * The slab state, used for slab.
 * 
 * @author ueyudiud
 */
@EnumStateName("facing")
public enum EnumSlabState implements IStringSerializable
{
	DOWN(
			"down",
			EnumFacing.DOWN),
	UP("up", EnumFacing.UP),
	SOUTH("south", EnumFacing.SOUTH),
	NORTH("north", EnumFacing.NORTH),
	WEST("west", EnumFacing.WEST),
	EAST("east", EnumFacing.EAST),
	DOUBLE_UD("d_ud", EnumFacing.Axis.Y),
	DOUBLE_NS("d_ns", EnumFacing.Axis.Z),
	DOUBLE_WE("d_we", EnumFacing.Axis.X);
	
	/**
	 * The general property of slab state.
	 */
	public static final PropertyEnum<EnumSlabState>	PROPERTY		= Properties.get(EnumSlabState.class);
	/**
	 * The rotation for slab state.
	 */
	public static final EnumSlabState[][]			rotationState	= { { DOWN, UP, WEST, EAST, NORTH, SOUTH, DOUBLE_UD, DOUBLE_WE, DOUBLE_NS }, { DOWN, UP, EAST, WEST, SOUTH, NORTH, DOUBLE_UD, DOUBLE_WE, DOUBLE_NS }, { EAST, WEST, SOUTH, NORTH, DOWN, UP, DOUBLE_WE, DOUBLE_NS, DOUBLE_UD }, { WEST, EAST, SOUTH, NORTH, UP, DOWN, DOUBLE_WE, DOUBLE_NS, DOUBLE_UD }, { SOUTH, NORTH, UP, DOWN, WEST, EAST, DOUBLE_NS, DOUBLE_UD, DOUBLE_WE }, { NORTH, SOUTH, DOWN, UP, WEST, EAST, DOUBLE_NS, DOUBLE_UD, DOUBLE_WE } };
	
	public static final Function<EnumFacing, EnumSlabState> FROM_FACING = facing -> values()[facing.ordinal()];
	
	private final String				name;
	/**
	 * For drop item multiple, double slab will drop two slab.
	 */
	public final int					dropMul;
	/**
	 * Is this block state fully cube.
	 */
	public final boolean				fullCube;
	public final @Nullable EnumFacing	face;
	public final EnumFacing.Axis		axis;
	
	EnumSlabState(String name, EnumFacing.Axis axis)
	{
		this(name, 2, true, null, axis);
	}
	
	EnumSlabState(String name, EnumFacing facing)
	{
		this(name, 1, false, facing, facing.getAxis());
	}
	
	EnumSlabState(String name, int dropMul, boolean fullCube, EnumFacing facing, EnumFacing.Axis axis)
	{
		this.name = name;
		this.dropMul = dropMul;
		this.fullCube = fullCube;
		this.face = facing;
		this.axis = axis;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}
