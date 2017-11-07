/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nebula.common.block.BlockBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BlockStairV1 extends BlockBase
{
	public static final PropertyDirection					FACING				= BlockHorizontal.FACING;
	public static final PropertyEnum<BlockStairs.EnumHalf>	HALF				= BlockStairs.HALF;
	public static final PropertyEnum<BlockStairs.EnumShape>	SHAPE				= BlockStairs.SHAPE;
	/**
	 * B: .. T: xx B: .. T: xx
	 */
	protected static final AxisAlignedBB					AABB_SLAB_TOP		= new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
	/**
	 * B: .. T: x. B: .. T: x.
	 */
	protected static final AxisAlignedBB					AABB_QTR_TOP_WEST	= new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
	/**
	 * B: .. T: .x B: .. T: .x
	 */
	protected static final AxisAlignedBB					AABB_QTR_TOP_EAST	= new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
	/**
	 * B: .. T: xx B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_QTR_TOP_NORTH	= new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
	/**
	 * B: .. T: .. B: .. T: xx
	 */
	protected static final AxisAlignedBB					AABB_QTR_TOP_SOUTH	= new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
	/**
	 * B: .. T: x. B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_TOP_NW		= new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
	/**
	 * B: .. T: .x B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_TOP_NE		= new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
	/**
	 * B: .. T: .. B: .. T: x.
	 */
	protected static final AxisAlignedBB					AABB_OCT_TOP_SW		= new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
	/**
	 * B: .. T: .. B: .. T: .x
	 */
	protected static final AxisAlignedBB					AABB_OCT_TOP_SE		= new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
	/**
	 * B: xx T: .. B: xx T: ..
	 */
	protected static final AxisAlignedBB					AABB_SLAB_BOTTOM	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	/**
	 * B: x. T: .. B: x. T: ..
	 */
	protected static final AxisAlignedBB					AABB_QTR_BOT_WEST	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
	/**
	 * B: .x T: .. B: .x T: ..
	 */
	protected static final AxisAlignedBB					AABB_QTR_BOT_EAST	= new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	/**
	 * B: xx T: .. B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_QTR_BOT_NORTH	= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
	/**
	 * B: .. T: .. B: xx T: ..
	 */
	protected static final AxisAlignedBB					AABB_QTR_BOT_SOUTH	= new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
	/**
	 * B: x. T: .. B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_BOT_NW		= new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
	/**
	 * B: .x T: .. B: .. T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_BOT_NE		= new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
	/**
	 * B: .. T: .. B: x. T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_BOT_SW		= new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
	/**
	 * B: .. T: .. B: .x T: ..
	 */
	protected static final AxisAlignedBB					AABB_OCT_BOT_SE		= new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
	
	public BlockStairV1(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	
	public BlockStairV1(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		this.useNeighborBrightness = true;
	}
	
	public BlockStairV1(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		this.useNeighborBrightness = true;
	}
	
	public BlockStairV1(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		this.useNeighborBrightness = true;
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName();
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
	{
		state = getActualState(state, worldIn, pos);
		
		for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state))
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
		}
	}
	
	private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate)
	{
		List<AxisAlignedBB> list = Lists.<AxisAlignedBB> newArrayList();
		boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;
		list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
		BlockStairs.EnumShape blockstairs$enumshape = bstate.getValue(SHAPE);
		
		if (blockstairs$enumshape == BlockStairs.EnumShape.STRAIGHT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_LEFT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_RIGHT)
		{
			list.add(getCollQuarterBlock(bstate));
		}
		
		if (blockstairs$enumshape != BlockStairs.EnumShape.STRAIGHT)
		{
			list.add(getCollEighthBlock(bstate));
		}
		
		return list;
	}
	
	/**
	 * Returns a bounding box representing an eighth of a block (a block whose
	 * three dimensions are halved). Used in all stair shapes except STRAIGHT
	 * (gets added alone in the case of OUTER; alone with a quarter block in
	 * case of INSIDE).
	 */
	private static AxisAlignedBB getCollEighthBlock(IBlockState bstate)
	{
		EnumFacing enumfacing = bstate.getValue(FACING);
		EnumFacing enumfacing1;
		
		switch (bstate.getValue(SHAPE))
		{
		case OUTER_LEFT:
		default:
			enumfacing1 = enumfacing;
			break;
		case OUTER_RIGHT:
			enumfacing1 = enumfacing.rotateY();
			break;
		case INNER_RIGHT:
			enumfacing1 = enumfacing.getOpposite();
			break;
		case INNER_LEFT:
			enumfacing1 = enumfacing.rotateYCCW();
		}
		
		boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;
		
		switch (enumfacing1)
		{
		case NORTH:
		default:
			return flag ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
		case SOUTH:
			return flag ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
		case WEST:
			return flag ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
		case EAST:
			return flag ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
		}
	}
	
	/**
	 * Returns a bounding box representing a quarter of a block (two eight-size
	 * cubes back to back). Used in all stair shapes except OUTER.
	 */
	private static AxisAlignedBB getCollQuarterBlock(IBlockState bstate)
	{
		boolean flag = bstate.getValue(HALF) == BlockStairs.EnumHalf.TOP;
		
		switch (bstate.getValue(FACING))
		{
		case NORTH:
		default:
			return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
		case SOUTH:
			return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
		case WEST:
			return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
		case EAST:
			return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
		}
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks
	 * for render
	 */
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
	
	/**
	 * Checks if an IBlockState represents a block that is opaque and a full
	 * cube.
	 */
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return state.getValue(HALF) == BlockStairs.EnumHalf.TOP;
	}
	
	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		IBlockState state = super.getBlockPlaceState(worldIn, pos, facing, hitX, hitY, hitZ, stackIn, placer);
		state = state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
		return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D) ? state.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM) : state.withProperty(HALF, BlockStairs.EnumHalf.TOP);
	}
	
	/**
	 * Ray traces through the blocks collision from start vector to end vector
	 * returning a ray trace hit.
	 */
	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		List<RayTraceResult> list = new ArrayList();
		
		for (AxisAlignedBB axisalignedbb : getCollisionBoxList(getActualState(blockState, worldIn, pos)))
		{
			list.add(rayTrace(pos, start, end, axisalignedbb));
		}
		
		RayTraceResult raytraceresult1 = null;
		double d1 = 0.0D;
		
		for (RayTraceResult raytraceresult : list)
		{
			if (raytraceresult != null)
			{
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);
				
				if (d0 > d1)
				{
					raytraceresult1 = raytraceresult;
					d1 = d0;
				}
			}
		}
		
		return raytraceresult1;
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState().withProperty(HALF, (meta & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
		state = state.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
		return state;
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		
		if (state.getValue(HALF) == BlockStairs.EnumHalf.TOP)
		{
			i |= 4;
		}
		
		i = i | 5 - state.getValue(FACING).getIndex();
		return i;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	/**
	 * Get the actual Block state of this Block at the given position. This
	 * applies properties not visible in the metadata, such as fence
	 * connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state.withProperty(SHAPE, getStairsShape(state, worldIn, pos));
	}
	
	private static EnumShape getStairsShape(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		IBlockState state2 = world.getBlockState(pos.offset(enumfacing));
		
		if ((state2.getBlock() instanceof BlockStairV1) && state.getValue(HALF) == state2.getValue(HALF))
		{
			EnumFacing enumfacing1 = state2.getValue(FACING);
			
			if (enumfacing1.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, world, pos, enumfacing1.getOpposite()))
			{
				if (enumfacing1 == enumfacing.rotateYCCW()) return BlockStairs.EnumShape.OUTER_LEFT;
				
				return BlockStairs.EnumShape.OUTER_RIGHT;
			}
		}
		
		IBlockState state3 = world.getBlockState(pos.offset(enumfacing.getOpposite()));
		
		if ((state3.getBlock() instanceof BlockStairV1) && state.getValue(HALF) == state3.getValue(HALF))
		{
			EnumFacing enumfacing2 = state3.getValue(FACING);
			
			if (enumfacing2.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, world, pos, enumfacing2))
			{
				if (enumfacing2 == enumfacing.rotateYCCW()) return BlockStairs.EnumShape.INNER_LEFT;
				
				return BlockStairs.EnumShape.INNER_RIGHT;
			}
		}
		
		return BlockStairs.EnumShape.STRAIGHT;
	}
	
	private static boolean isDifferentStairs(IBlockState state1, IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState state2 = world.getBlockState(pos.offset(facing));
		return !(state2.getBlock() instanceof BlockStairV1) || state2.getValue(FACING) != state1.getValue(FACING) || state2.getValue(HALF) != state1.getValue(HALF);
	}
	
	/**
	 * Returns the blockstate with the given rotation from the passed
	 * blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	@SuppressWarnings("incomplete-switch")
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		BlockStairs.EnumShape blockstairs$enumshape = state.getValue(SHAPE);
		
		switch (mirrorIn)
		{
		case LEFT_RIGHT:
			
			if (enumfacing.getAxis() == EnumFacing.Axis.Z)
			{
				switch (blockstairs$enumshape)
				{
				case OUTER_LEFT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
				case OUTER_RIGHT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
				case INNER_RIGHT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				case INNER_LEFT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				default:
					return state.withRotation(Rotation.CLOCKWISE_180);
				}
			}
			
			break;
		case FRONT_BACK:
			
			if (enumfacing.getAxis() == EnumFacing.Axis.X)
			{
				switch (blockstairs$enumshape)
				{
				case OUTER_LEFT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
				case OUTER_RIGHT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
				case INNER_RIGHT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
				case INNER_LEFT:
					return state.withRotation(Rotation.CLOCKWISE_180).withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
				case STRAIGHT:
					return state.withRotation(Rotation.CLOCKWISE_180);
				}
			}
		}
		
		return super.withMirror(state, mirrorIn);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, HALF, SHAPE });
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (state.isOpaqueCube()) return true;
		EnumHalf half = state.getValue(HALF);
		EnumFacing side = state.getValue(FACING);
		return side == face || (half == EnumHalf.TOP && face == EnumFacing.UP) || (half == EnumHalf.BOTTOM && face == EnumFacing.DOWN);
	}
}
