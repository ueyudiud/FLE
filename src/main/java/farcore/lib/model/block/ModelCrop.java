package farcore.lib.model.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
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
import farcore.lib.block.instance.BlockCrop.CropState;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.util.Log;
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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
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

/**
 * Waiting for new crop model loader.
 * @author ueyudiud
 *
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class ModelCrop implements IModel, ICustomModelLoader, IStateMapper
{
	private static final List<ResourceLocation> LOAD_TARGETS = new ArrayList();
	private static final ResourceLocation LOCATION = new ResourceLocation(FarCore.ID, "blockstates/crop");
	private static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(FarCore.INNER_RENDER + ":crop", null);
	private static final JsonDeserializer<CropRenderConfig> DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if(!(json instanceof JsonObject))
			throw new JsonParseException("The json should be an object!");
		JsonObject object = json.getAsJsonObject();
		CropRenderConfig config = new CropRenderConfig();
		if(object.has("types"))
		{
			JsonObject object1 = object.getAsJsonObject("types");
			for(Entry<String, JsonElement> entry : object1.entrySet())
			{
				RenderType type = RenderType.valueOf(entry.getValue().getAsString());
				if(type == null)
				{
					Log.warn("The entry value '" + entry.getValue().getAsString() + "' is not a type in list, use cross instead.");
					type = RenderType.cross;
				}
				config.renderTypes.put(entry.getKey(), type);
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
			.registerTypeAdapter(CropRenderConfig.class, DESERIALIZER)
			.create();
	public static final ModelCrop instance = new ModelCrop();
	
	static
	{
		LOAD_TARGETS.add(LOCATION);
	}
	
	public static void addCropConfig(ResourceLocation location)
	{
		LOAD_TARGETS.add(location);
	}

	@SideOnly(Side.CLIENT)
	static class CropRenderConfig
	{
		Map<String, RenderType> renderTypes = new HashMap();
		Map<String, ResourceLocation> sourceMap = new HashMap();
		Map<String, ResourceLocation> modelMap = new HashMap();
	}

	@SideOnly(Side.CLIENT)
	static enum RenderType
	{
		cross(new ResourceLocation(FarCore.ID, "block/crop/cross")),
		lattice(new ResourceLocation(FarCore.ID, "block/crop/lattice")),
		custom(null);
		
		ResourceLocation location;
		
		RenderType(ResourceLocation location)
		{
			this.location = location;
		}
	}
	
	private Map<String, RenderType> renderTypes = new HashMap();
	private Map<String, ResourceLocation> sourceMap = new HashMap();
	private Map<String, ResourceLocation> models = new HashMap();
	
	private ModelCrop(){ }

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
				CropRenderConfig config = GSON.fromJson(reader, CropRenderConfig.class);
				models.putAll(config.modelMap);
				sourceMap.putAll(config.sourceMap);
				renderTypes.putAll(config.renderTypes);
			}
			catch (IOException exception)
			{
				Log.warn("Fail to load crop render config from file.", exception);
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
		return MODEL_RESOURCE_LOCATION.toString().equals(modelLocation.toString());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return instance;
	}
	
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		return ImmutableMap.of(blockIn.getDefaultState(), MODEL_RESOURCE_LOCATION);
	}
	
	private Map<String, ResourceLocation> getSource()
	{
		if(sourceMap.isEmpty())
		{
			for(Mat material : Mat.materials())
			{
				if(material.isCrop)
				{
					for(String string : material.crop.getAllowedState())
					{
						sourceMap.put(material.name + "_" + string, new ResourceLocation(material.modid, "blocks/crop/" + material.name + "_" + string));
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
		EnumMap<RenderType, IModel> map = new EnumMap(RenderType.class);
		for(RenderType type : RenderType.values())
		{
			if(type != RenderType.custom)
			{
				map.put(type, ModelLoaderRegistry.getModelOrMissing(type.location));
			}
		}
		ImmutableMap.Builder<String, RenderType> typeBuilder = ImmutableMap.builder();
		ImmutableMap.Builder<String, IBakedModel> modelBuilder = ImmutableMap.builder();
		Map<String, RenderType> renderTypes = typeBuilder.build();
		for(Mat material : Mat.materials())
		{
			if(material.isCrop)
			{
				String key = material.name;
				if(renderTypes.getOrDefault(key, RenderType.cross) != RenderType.custom)
				{
					IModel model1 = map.get(renderTypes.get(key));
					if(model1 instanceof IRetexturableModel)
					{
						String replaced = getSource().getOrDefault(material.name + "_" + key, TextureMap.LOCATION_MISSING_TEXTURE).toString();
						model1 = ((IRetexturableModel) model1).retexture(ImmutableMap.of("all", replaced, "particle", replaced, "crop", replaced));
					}
					modelBuilder.put(material.name + "_" + key, model1.bake(state, format, bakedTextureGetter));
					continue;
				}
				RenderType type = null;
				if(renderTypes.containsKey(key))
				{
					type = renderTypes.get(key);
				}
				for(String t1 : material.crop.getAllowedState())
				{
					key = material.name + "_" + t1;
					if(type != null)
					{
						typeBuilder.put(key, type);
					}
					else
					{
						typeBuilder.put(key, renderTypes.getOrDefault(key, RenderType.cross));
					}
				}
			}
		}
		for(Entry<String, ResourceLocation> entry : models.entrySet())
		{
			if(Mat.contain(entry.getKey()))
			{
				Mat material = Mat.material(entry.getKey());
				IModel model = ModelLoaderRegistry.getModelOrMissing(entry.getValue());
				for(String string : material.crop.getAllowedState())
				{
					IModel model1 = model;
					if(model instanceof IRetexturableModel)
					{
						String replaced = getSource().getOrDefault(material.name + "_" + string, TextureMap.LOCATION_MISSING_TEXTURE).toString();
						model1 = ((IRetexturableModel) model).retexture(ImmutableMap.of("all", replaced, "particle", replaced, "crop", replaced));
					}
					modelBuilder.put(material.name + "_" + string, model1.bake(state, format, bakedTextureGetter));
				}
			}
			else
			{
				IModel model = ModelLoaderRegistry.getModelOrMissing(entry.getValue());
				if(model instanceof IRetexturableModel)
				{
					String replaced = getSource().getOrDefault(entry.getKey(), TextureMap.LOCATION_MISSING_TEXTURE).toString();
					model = ((IRetexturableModel) model).retexture(ImmutableMap.of("all", replaced, "particle", replaced, "crop", replaced));
				}
				modelBuilder.put(entry.getKey(), model.bake(state, format, bakedTextureGetter));
			}
		}
		Map<String, IBakedModel> models = modelBuilder.build();
		return new BakedModelCrop(models);
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
	
	private static class BakedModelCrop implements IBakedModel
	{
		Map<String, IBakedModel> models;

		BakedModelCrop(Map<String, IBakedModel> models)
		{
			this.models = models;
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			if(state instanceof CropState)
			{
				ICropAccess access = ((CropState) state).access;
				if(access.crop() == ICrop.VOID) return ImmutableList.of();
				String tag = access.crop().getRegisteredName() + "_" + access.crop().getState(access);
				if(models.containsKey(tag))
					return models.get(tag).getQuads(state, side, rand);
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
			return false;
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