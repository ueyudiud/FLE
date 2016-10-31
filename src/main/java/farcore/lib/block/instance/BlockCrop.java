package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.M;
import farcore.lib.block.BlockSingleTE;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.material.Mat;
import farcore.lib.model.block.StateMapperCrop;
import farcore.lib.prop.PropertyString;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.util.BlockStateWrapper;
import farcore.lib.util.SubTag;
import farcore.util.U.Client;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrop extends BlockSingleTE implements IPlantable
{
	@Deprecated
	public static class CropState extends BlockStateWrapper
	{
		public ICropAccess access;

		public CropState(ICropAccess access, IBlockState state)
		{
			super(state);
			this.access = access;
		}
		
		@Override
		protected BlockStateWrapper wrapState(IBlockState state)
		{
			return new CropState(access, state);
		}
	}

	public static final ThreadLocal<ICrop> CROP_THREAD = new ThreadLocal();
	public static final ThreadLocal<ItemStack> ITEM_THREAD = new ThreadLocal();
	public static final PropertyString PROP_CROP_TYPE;

	static
	{
		List<String> list = new ArrayList();
		for(Mat material : Mat.filt(SubTag.CROP))
		{
			list.addAll(material.getProperty(M.property_crop).getAllowedState());
		}
		list.add("void");//Empty crop mark.
		PROP_CROP_TYPE = new PropertyString("crop", list);
	}

	public BlockCrop()
	{
		super(FarCore.ID, "crop", Material.PLANTS);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
		unharvestableSpeedMultiplier = 600F;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROP_CROP_TYPE);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return super.initDefaultState(state).withProperty(PROP_CROP_TYPE, "void");
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ModelLoader.setCustomStateMapper(this, new StateMapperCrop());
		ModelLoader.registerItemVariants(item, new ResourceLocation[0]);
		ModelLoader.setCustomMeshDefinition(item, (ItemStack stack) -> Client.MODEL_MISSING);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(FarCore.worldGenerationFlag)
		{
			if(CROP_THREAD.get() != null)
			{
				worldIn.setTileEntity(pos, new TECrop(CROP_THREAD.get()));
				return;
			}
		}
		if(ITEM_THREAD.get() != null)
		{
			ItemStack stack = ITEM_THREAD.get();
			if(stack.getItem() instanceof ItemSeed)
			{
				worldIn.setTileEntity(pos, new TECrop(
						ItemSeed.getMaterial(stack).crop,
						ItemSeed.getDNAFromStack(stack),
						ItemSeed.getGenerationFromStack(stack)));
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECrop();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TECrop)
		{
			state = state.withProperty(PROP_CROP_TYPE, ((TECrop) tile).getStateName());
		}
		return super.getActualState(state, worldIn, pos);
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
	
	public boolean canBlockStay(World world, BlockPos pos)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(pos)) instanceof TECrop)
			return ((TECrop) tile).canPlantAt();
		return true;
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TECrop ? ((TECrop) tile).getPlantType() : EnumPlantType.Crop;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return getDefaultState();
	}
}