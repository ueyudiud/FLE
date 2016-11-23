package farcore.lib.model.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.MP;
import farcore.lib.block.instance.BlockOre.OreStateWrapper;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.block.instance.ItemOre;
import farcore.lib.material.Mat;
import farcore.lib.model.item.ICustomItemRenderModel;
import farcore.lib.util.Log;
import farcore.lib.util.SubTag;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelOre implements IModel, ICustomModelLoader, IStateMapper, ICustomItemModelSelector
{
	private static final String DEFAULT_MODEL_LOCATE = FarCore.ID + ":block/ore";
	private static final List<ResourceLocation> LOAD_TARGETS = new ArrayList();
	private static final ResourceLocation ITEM_PARENT_LOCATION = new ResourceLocation("minecraft", "block/block");
	private static final ResourceLocation LOCATION = new ResourceLocation(FarCore.ID, "blockstates/ore");
	private static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(FarCore.INNER_RENDER + ":ore", null);
	private static final ModelResourceLocation MODEL_ITEM_RESOURCE_LOCATION = new ModelResourceLocation(FarCore.INNER_RENDER + ":ore_item", null);
	private static final JsonDeserializer<OreRenderConfig> DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if(!(json instanceof JsonObject))
			throw new JsonParseException("The json should be an object!");
		JsonObject object = json.getAsJsonObject();
		OreRenderConfig config = new OreRenderConfig();
		if(object.has("types"))
		{
			JsonObject object1 = object.getAsJsonObject("types");
			for(Entry<String, JsonElement> entry : object1.entrySet())
			{
				/**
				 * Far Core wrote annotation in json.
				 */
				if("annotation".equals(entry.getKey()))
				{
					continue;
				}
				config.renderTypes.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
		if(object.has("textures"))
		{
			JsonObject object1 = object.getAsJsonObject("textures");
			for(Entry<String, JsonElement> entry : object1.entrySet())
			{
				config.sourceMap.put(entry.getKey(), new ResourceLocation(entry.getValue().getAsString()));
			}
		}
		if(object.has("model"))
		{
			JsonObject object1 = object.getAsJsonObject("model");
			for(Entry<String, JsonElement> entry : object1.entrySet())
			{
				config.modelMap.put(entry.getKey(), new ResourceLocation(entry.getValue().getAsString()));
			}
		}
		return config;
	};
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(OreRenderConfig.class, DESERIALIZER)
			.create();
	public static final ModelOre instance = new ModelOre();
	
	static
	{
		LOAD_TARGETS.add(LOCATION);
	}
	
	public static void addOreConfig(ResourceLocation location)
	{
		LOAD_TARGETS.add(location);
	}

	@SideOnly(Side.CLIENT)
	static class OreRenderConfig
	{
		Map<String, String> renderTypes = new HashMap();
		Map<String, ResourceLocation> sourceMap = new HashMap();
		Map<String, ResourceLocation> modelMap = new HashMap();
	}

	private Map<String, String> renderTypes = new HashMap();
	private Map<String, ResourceLocation> sourceMap = new HashMap();
	private Map<String, ResourceLocation> models = new HashMap();
	
	private ModelOre(){ }

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		models.clear();
		sourceMap.clear();
		renderTypes.clear();
		getSource();
		for(ResourceLocation location : LOAD_TARGETS)
		{
			String locate = location.toString();
			if(!locate.endsWith(".json"))
			{
				locate += ".json";
			}
			location = new ResourceLocation(locate);
			JsonReader reader = null;
			try
			{
				IResource blockstateResource = resourceManager.getResource(location);
				InputStream stream = blockstateResource.getInputStream();
				reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
				OreRenderConfig config = GSON.fromJson(reader, OreRenderConfig.class);
				models.putAll(config.modelMap);
				sourceMap.putAll(config.sourceMap);
				renderTypes.putAll(config.renderTypes);
			}
			catch (IOException exception)
			{
				Log.warn("Fail to load ore render config from file.", exception);
			}
			catch (Exception exception)
			{
				Log.error("The render config is invalid.", exception);
			}
			finally
			{
				if(reader != null)
				{
					try
					{
						reader.close();
					}
					catch(Exception exception)
					{
						exception.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return MODEL_RESOURCE_LOCATION.toString().equals(modelLocation.toString()) ||
				MODEL_ITEM_RESOURCE_LOCATION.toString().equals(modelLocation.toString());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return MODEL_ITEM_RESOURCE_LOCATION.equals(modelLocation) ?
				new ModelOreItem() : instance;
	}
	
	@Override
	public List<ResourceLocation> getAllowedResourceLocations(Item item)
	{
		return ImmutableList.of(MODEL_ITEM_RESOURCE_LOCATION);
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return MODEL_ITEM_RESOURCE_LOCATION;
	}
	
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		ImmutableMap.Builder<IBlockState, ModelResourceLocation> builder = ImmutableMap.builder();
		for(IBlockState state : blockIn.getBlockState().getValidStates())
		{
			builder.put(state, MODEL_RESOURCE_LOCATION);
		}
		return builder.build();
	}
	
	private Map<String, ResourceLocation> getSource()
	{
		if(sourceMap.isEmpty())
		{
			for(Mat material : Mat.materials())
			{
				if(material.contain(SubTag.ORE))
				{
					for(EnumOreAmount amount : EnumOreAmount.values())
					{
						sourceMap.put(material.name + "@" + amount.name(), new ResourceLocation(material.modid, "blocks/ore/" + amount.name() + "/" + material.name));
					}
				}
			}
		}
		return sourceMap;
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return getSource().values();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		Map<String, IModel> models = new HashMap();
		ImmutableMap.Builder<String, IBakedModel> typeBuilder = ImmutableMap.builder();
		for(Mat material : Mat.materials())
		{
			if(material.contain(SubTag.ORE))
			{
				String key = material.name;
				String type = null;
				if(renderTypes.containsKey(key))
				{
					type = renderTypes.get(key);
					if(this.models.containsKey(type))
					{
						type = this.models.get(type).toString();
					}
				}
				else
				{
					type = DEFAULT_MODEL_LOCATE;
				}
				IModel model;
				if(!models.containsKey(type))
				{
					models.put(key, model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(type)));
				}
				else
				{
					model = models.get(key);
				}
				for(EnumOreAmount amount : EnumOreAmount.values())
				{
					String key1 = key + "@" + amount.name();
					if(model instanceof IRetexturableModel)
					{
						String location = sourceMap.containsKey(key) ? sourceMap.get(key).toString() : sourceMap.containsKey(key1) ? sourceMap.get(key1).toString() : material.modid + "blocks/ore/" + material.name;
						model = ((IRetexturableModel) model).retexture(ImmutableMap.of("ore", location));
					}
					typeBuilder.put(key1, model.bake(state, format, bakedTextureGetter));
				}
			}
		}
		Map<String, IBakedModel> bakedmodels = typeBuilder.build();
		return new BakedModelOre(bakedmodels, format);
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	private class ModelOreItem implements IModel
	{
		@Override
		public Collection<ResourceLocation> getDependencies()
		{
			return ImmutableList.of(ITEM_PARENT_LOCATION);
		}
		
		@Override
		public Collection<ResourceLocation> getTextures()
		{
			return sourceMap.values();
		}
		
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			IModel parent = ModelLoaderRegistry.getModelOrMissing(ITEM_PARENT_LOCATION);
			Map<String, IModel> models = new HashMap();
			ImmutableMap.Builder<String, IBakedModel> typeBuilder = ImmutableMap.builder();
			for(Mat material : Mat.materials())
			{
				if(material.contain(SubTag.ORE))
				{
					String key = material.name;
					String type = null;
					if(renderTypes.containsKey(key))
					{
						type = renderTypes.get(key);
						if(ModelOre.this.models.containsKey(type))
						{
							type = ModelOre.this.models.get(type).toString();
						}
					}
					else
					{
						type = DEFAULT_MODEL_LOCATE;
					}
					IModel model;
					if(!models.containsKey(type))
					{
						models.put(key, model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(type)));
					}
					else
					{
						model = models.get(key);
					}
					for(EnumOreAmount amount : EnumOreAmount.values())
					{
						String key1 = key + "@" + amount.name();
						if(model instanceof IRetexturableModel)
						{
							String location = sourceMap.containsKey(key) ? sourceMap.get(key).toString() : sourceMap.containsKey(key1) ? sourceMap.get(key1).toString() : material.modid + "blocks/ore/" + material.name;
							model = ((IRetexturableModel) model).retexture(ImmutableMap.of("ore", location));
						}
						typeBuilder.put(key1, model.bake(state, format, bakedTextureGetter));
					}
				}
			}
			Map<String, IBakedModel> bakedmodels = typeBuilder.build();
			return new BakedModelOreItem(bakedmodels, format, parent.bake(state, format, bakedTextureGetter));
		}
		
		@Override
		public IModelState getDefaultState()
		{
			return TRSRTransformation.identity();
		}
	}

	private static class BakedModelOreItem extends BakedModelOre
	{
		private IBakedModel parent;
		
		BakedModelOreItem(Map<String, IBakedModel> models, VertexFormat format, IBakedModel parent)
		{
			super(models, format);
			this.parent = parent;
		}

		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return parent.getItemCameraTransforms();
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return parent.getOverrides();
		}
		
		@Override
		public boolean isGui3d()
		{
			return parent.isGui3d();
		}
	}
	
	private static class BakedModelOre implements ICustomItemRenderModel
	{
		Map<String, TextureAtlasSprite> icons;
		Map<String, IBakedModel> models;
		
		BakedModelOre(Map<String, IBakedModel> models, VertexFormat format)
		{
			this.models = models;
		}

		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			if(stack == null || !stack.hasTagCompound()) return ImmutableList.of();
			IBlockState state = EnumBlock.ore.block.getDefaultState();
			NBTTagCompound nbt = stack.getTagCompound();
			state = new OreStateWrapper(state, Mat.material(stack.getItemDamage()), ItemOre.getAmount(nbt), ItemOre.getRockMaterial(nbt), ItemOre.getRockType(nbt));
			return getQuads(state, facing, rand);
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			if(state instanceof OreStateWrapper)
			{
				OreStateWrapper wrapper = (OreStateWrapper) state;
				Mat ore = wrapper.ore;
				if(ore == Mat.VOID) return ImmutableList.of();
				Block block = wrapper.rock.getProperty(MP.property_rock).block;
				IBlockState state2 = block.getDefaultState().withProperty(BlockRock.ROCK_TYPE, wrapper.type);
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
						.getModelManager().getBlockModelShapes()
						.getModelForState(state2);
				ImmutableList.Builder<BakedQuad> list = ImmutableList.builder();
				list.addAll(model.getQuads(state2, side, rand));
				if(models.containsKey(ore.name + "@" + wrapper.amount.name()))
				{
					list.addAll(models.get(ore.name + "@" + wrapper.amount.name()).getQuads(state.getBlock().getDefaultState(), side, rand));
				}
				return list.build();
			}
			return ImmutableList.of();
		}

		@Override
		public boolean isAmbientOcclusion()
		{
			return false;
		}

		@Override
		public boolean isGui3d()
		{
			return true;
		}

		@Override
		public boolean isBuiltInRenderer()
		{
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		}

		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return ItemCameraTransforms.DEFAULT;
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return ItemOverrideList.NONE;
		}
	}
}