package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.MP;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.crop.ICrop;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.material.Mat;
import farcore.lib.model.block.statemap.StateMapperCrop;
import farcore.lib.tile.instance.TECrop;
import nebula.client.util.Client;
import nebula.common.block.BlockSingleTE;
import nebula.common.block.property.PropertyString;
import nebula.common.util.L;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
	public static final ThreadLocal<ICrop> CROP_THREAD = new ThreadLocal();
	public static final ThreadLocal<ItemStack> ITEM_THREAD = new ThreadLocal();
	public static final PropertyString PROP_CROP_TYPE;
	
	static
	{
		List<String> list = new ArrayList();
		for(Mat material : Mat.filt(SubTags.CROP))
		{
			ICrop crop = material.getProperty(MP.property_crop);
			L.consume(1, 1 + crop.getMaxStage(), idx->list.add(material.name + "_" + idx));
		}
		list.add("void");//Empty crop mark.
		PROP_CROP_TYPE = new PropertyString("crop", list);
	}
	
	public BlockCrop()
	{
		super(FarCore.ID, "crop", Materials.PLANT);
		EnumBlock.crop.set(this);
		setHardness(0.5F);
		this.uneffectiveSpeedMultiplier = 1F / 600F;
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
		ModelLoader.registerItemVariants(this.item);
		ModelLoader.setCustomMeshDefinition(this.item, stack -> Client.MODEL_MISSING);
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
				TECrop crop;
				worldIn.setTileEntity(pos, crop = new TECrop(
						ItemSeed.getMaterial(stack).getProperty(MP.property_crop),
						ItemSeed.getDNAFromStack(stack)));
				crop.syncToNearby();
			}
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(ITEM_THREAD.get() != null)
		{
			ItemStack stack = ITEM_THREAD.get();
			if(stack.getItem() instanceof ItemSeed)
				return new TECrop(
						ItemSeed.getMaterial(stack).getProperty(MP.property_crop),
						ItemSeed.getDNAFromStack(stack));
		}
		return new TECrop();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TECrop)
		{
			try
			{
				state = state.withProperty(PROP_CROP_TYPE, ((TECrop) tile).getStateName());
			}
			catch (Exception exception)//If client out of synch, the crop type might lost, use default instead.
			{
				return state;
			}
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
		/**
		 * How buggy Minecraft is!
		 * The every kinds of render layer will cut out fluid rendering
		 * behind face rendering destroy effect.
		 *
		 * This bug can not be fixed by mod, maybe we should wait for
		 * new version to fix it.
		 */
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