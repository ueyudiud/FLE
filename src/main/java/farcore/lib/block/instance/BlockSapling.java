package farcore.lib.block.instance;

import java.util.List;

import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.block.BlockBase;
import farcore.lib.material.Mat;
import farcore.lib.prop.PropertyMaterial;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.SubTag;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSapling extends BlockBase implements IPlantable, ITileEntityProvider
{
	public static PropertyMaterial PROP_SAPLING;
	public static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(.1F, .0F, .1F, .9F, .8F, .9F);

	public BlockSapling()
	{
		super("farcore", "sapling", Material.PLANTS);
		setHardness(.4F);
		EnumBlock.sapling.set(this);
		for(Mat material : PROP_SAPLING.getAllowedValues())
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(material.id), MC.sapling.getLocal(material));
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		if(PROP_SAPLING == null)
		{
			PROP_SAPLING = new PropertyMaterial("material", SubTag.WOOD);
		}
		return new BlockStateContainer(this, PROP_SAPLING);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile;
		if((tile = worldIn.getTileEntity(pos)) instanceof TESapling)
			return state.withProperty(PROP_SAPLING, ((TESapling) tile).material);
		return state;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return SAPLING_AABB.offset(pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for(Mat material : Mat.register)
			if(material.hasTree)
			{
				list.add(new ItemStack(item, 1, material.id));
			}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity tile;
		if((tile = worldIn.getTileEntity(pos)) instanceof TESapling)
		{
			((TESapling) tile).setTree(placer, Mat.register.get(stack.getItemDamage(), M.VOID));
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState state;
		return (state = worldIn.getBlockState(pos.down())).getBlock()
				.canSustainPlant(state, worldIn, pos, EnumFacing.UP, this);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return getDefaultState();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TESapling();
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 200;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 50;
	}
}