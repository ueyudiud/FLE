package farcore.lib.block;

import java.util.Random;

import farcore.data.EnumSlabState;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockSlab extends BlockBase
{
	private static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5D, 1D);
	private static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0D, 0.5D, 0D, 1D, 1D, 1D);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.5D);
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0D, 0D, 0.5D, 1D, 1D, 1D);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0D, 0D, 0D, 0.5D, 1D, 1D);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.5D, 0D, 0D, 1D, 1D, 1D);

	public BlockSlab(String name, Material materialIn)
	{
		super(name, materialIn);
		lightOpacity = -1;
	}
	public BlockSlab(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		lightOpacity = -1;
	}
	public BlockSlab(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		lightOpacity = -1;
	}
	public BlockSlab(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		lightOpacity = -1;
	}

	protected abstract String getLocalName();

	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		String localName = getLocalName();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(3), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(4), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(5), localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(6), "Double " + localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(7), "Double " + localName + " Slab");
		LanguageManager.registerLocal(getTranslateNameForItemStack(8), "Double " + localName + " Slab");
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
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(EnumSlabState.PROPERTY,
				placer.isSneaking() ? (hitY > .5F ? EnumSlabState.up : EnumSlabState.down) :
					EnumSlabState.values()[facing.getOpposite().ordinal()]);
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
		case down : return AABB_DOWN;
		case up : return AABB_UP;
		case south : return AABB_NORTH;
		case north : return AABB_SOUTH;
		case west : return AABB_WEST;
		case east : return AABB_EAST;
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
		return lightOpacity != -1 ? lightOpacity : 0;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return lightOpacity != -1 ? (isOpaqueCube(state) ? lightOpacity : Math.min(lightOpacity, 3)) :
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
	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(this, quantityDropped(state, 0, RANDOM), damageDropped(state));
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		EnumSlabState state = world.getBlockState(pos).getValue(EnumSlabState.PROPERTY);
		if(state != EnumSlabState.rotationState[axis.ordinal()][state.ordinal()])
		{
			U.Worlds.switchProp(world, pos, EnumSlabState.PROPERTY, EnumSlabState.rotationState[axis.ordinal()][state.ordinal()], 3);
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
}