/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.instance;

import java.util.Random;

import javax.annotation.Nullable;

import nebula.common.block.BlockBase;
import nebula.common.block.IBlockStateRegister;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.util.Direction;
import nebula.common.util.Facing;
import nebula.common.util.Properties;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created at 2017年4月4日 下午2:32:45
 * @author ueyudiud
 */
public class BlockRedstoneWireExt extends BlockBase implements IExtendedDataBlock
{
	public static final PropertyEnum<EnumFacing> BASE_SIDE = PropertyEnum.create("base", EnumFacing.class);
	public static final PropertyEnum<ConnectType> R0 = PropertyEnum.create("r0", ConnectType.class);
	public static final PropertyEnum<ConnectType> R90 = PropertyEnum.create("r90", ConnectType.class);
	public static final PropertyEnum<ConnectType> R180 = PropertyEnum.create("r180", ConnectType.class);
	public static final PropertyEnum<ConnectType> R270 = PropertyEnum.create("r270", ConnectType.class);
	public static final IProperty<Integer> POWER = Properties.create("power", 0, 15);
	public static final PropertyEnum<MaterialType> MATERIAL = PropertyEnum.create("material", MaterialType.class);
	
	private static final AxisAlignedBB[] AABBS = {
			new AxisAlignedBB(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F),
			new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F),
			new AxisAlignedBB(0.0F, 0.0F, 0.9375F, 1.0F, 1.0F, 1.0F),
			new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F),
			new AxisAlignedBB(0.9375F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F),
			new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0625F, 1.0F, 1.0F)
	};
	
	public BlockRedstoneWireExt()
	{
		super("red.stone.circuit", Material.CIRCUITS);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(BASE_SIDE, EnumFacing.UP)
				.withProperty(POWER, 0).withProperty(MATERIAL, MaterialType.NONE)
				.withProperty(R0, ConnectType.NONE)
				.withProperty(R90, ConnectType.NONE)
				.withProperty(R180, ConnectType.NONE)
				.withProperty(R270, ConnectType.NONE);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BASE_SIDE, R0, R90, R180, R270, POWER, MATERIAL);
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		return state.getValue(BASE_SIDE).ordinal() | state.getValue(POWER) << 3 | state.getValue(MATERIAL).ordinal() << 8;
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		IBlockState state = getDefaultState();
		state = state.withProperty(BASE_SIDE, EnumFacing.VALUES[meta & 0x8]);
		state = state.withProperty(POWER, (meta >> 3) & 0xF);
		state = state.withProperty(MATERIAL, MaterialType.values()[(meta >> 8) & 0x3F]);
		return state;
	}
	
	@Override
	public void registerStateToRegister(IBlockStateRegister register)
	{
		register.registerStates(this, BASE_SIDE, POWER, MATERIAL);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABBS[state.getValue(BASE_SIDE).ordinal()];
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumFacing base = state.getValue(BASE_SIDE);
		return state
				.withProperty(R0, getConnectionType(state, base, worldIn, pos, Facing.UP))
				.withProperty(R90, getConnectionType(state, base, worldIn, pos, Facing.LEFT))
				.withProperty(R180, getConnectionType(state, base, worldIn, pos, Facing.DOWN))
				.withProperty(R270, getConnectionType(state, base, worldIn, pos, Facing.RIGHT));
	}
	
	private ConnectType getConnectionType(IBlockState state, EnumFacing base, IBlockAccess world, BlockPos pos, Facing facing)
	{
		Direction direction = facing.toDirection(base);
		BlockPos pos2 = direction.offset(pos);
		IBlockState state2;
		EnumFacing recieve = direction.getOpposite().of();
		if (!canConnectTo(state, state2 = world.getBlockState(pos2), world, pos2, recieve))
		{
			if (state2.isSideSolid(world, pos2, direction.getOpposite().of()))
			{
				if (!canConnectTo(state, state2 = world.getBlockState(pos2 = pos2.offset(base, 1)), world, pos2, null))
				{
					return ConnectType.INNER;
				}
			}
			else if (state2.getBlock().isAir(state2, world, pos2))
			{
				if (!canConnectTo(state, state2 = world.getBlockState(pos2 = pos2.offset(base, -1)), world, pos2, null))
				{
					return ConnectType.OUTER;
				}
			}
			return ConnectType.NONE;
		}
		else
		{
			return ConnectType.NORMAL;
		}
	}
	
	protected boolean canConnectTo(IBlockState state, IBlockState state2, IBlockAccess world, BlockPos pos, @Nullable EnumFacing facing)
	{
		if (state2.getBlock() instanceof BlockRedstoneWire)
		{
			return true;
		}
		else if (state2.getBlock() instanceof BlockRedstoneWireExt)
		{
			MaterialType type1 = state.getValue(MATERIAL);
			MaterialType type2 = state.getValue(MATERIAL);
			return type1.tier != type2.tier || (type1.color == WireColor.NONE || type2.color == WireColor.NONE || type1.color == type2.color);
		}
		else if (Blocks.UNPOWERED_REPEATER.isSameDiode(state2))
		{
			EnumFacing facing2 = state2.getValue(BlockRedstoneRepeater.FACING);
			return facing2 == facing || facing2.getOpposite() == facing;
		}
		else
		{
			return state2.getBlock().canConnectRedstone(state2, world, pos, facing);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceBlockOnSide(worldIn, pos, EnumFacing.DOWN);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos = pos.offset(side, -1));
		return state.isSideSolid(worldIn, pos, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		MaterialType type = stateIn.getValue(MATERIAL);
		int i = stateIn.getValue(POWER).intValue();
		
		switch (type)
		{
		case NONE :
		case GLOW :
			if (i != 0)
			{
				double d0 = pos.getX();
				double d1 = pos.getY();
				double d2 = pos.getZ();
				float r, g, b;
				switch (stateIn.getValue(BASE_SIDE))
				{
				case UP :
					d0 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d1 += .0625;
					d2 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					break;
				case DOWN :
					d0 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d1 += .9375;
					d2 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					break;
				case NORTH :
					d0 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d1 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d2 += .0625;
					break;
				case SOUTH :
					d0 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d1 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d2 += .9375;
					break;
				case WEST :
					d0 += .0625;
					d1 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d2 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					break;
				case EAST :
					d0 += .9375;
					d1 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					d2 += .5 + (rand.nextFloat() - 0.5) * 0.2;
					break;
				default:
					break;
				}
				float f = i / 15.0F;
				r = f * 0.6F + 0.4F;
				g = Math.max(0.0F, f * f * 0.7F - 0.5F);
				b = Math.max(0.0F, f * f * 0.6F - 0.7F);
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, r, g, b, new int[0]);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		switch (mirrorIn)
		{
		case FRONT_BACK :
			return state.withProperty(R0, state.getValue(R180)).withProperty(R180, state.getValue(R0));
		case LEFT_RIGHT :
			return state.withProperty(R90, state.getValue(R270)).withProperty(R270, state.getValue(R90));
		default:
			return state;
		}
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch (rot)
		{
		case CLOCKWISE_180:
			return state
					.withProperty(R0, state.getValue(R180))
					.withProperty(R90, state.getValue(R270))
					.withProperty(R180, state.getValue(R0))
					.withProperty(R270, state.getValue(R90));
		case COUNTERCLOCKWISE_90:
			return state
					.withProperty(R0, state.getValue(R270))
					.withProperty(R90, state.getValue(R0))
					.withProperty(R180, state.getValue(R90))
					.withProperty(R270, state.getValue(R180));
		case CLOCKWISE_90:
			return state
					.withProperty(R0, state.getValue(R90))
					.withProperty(R90, state.getValue(R180))
					.withProperty(R180, state.getValue(R270))
					.withProperty(R270, state.getValue(R0));
		default:
			return state;
		}
	}
	
	static enum ConnectType implements IStringSerializable
	{
		NONE("none"),
		NORMAL("normal"),
		OUTER("outer"),
		INNER("inner");
		
		final String name;
		
		ConnectType(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
	}
	
	static enum MaterialType implements IStringSerializable
	{
		NONE("none", WireColor.NONE, 0),
		
		GLOW("glow", WireColor.NONE, 0),
		
		INSULATION("insulation", WireColor.NONE, 1),
		
		INSULATION_WHITE("insulation_white", WireColor.WHITE, 1),
		INSULATION_ORANGE("insulation_orange", WireColor.ORANGE, 1),
		INSULATION_MAGENTA("insulation_magenta", WireColor.MAGENTA, 1),
		INSULATION_LIGHT_BLUE("insulation_light_blue", WireColor.LIGHT_BLUE, 1),
		INSULATION_YELLOW("insulation_yellow", WireColor.YELLOW, 1),
		INSULATION_LIME("insulation_lime", WireColor.LIME, 1),
		INSULATION_PINK("insulation_pink", WireColor.PINK, 1),
		INSULATION_GRAY("insulation_gray", WireColor.GRAY, 1),
		INSULATION_SILVER("insulation_silver", WireColor.SILVER, 1),
		INSULATION_CYAN("insulation_cyan", WireColor.CYAN, 1),
		INSULATION_PURPLE("insulation_purple", WireColor.PURPLE, 1),
		INSULATION_BLUE("insulation_blue", WireColor.BLUE, 1),
		INSULATION_BROWN("insulation_brown", WireColor.BROWN, 1),
		INSULATION_GREEN("insulation_green", WireColor.GREEN, 1),
		INSULATION_RED("insulation_red", WireColor.RED, 1),
		INSULATION_BLACK("insulation_black", WireColor.BLACK, 1),
		
		INSULATION_II("insulation_ii", WireColor.NONE, 2),
		
		INSULATION_II_WHITE("insulation_ii_white", WireColor.WHITE, 2),
		INSULATION_II_ORANGE("insulation_ii_orange", WireColor.ORANGE, 2),
		INSULATION_II_MAGENTA("insulation_ii_magenta", WireColor.MAGENTA, 2),
		INSULATION_II_LIGHT_BLUE("insulation_ii_light_blue", WireColor.LIGHT_BLUE, 2),
		INSULATION_II_YELLOW("insulation_ii_yellow", WireColor.YELLOW, 2),
		INSULATION_II_LIME("insulation_ii_lime", WireColor.LIME, 2),
		INSULATION_II_PINK("insulation_ii_pink", WireColor.PINK, 2),
		INSULATION_II_GRAY("insulation_ii_gray", WireColor.GRAY, 2),
		INSULATION_II_SILVER("insulation_ii_silver", WireColor.SILVER, 2),
		INSULATION_II_CYAN("insulation_ii_cyan", WireColor.CYAN, 2),
		INSULATION_II_PURPLE("insulation_ii_purple", WireColor.PURPLE, 2),
		INSULATION_II_BLUE("insulation_ii_blue", WireColor.BLUE, 2),
		INSULATION_II_BROWN("insulation_ii_brown", WireColor.BROWN, 2),
		INSULATION_II_GREEN("insulation_ii_green", WireColor.GREEN, 2),
		INSULATION_II_RED("insulation_ii_red", WireColor.RED, 2),
		INSULATION_II_BLACK("insulation_ii_black", WireColor.BLACK, 2);
		
		static
		{
			NONE.light = 4;
			GLOW.light = 15;
		}
		
		final String name;
		final WireColor color;
		final int tier;
		int light = 0;
		
		MaterialType(String name, WireColor color, int tier)
		{
			this.name = name;
			this.color = color;
			this.tier = tier;
		}
		
		@Override
		public String getName()
		{
			return this.name;
		}
	}
	
	static enum WireColor
	{
		NONE("none", null),
		WHITE("white", EnumDyeColor.WHITE),
		ORANGE("orange", EnumDyeColor.ORANGE),
		MAGENTA("magenta", EnumDyeColor.MAGENTA),
		LIGHT_BLUE("light_blue", EnumDyeColor.LIGHT_BLUE),
		YELLOW("yellow", EnumDyeColor.YELLOW),
		LIME("lime", EnumDyeColor.LIME),
		PINK("pink", EnumDyeColor.PINK),
		GRAY("gray", EnumDyeColor.GRAY),
		SILVER("silver", EnumDyeColor.SILVER),
		CYAN("cyan", EnumDyeColor.CYAN),
		PURPLE("purple", EnumDyeColor.PURPLE),
		BLUE("blue", EnumDyeColor.BLUE),
		BROWN("brown", EnumDyeColor.BROWN),
		GREEN("green", EnumDyeColor.GREEN),
		RED("red", EnumDyeColor.RED),
		BLACK("black", EnumDyeColor.BLACK);
		
		final String name;
		final EnumDyeColor color;
		
		WireColor(String name, EnumDyeColor color)
		{
			this.name = name;
			this.color = color;
		}
	}
}