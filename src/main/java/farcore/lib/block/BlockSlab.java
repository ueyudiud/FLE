package farcore.lib.block;

import java.util.Random;

import farcore.data.EnumSlabState;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.block.BlockBase;
import nebula.common.util.Worlds;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockSlab extends BlockBase
{
	private static final AxisAlignedBB AABB_DOWN  = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5D, 1D);
	private static final AxisAlignedBB AABB_UP    = new AxisAlignedBB(0D, 0.5D, 0D, 1D, 1D, 1D);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.5D);
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0D, 0D, 0.5D, 1D, 1D, 1D);
	private static final AxisAlignedBB AABB_WEST  = new AxisAlignedBB(0D, 0D, 0D, 0.5D, 1D, 1D);
	private static final AxisAlignedBB AABB_EAST  = new AxisAlignedBB(0.5D, 0D, 0D, 1D, 1D, 1D);
	
	public BlockSlab(String name, Material materialIn)
	{
		super(name, materialIn);
		this.lightOpacity = -1;
		this.useNeighborBrightness = true;
	}
	public BlockSlab(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		this.lightOpacity = -1;
		this.useNeighborBrightness = true;
	}
	public BlockSlab(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		this.lightOpacity = -1;
		this.useNeighborBrightness = true;
	}
	public BlockSlab(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		this.lightOpacity = -1;
		this.useNeighborBrightness = true;
	}
	
	protected abstract String getLocalName();
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		String localName = getLocalName();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(6), "Double " + localName + " Slab");
	}
	
	@Override
	protected Item createItemBlock()
	{
		return new ItemBlockSlab(this);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumSlabState.PROPERTY);
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		//The slab needn't provider much more translation for each block state.
		return getUnlocalizedName() + "@" + (EnumSlabState.values()[metadata].fullCube ? "d" : "s");
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(EnumSlabState.PROPERTY, EnumSlabState.values()[meta]);
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(EnumSlabState.PROPERTY,
				placer.isSneaking() ? (hitY > .5F ? EnumSlabState.UP : EnumSlabState.DOWN) :
					EnumSlabState.values()[facing.getOpposite().ordinal()]);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).fullCube;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos);
		return state.isOpaqueCube() || state.getValue(EnumSlabState.PROPERTY).ordinal() == side.ordinal();
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return state.isOpaqueCube() || state.getValue(EnumSlabState.PROPERTY).ordinal() == face.ordinal();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side) ||
				blockState.getValue(EnumSlabState.PROPERTY).ordinal() == side.ordinal();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return getBoundingBox(state, worldIn, pos);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return getBoundingBox(state, worldIn, pos);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		switch (state.getValue(EnumSlabState.PROPERTY))
		{
		case DOWN  : return AABB_DOWN;
		case UP    : return AABB_UP;
		case SOUTH : return AABB_NORTH;
		case NORTH : return AABB_SOUTH;
		case WEST  : return AABB_WEST;
		case EAST  : return AABB_EAST;
		default : return FULL_BLOCK_AABB;
		}
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).fullCube;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).fullCube;
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return this.lightOpacity != -1 ? this.lightOpacity : 0;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return this.lightOpacity != -1 ? (isOpaqueCube(state) ? this.lightOpacity : Math.min(this.lightOpacity, 3)) :
			(isOpaqueCube(state) ? 255 : 3);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		EnumSlabState state = base_state.getValue(EnumSlabState.PROPERTY);
		return state.fullCube || state.ordinal() == side.ordinal();
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return state.getValue(EnumSlabState.PROPERTY).dropMul;
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(this, quantityDropped(state, 0, RANDOM), damageDropped(state));
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		EnumSlabState state = world.getBlockState(pos).getValue(EnumSlabState.PROPERTY);
		if(state != EnumSlabState.rotationState[axis.ordinal()][state.ordinal()])
		{
			Worlds.switchProp(world, pos, EnumSlabState.PROPERTY, EnumSlabState.rotationState[axis.ordinal()][state.ordinal()], 3);
			return true;
		}
		return false;
	}
	
	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		return EnumFacing.VALUES;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip,
			boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		tooltip.add("info.slab.place");
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		EnumSlabState state2;
		switch (state2 = state.getValue(EnumSlabState.PROPERTY))
		{
		case DOUBLE_NS:
		case DOUBLE_UD:
		case DOUBLE_WE:
			return state;
		default:
			return state.withProperty(EnumSlabState.PROPERTY, EnumSlabState.FROM_FACING.apply(mirrorIn.mirror(state2.face)));
		}
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		EnumSlabState state2;
		switch (state2 = state.getValue(EnumSlabState.PROPERTY))
		{
		case DOUBLE_NS:
		case DOUBLE_UD:
		case DOUBLE_WE:
			return state;
		default:
			return state.withProperty(EnumSlabState.PROPERTY, EnumSlabState.FROM_FACING.apply(rot.rotate(state2.face)));
		}
	}
}