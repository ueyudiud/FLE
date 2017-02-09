package farcore.lib.block.instance;

import java.util.List;

import farcore.data.EnumBlock;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import farcore.lib.model.block.ModelSapling;
import farcore.lib.tile.instance.TESapling;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSingleTE;
import nebula.common.util.OreDict;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSapling extends BlockSingleTE implements IPlantable
{
	public static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(.1F, .0F, .1F, .9F, .8F, .9F);
	
	public BlockSapling()
	{
		super("farcore", "sapling", Material.PLANTS);
		setHardness(.4F);
		EnumBlock.sapling.set(this);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		OreDict.registerValid("treeSapling", this);
		for(Mat material : Mat.filt(SubTags.TREE))
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(material.id), material.localName + " Sapling");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ModelLoaderRegistry.registerLoader(ModelSapling.instance);
		ModelLoader.setCustomStateMapper(this, ModelSapling.instance);
		Renders.registerCustomItemModelSelector(this, ModelSapling.instance);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BlockStateTileEntityWapper.wrap(world.getTileEntity(pos), state);
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
		for(Mat material : Mat.filt(SubTags.TREE))
		{
			list.add(new ItemStack(item, 1, material.id));
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
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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