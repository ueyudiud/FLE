/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.wood;

import static nebula.common.data.Misc.PROPS_SIDE_HORIZONTALS;
import static net.minecraft.block.BlockFence.EAST_AABB;
import static net.minecraft.block.BlockFence.NORTH_AABB;
import static net.minecraft.block.BlockFence.PILLAR_AABB;
import static net.minecraft.block.BlockFence.SOUTH_AABB;
import static net.minecraft.block.BlockFence.WEST_AABB;

import java.util.List;

import farcore.data.CT;
import farcore.data.Materials;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.prop.PropertyWood;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.stack.BaseStack;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockWoodenFence extends BlockMaterial<PropertyWood>
{
	public static final AxisAlignedBB[] FENCE_BOUNDING_BOXES;
	
	public BlockWoodenFence(PropertyWood property)
	{
		super(property.material.modid, property.material.name, Materials.WOOD, property.material, property);
		setCreativeTab(CT.TREE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), this.material.localName + " Fence");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(getRegistryName().getResourceDomain(), "fence", null, PROPS_SIDE_HORIZONTALS);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this, null);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state
				.withProperty(PROPS_SIDE_HORIZONTALS[0], false)
				.withProperty(PROPS_SIDE_HORIZONTALS[1], false)
				.withProperty(PROPS_SIDE_HORIZONTALS[2], false)
				.withProperty(PROPS_SIDE_HORIZONTALS[3], false);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPS_SIDE_HORIZONTALS);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, PILLAR_AABB);
		
		if (state.getValue(PROPS_SIDE_HORIZONTALS[2]))
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
		}
		
		if (state.getValue(PROPS_SIDE_HORIZONTALS[3]))
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
		}
		
		if (state.getValue(PROPS_SIDE_HORIZONTALS[0]))
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
		}
		
		if (state.getValue(PROPS_SIDE_HORIZONTALS[1]))
		{
			addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return FENCE_BOUNDING_BOXES[getBoundingBoxIdx(state)];
	}
	
	private static final int[] I = {2, 3, 0, 1};
	
	public static int getBoundingBoxIdx(IBlockState state)
	{
		int idx = 0;
		for (int i = 0; i < 4; ++i)
			if (state.getValue(PROPS_SIDE_HORIZONTALS[I[i]]))
				idx |= 1 << I[i];
		return idx;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return false;
	}
	
	public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing)
	{
		IBlockState state2 = worldIn.getBlockState(pos.offset(facing));
		return state2.getBlock() instanceof BlockWoodenFence ||
				state2.isSideSolid(worldIn, pos, facing.getOpposite()) && state2.isOpaqueCube();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		EnumFacing side1 = EnumFacing.VALUES[Worlds.fixSide(side, hitX, 0.5F, hitZ)];
		IProperty<Boolean> property = PROPS_SIDE_HORIZONTALS[side1.getHorizontalIndex()];
		boolean flag = state.getValue(property);
		if (!flag)
		{
			if (canConnectTo(worldIn, pos, side1))
			{
				IBlockState state2 = worldIn.getBlockState(pos.offset(side1));
				if (state2.getBlock() instanceof BlockWoodenFence)
				{
					if (new BaseStack(Items.STICK, 2).contain(heldItem))
					{
						if (!worldIn.isRemote)
						{
							heldItem.stackSize -= 2;
							worldIn.setBlockState(pos, state.withProperty(property, true));
							worldIn.setBlockState(pos.offset(side1), state.withProperty(PROPS_SIDE_HORIZONTALS[side1.getOpposite().getHorizontalIndex()], true));
						}
						return true;
					}
				}
				else if (new BaseStack(Items.STICK, 1).contain(heldItem))
				{
					if (!worldIn.isRemote)
					{
						heldItem.stackSize --;
						worldIn.setBlockState(pos, state.withProperty(property, true));
					}
					return true;
				}
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return getBoundingBoxIdx(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		for (int i = 0; i < 4; ++i)
		{
			if ((meta & (1 << i)) != 0)
			{
				state = state.withProperty(PROPS_SIDE_HORIZONTALS[I[i]], true);
			}
		}
		return state;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		super.neighborChanged(state, worldIn, pos, blockIn);
		IBlockState updated = state;
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			if (state.getValue(PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()]) &&
					!canConnectTo(worldIn, pos, facing))
			{
				updated = updated.withProperty(PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()], false);
				Worlds.spawnDropInWorld(worldIn, pos.offset(facing), new ItemStack(Items.STICK));
			}
		}
		if (updated != state)
		{
			worldIn.setBlockState(pos, updated);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		int i = 0;
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			if (state.getValue(PROPS_SIDE_HORIZONTALS[facing.getHorizontalIndex()]))
			{
				i++;
			}
		}
		if (i > 0)
			Worlds.spawnDropInWorld(worldIn, pos, new ItemStack(Items.STICK, i));
	}
	
	static
	{
		FENCE_BOUNDING_BOXES = new AxisAlignedBB[] {
				new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D),
				new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D  ),
				new AxisAlignedBB(0.0D  , 0.0D, 0.375D, 0.625D, 1.0D, 0.625D),
				new AxisAlignedBB(0.0D  , 0.0D, 0.375D, 0.625D, 1.0D, 1.0D  ),
				new AxisAlignedBB(0.375D, 0.0D, 0.0D  , 0.625D, 1.0D, 0.625D),
				new AxisAlignedBB(0.375D, 0.0D, 0.0D  , 0.625D, 1.0D, 1.0D  ),
				new AxisAlignedBB(0.0D  , 0.0D, 0.0D  , 0.625D, 1.0D, 0.625D),
				new AxisAlignedBB(0.0D  , 0.0D, 0.0D  , 0.625D, 1.0D, 1.0D  ),
				new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D  , 1.0D, 0.625D),
				new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D  , 1.0D, 1.0D  ),
				new AxisAlignedBB(0.0D  , 0.0D, 0.375D, 1.0D  , 1.0D, 0.625D),
				new AxisAlignedBB(0.0D  , 0.0D, 0.375D, 1.0D  , 1.0D, 1.0D  ),
				new AxisAlignedBB(0.375D, 0.0D, 0.0D  , 1.0D  , 1.0D, 0.625D),
				new AxisAlignedBB(0.375D, 0.0D, 0.0D  , 1.0D  , 1.0D, 1.0D  ),
				new AxisAlignedBB(0.0D  , 0.0D, 0.0D  , 1.0D  , 1.0D, 0.625D),
				new AxisAlignedBB(0.0D  , 0.0D, 0.0D  , 1.0D  , 1.0D, 1.0D  )};
	}
}