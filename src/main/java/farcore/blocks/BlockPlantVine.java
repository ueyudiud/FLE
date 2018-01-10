/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.FarCoreRegistry;
import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.Materials;
import farcore.data.V;
import farcore.lib.material.Mat;
import nebula.base.function.Applicable;
import nebula.client.model.StateMapperExt;
import nebula.common.block.BlockBase;
import nebula.common.data.Misc;
import nebula.common.item.ItemSubBehavior;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockPlantVine extends BlockBase
{
	public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D), EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D),
			SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D), AABBS[] = { SOUTH_AABB, WEST_AABB, EAST_AABB, NORTH_AABB };
	
	private static final Applicable<ItemStack> DROP = () -> EnumItem.crop_related.available() ? ((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("vine") : null;
	
	public static void addVineBlock(World world, BlockPos pos, EnumFacing facing)
	{
		assert facing.getHorizontalIndex() >= 0;
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == EnumBlock.vine.block && !state.getValue(Misc.PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()]))
		{
			world.setBlockState(pos, state.withProperty(Misc.PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()], true), V.generateState ? 2 : 3);
		}
		else if (state.getBlock().isAir(state, world, pos))
		{
			world.setBlockState(pos, EnumBlock.vine.apply().withProperty(Misc.PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()], true), V.generateState ? 2 : 3);
		}
	}
	
	private Mat				material;
	private @Nullable Block	baseBlock;
	private byte			growDir	= -1;// -1 or 1
	
	public BlockPlantVine(Mat material)
	{
		super(material.modid, "plant.vine." + material.name, Materials.VINE);
		this.material = material;
		setTickRandomly(true);
	}
	
	public BlockPlantVine setBaseBlock(Block baseBlock)
	{
		this.baseBlock = baseBlock;
		return this;
	}
	
	public BlockPlantVine setGrowDirection(boolean isDown)
	{
		this.growDir = (byte) (isDown ? -1 : 1);
		return this;
	}
	
	public byte getGrowDirection()
	{
		return this.growDir;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, Misc.PROPS_SIDE_HORIZONTALS);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(Misc.PROPS_SIDE_HORIZONTALS[0], false).withProperty(Misc.PROPS_SIDE_HORIZONTALS[1], false).withProperty(Misc.PROPS_SIDE_HORIZONTALS[2], false).withProperty(Misc.PROPS_SIDE_HORIZONTALS[3], false);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		FarCoreRegistry.registerItemModel(this.item, new ResourceLocation(this.material.modid, "vine/" + this.material.name));
		ModelLoader.setCustomStateMapper(this, new StateMapperExt(this.material.modid, "vine/" + this.material.name, null));
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int meta = getMetaFromState(state);
		// Check is meta is power of 2. If it is, it means this vine only has
		// one direction grown.
		if (meta != 0 && (meta & meta - 1) == 0)
			// Use (x%5)-1 to split 1, 2, 4, 8 to 0, 1, 3, 2 and use this as
			// index in array.
			return AABBS[(meta % 5) - 1];
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return facing.getHorizontalIndex() < 0 ? getDefaultState() : getDefaultState().withProperty(Misc.PROPS_SIDE_HORIZONTALS[facing.getOpposite().getHorizontalIndex()], true);
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
	 * Check whether this Block can be placed on the given side
	 */
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return side.getHorizontalIndex() != -1 && Worlds.isSideSolid(worldIn, pos.offset(side, -1), side, false);
	}
	
	private boolean canAttachOn(World world, BlockPos pos, @Nullable IBlockState state, EnumFacing facing)
	{
		return Worlds.isSideSolid(world, pos.offset(facing), facing.getOpposite(), false) || (this.growDir < 0 && this.baseBlock != null ? (Worlds.isBlock(world, pos.up().offset(facing), this.baseBlock, -1, false)) : (Worlds.isBlock(world, pos.down(), this.baseBlock, -1, false)));
	}
	
	private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state)
	{
		IBlockState old = state;
		
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			PropertyBool property = Misc.PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()];
			
			if (state.getValue(property) && !canAttachOn(worldIn, pos, state, facing))
			{
				IBlockState state1 = worldIn.getBlockState(pos.down(this.growDir));
				
				if (state1.getBlock() != this || !state1.getValue(property))
				{
					state = state.withProperty(property, false);
				}
			}
		}
		
		if (getMetaFromState(state) == 0)
		{
			return false;
		}
		else
		{
			if (old != state) worldIn.setBlockState(pos, state, 2);
			return true;
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if (!worldIn.isRemote && !recheckGrownSides(worldIn, pos, state))
		{
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}
	
	protected int getVineCount(IBlockState state)
	{
		int i = 0;
		for (IProperty<Boolean> property : Misc.PROPS_SIDE_HORIZONTALS)
			if (state.getValue(property)) ++i;
		return i;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		if (!worldIn.isRemote)
		{
			if (random.nextInt(4) == 0)
			{
				int j = 20;
				label:
				{
					for (int x1 = -4; x1 <= 4; ++x1)
					{
						for (int z1 = -4; z1 <= 4; ++z1)
						{
							for (int y1 = -1; y1 <= 1; ++y1)
							{
								IBlockState state2 = worldIn.getBlockState(pos.add(x1, y1, z1));
								if (state2.getBlock() instanceof BlockPlantVine)
								{
									j -= ((BlockPlantVine) state2.getBlock()).getVineCount(state2);
									
									if (j <= 5) break label;
								}
							}
						}
					}
					
					Direction dir = L.random(Direction.DIRECTIONS_2D, random);
					BlockPos pos2;
					IBlockState state2 = worldIn.getBlockState(pos2 = pos.up(this.growDir));
					if (state.getValue(Misc.PROPS_SIDE_HORIZONTALS[dir.horizontalOrdinal]) && canAttachOn(worldIn, pos2, state2, dir.of()))
					{
						if (state2.getBlock().isAir(state2, worldIn, pos2))
						{
							if (random.nextInt(j) >= 5)
							{
								worldIn.setBlockState(pos2, getDefaultState().withProperty(Misc.PROPS_SIDE_HORIZONTALS[dir.horizontalOrdinal], true));
							}
						}
						else if (state2.getBlock() == this && !state2.getValue(Misc.PROPS_SIDE_HORIZONTALS[dir.horizontalOrdinal]))
						{
							if (random.nextInt(j) >= 5 + getVineCount(state2))
							{
								worldIn.setBlockState(pos2, state2.withProperty(Misc.PROPS_SIDE_HORIZONTALS[dir.horizontalOrdinal], true), 2);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		for (Direction direction : Direction.DIRECTIONS_2D)
		{
			if ((meta & direction.flag1) != 0) state = state.withProperty(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal], true);
		}
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;
		for (Direction direction : Direction.DIRECTIONS_2D)
		{
			if (state.getValue(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal])) meta |= direction.flag1;
		}
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(DROP.apply());
		return list;
	}
	
	/**
	 * Returns the blockstate with the given rotation from the passed
	 * blockstate. If inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		int meta;
		switch (rot)
		{
		case CLOCKWISE_180:
			return getStateFromMeta((meta = getMetaFromState(state)) >> 2 | (meta << 2 & 0xC));
		case COUNTERCLOCKWISE_90:
			return getStateFromMeta((meta = getMetaFromState(state)) >> 3 | (meta << 1 & 0xE));
		case CLOCKWISE_90:
			return getStateFromMeta((meta = getMetaFromState(state)) >> 1 | (meta << 3 & 0x8));
		default:
			return state;
		}
	}
	
	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		switch (mirrorIn)
		{
		case LEFT_RIGHT:
			return state.withProperty(Misc.PROP_NORTH, state.getValue(Misc.PROP_SOUTH)).withProperty(Misc.PROP_SOUTH, state.getValue(Misc.PROP_NORTH));
		case FRONT_BACK:
			return state.withProperty(Misc.PROP_EAST, state.getValue(Misc.PROP_WEST)).withProperty(Misc.PROP_WEST, state.getValue(Misc.PROP_EAST));
		default:
			return state;
		}
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	{
		return Config.useVineAsLadder;
	}
}
