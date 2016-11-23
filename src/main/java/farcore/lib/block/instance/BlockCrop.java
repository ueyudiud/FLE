package farcore.lib.block.instance;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.MP;
import farcore.lib.block.BlockSingleTE;
import farcore.lib.crop.ICrop;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.material.Mat;
import farcore.lib.model.block.StateMapperCrop;
import farcore.lib.prop.PropertyString;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.util.Log;
import farcore.lib.util.SubTag;
import farcore.util.U.Client;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
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
	@SideOnly(Side.CLIENT)
	public static enum CropSelectBoxHeightHandler implements IResourceManagerReloadListener
	{
		INSTANCE;
		
		private static final Gson GSON = new GsonBuilder()
				.registerTypeAdapter(CropSelectBoxHeightHandler.class, (JsonDeserializer<CropSelectBoxHeightHandler>)
						(JsonElement json, Type typeOfT, JsonDeserializationContext context) -> INSTANCE.decode(json, context)).create();
		private static final AxisAlignedBB selectBoundBoxCache = new AxisAlignedBB(.0625F, .0F, .0625F, .9375F, 1F, .9375F);

		private Mat loadingMaterial;
		private Map<String, Float> bounds;

		private CropSelectBoxHeightHandler decode(JsonElement json, JsonDeserializationContext context)
		{
			if(loadingMaterial == null) throw new RuntimeException("No material is loading!");
			if(!json.isJsonObject()) throw new JsonParseException("Should be an object");
			JsonObject object = json.getAsJsonObject();
			if(object.has("height"))
			{
				JsonObject object2 = object.getAsJsonObject("height");
				for(Entry<String, JsonElement> entry : object2.entrySet())
				{
					if(bounds.containsKey(entry.getKey()))
						throw new RuntimeException("The same state has registered twice!");
					bounds.put(entry.getKey(), entry.getValue().getAsFloat() / 16F);//Use 16 pixel per block.
				}
			}
			return this;
		}
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			bounds = new HashMap();
			Log.reset();
			ResourceLocation location;
			for(Mat material : Mat.filt(SubTag.CROP))
			{
				loadingMaterial = material;
				location = new ResourceLocation(material.modid, "blockstates/crop/" + material.name + ".json");
				try
				{
					IResource resource = resourceManager.getResource(location);
					GSON.fromJson(new InputStreamReader(resource.getInputStream()), CropSelectBoxHeightHandler.class);
					resource.close();
				}
				catch (IOException | JsonParseException exception)
				{
					Log.cache(location);
				}
				catch (RuntimeException exception)
				{
					Log.error("Fail to load %s.", exception, location.toString());
				}
			}
			loadingMaterial = null;
			Log.logCachedInformations((Object object) -> ((ResourceLocation) object).toString(), "The crop path is mssing a height mapping.");
			bounds = ImmutableMap.copyOf(bounds);
		}

		public AxisAlignedBB getSelectBoundBox(String state, AxisAlignedBB def)
		{
			Float val = bounds.get(state);
			if(val == null) return def;
			return selectBoundBoxCache.setMaxY(val);
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
			list.addAll(material.getProperty(MP.property_crop).getAllowedState());
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
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(CropSelectBoxHeightHandler.INSTANCE);
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
				TECrop crop;
				worldIn.setTileEntity(pos, crop = new TECrop(
						ItemSeed.getMaterial(stack).getProperty(MP.property_crop),
						ItemSeed.getDNAFromStack(stack),
						ItemSeed.getGenerationFromStack(stack)));
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
						ItemSeed.getDNAFromStack(stack),
						ItemSeed.getGenerationFromStack(stack));
		}
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