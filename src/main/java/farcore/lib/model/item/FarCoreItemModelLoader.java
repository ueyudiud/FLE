package farcore.lib.model.item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.FarCore;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemFluidDisplay;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.model.item.ModelLayer.UnbakedModelLayer;
import farcore.lib.util.Log;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum FarCoreItemModelLoader implements ICustomModelLoader
{
	instance;

	public static final String NORMAL = "";
	public static final String PARTICLE = "particle";
	public static final Function<ItemStack, String> NORMAL_FUNCTION = (ItemStack stack) -> NORMAL;
	public static final Function<ItemStack, Integer> NORMAL_MULTIPLIER = (ItemStack stack) -> 0xFFFFFFFF;
	
	private static final Map<ResourceLocation, Function<ItemStack, Integer>> colorMultipliers = new HashMap();
	private static final Map<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> multiIconLoaders = new HashMap();
	private static final Map<ResourceLocation, Function<ItemStack, String>> submetaProviders = new HashMap();
	
	public static final JsonDeserializer<IMultiTextureCollection> TEXTURE_GETTER_DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if (!json.isJsonObject())
			throw new JsonParseException("The json should be an object!");
		JsonObject object = json.getAsJsonObject();
		if (object.has("parent"))
		{
			FarCoreMultiTextureModifier function = new FarCoreMultiTextureModifier(instance.loadTextures(new ResourceLocation(object.get("parent").getAsString())));
			if (object.has("domain"))
			{
				function.setDomain(object.get("domain").getAsString());
			}
			if (object.has("prefix"))
			{
				function.setPrefix(object.get("prefix").getAsString());
			}
			if (object.has("postfix"))
			{
				function.setPostfix(object.get("postfix").getAsString());
			}
			return function;
		}
		else
		{
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for (Entry<String, JsonElement> entry : object.entrySet())
			{
				builder.put(entry.getKey(), new ResourceLocation(entry.getValue().getAsString()));
			}
			return new FarCoreMultiTextureGetter(builder.build());
		}
	};
	public static final JsonDeserializer<UnbakedModelLayer> MODEL_LAYER_DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		UnbakedModelLayer layer = new UnbakedModelLayer();
		if (json.isJsonObject())
		{
			JsonObject object = json.getAsJsonObject();
			if (object.has("converts"))
			{
				String value = object.get("converts").getAsString();
				switch (value.charAt(0))
				{
				case '#':
					layer.converts = value.substring(1);
					break;
				default:
					layer.convertLocation = new ResourceLocation(value);
					break;
				}
				layer.renderFull3D = false;//Use covert will force to disable render full 3D option.
			}
			else if (object.has("renderFull3D"))
			{
				layer.renderFull3D = object.get("renderFull3D").getAsBoolean();
			}
			if (object.has("zLevel"))
			{
				layer.zOffset = object.get("zLevel").getAsFloat();
			}
			if (object.has("color"))
			{
				layer.baseColor = object.get("color").getAsInt();
			}
			if (object.has("textures"))
			{
				JsonElement json1 = object.get("textures");
				if (json1.isJsonArray())
				{
					JsonArray array = json1.getAsJsonArray();
					for (JsonElement json2 : array)
					{
						layer.allowedTextures.add(json2.getAsString());
					}
				}
				else if (json1.isJsonObject())
				{
					JsonObject object1 = json1.getAsJsonObject();
					for (Entry<String, JsonElement> entry : object1.entrySet())
					{
						String key = entry.getKey();
						switch (key.charAt(0))
						{
						case '#' :
							layer.allowedTextures.add(key.substring(1));
							break;
						case '[' :
							layer.allowedTextures.add(key);
							break;
						default:
							layer.locations.put(key, new ResourceLocation(entry.getValue().getAsString()));
							break;
						}
					}
				}
				else throw new JsonParseException("The 'textures' must be an array or object, use single texture please use 'texture' for key.");
			}
			else if (object.has("texture"))
			{
				String key = object.get("texture").getAsString();
				switch (key.charAt(0))
				{
				case '#' :
					layer.allowedTextures.add(key.substring(1));
					break;
				case '[' :
					layer.allowedTextures.add(key);
					break;
				default:
					layer.locations.put(NORMAL, new ResourceLocation(key));
					break;
				}
			}
			else
			{
				layer.allowedTextures = null;//Get all textures if no selected.
			}
			if (object.has("submeta"))
			{
				layer.function = instance.loadSubmetaGetter(object.get("submeta"));
			}
			if (object.has("colorMultiplier"))
			{
				layer.colorMultiplier = instance.loadColorMultiplier(new ResourceLocation(object.get("colorMultiplier").getAsString()));
			}
		}
		else if (json.isJsonArray())
			//TODO I don't know what to do yet.
			throw new JsonParseException("The json can not be an array.");
		else
		{
			String locate = json.getAsString();
			char chr = locate.charAt(0);
			switch (chr)
			{
			case '#' :
				layer.allowedTextures.add(locate.substring(1));
				break;
			case '[' :
				layer.allowedTextures.add(locate);
				break;
			case '~' ://Multi texture selector, only use in single locate.
				layer.allowedTextures = null;//Mark for include all textures.
				layer.function = instance.loadSubmetaGetter(new ResourceLocation(locate.substring(1)));
				break;
			default:
				layer.locations.put(NORMAL, new ResourceLocation(locate));
				break;
			}
		}
		return layer;
	};
	public static final JsonDeserializer<ItemModelCache> ITEM_MODEL_CACHE_DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if(!json.isJsonObject())
			throw new JsonParseException("The json should be an object!");
		ItemModelCache cache = new ItemModelCache();
		JsonObject object = json.getAsJsonObject();
		if (object.has("parent"))
		{
			cache.model = new ResourceLocation(object.get("parent").getAsString());
			ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			if (object.has("textures"))
			{
				for(Entry<String, JsonElement> entry : object.getAsJsonObject("textures").entrySet())
				{
					String value = entry.getValue().getAsString();
					builder.put(entry.getKey(), value);
				}
			}
			cache.retextures = builder.build();
			return cache;
		}
		ImmutableMap.Builder<String, ResourceLocation> textures = ImmutableMap.builder();
		ImmutableMap.Builder<String, Function<IResourceManager, Map<String, ResourceLocation>>> multiTextures = ImmutableMap.builder();
		String particleLocate = null;
		if (object.has("include"))//Include textures.
		{
			JsonElement json1 = object.get("include");
			if (json1.isJsonObject())//Put more than one texture in to map.
			{
				JsonObject object1 = json1.getAsJsonObject();
				for (Entry<String, JsonElement> entry : object1.entrySet())
				{
					String value = entry.getValue().getAsString();
					if (PARTICLE.equals(entry.getKey()))
					{
						particleLocate = value;
						continue;
					}
					switch (value.charAt(0))
					{
					case '[' :
						ResourceLocation location1 = new ResourceLocation(value.substring(1));
						Function<IResourceManager, Map<String, ResourceLocation>> function = multiIconLoaders.get(location1);
						if (function != null)
						{
							multiTextures.put(entry.getKey(), function);
						}
						else
						{
							multiTextures.put(entry.getKey(), instance.loadTextureGetter(location1));
						}
						break;
					default :
						textures.put(entry.getKey(), new ResourceLocation(value));
						break;
					}
				}
			}
			else//Put single texture or only one texture set into map.
			{
				String value = json1.getAsString();
				if(value.charAt(0) == '[')
				{
					ResourceLocation location1 = new ResourceLocation(value.substring(1));
					Function<IResourceManager, Map<String, ResourceLocation>> function = multiIconLoaders.get(location1);
					if(function != null)
					{
						multiTextures.put(NORMAL, function);
					}
					else
					{
						multiTextures.put(NORMAL, instance.loadTextureGetter(location1));
					}
				}
				else
				{
					textures.put(NORMAL, new ResourceLocation(particleLocate = value));
				}
			}
		}
		cache.particle = particleLocate;
		cache.textures = textures.build();
		cache.multiTextures = multiTextures.build();
		if (object.has("layers"))
		{
			JsonElement json1 = object.get("layers");
			if (json1.isJsonArray())
			{
				JsonArray array = json1.getAsJsonArray();
				cache.layers = new UnbakedModelLayer[array.size()];
				for (int i = 0; i < array.size(); ++i)
				{
					UnbakedModelLayer layer = context.deserialize(array.get(i), UnbakedModelLayer.class);
					layer.layer = i;
					cache.layers[i] = layer;
				}
			}
			else if (json1.isJsonObject())
			{
				JsonObject object1 = json1.getAsJsonObject();
				int length = 0;
				List<UnbakedModelLayer> list = new ArrayList();
				while (object1.has("layer" + length))
				{
					JsonElement json2 = object1.get("layer" + length);
					UnbakedModelLayer layer = context.deserialize(json2, UnbakedModelLayer.class);
					layer.layer = length;
					list.add(length, layer);
					++length;
				}
				if (length == 0)
				{
					UnbakedModelLayer layer = context.deserialize(object1, UnbakedModelLayer.class);
					layer.layer = 0;
					cache.layers = new UnbakedModelLayer[]{ layer };
				}
				else
				{
					cache.layers = U.L.cast(list, UnbakedModelLayer.class);
				}
			}
			else
			{
				UnbakedModelLayer layer = context.deserialize(json1, UnbakedModelLayer.class);
				layer.layer = 0;
				cache.layers = new UnbakedModelLayer[]{ layer };
			}
		}
		else
		{
			UnbakedModelLayer layer = new UnbakedModelLayer();
			layer.layer = 0;
			layer.allowedTextures.add(NORMAL);
			cache.layers = new UnbakedModelLayer[]{ layer };
			if(!cache.textures.containsKey(NORMAL) && cache.multiTextures.isEmpty())
			{
				cache.textures.put(NORMAL, TextureMap.LOCATION_MISSING_TEXTURE);//If model doesn't contain any texture.
			}
		}
		return cache;
	};

	static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(IMultiTextureCollection.class, TEXTURE_GETTER_DESERIALIZER)
			.registerTypeAdapter(UnbakedModelLayer.class, MODEL_LAYER_DESERIALIZER)
			.registerTypeAdapter(ItemModelCache.class, ITEM_MODEL_CACHE_DESERIALIZER)
			.create();

	static
	{
		init();
	}

	public static class ItemModelCache
	{
		ResourceLocation model;
		ImmutableMap<String, String> retextures;

		Map<String, ResourceLocation> textures;
		Map<String, Function<IResourceManager, Map<String, ResourceLocation>>> multiTextures;

		String particle;
		
		UnbakedModelLayer[] layers;
		
		void registerItemColor(Item item, ItemColors colors)
		{
			for (UnbakedModelLayer layer : layers)
			{
				if (layer.colorMultiplier != FarCoreItemModelLoader.NORMAL_MULTIPLIER)
				{
					colors.registerItemColorHandler((ItemStack stack, int tintIndex) -> U.L.cast(layers[tintIndex].colorMultiplier.apply(stack)), item);
					return;
				}
			}
		}
	}

	public static void registerMultiIconProvider(ResourceLocation location, Function<IResourceManager, Map<String, ResourceLocation>> function)
	{
		multiIconLoaders.put(location, function);
	}
	
	public static void registerSubmetaProvider(ResourceLocation location, Function<ItemStack, String> function)
	{
		submetaProviders.put(location, function);
	}
	
	public static void registerColorMultiplier(ResourceLocation location, Function<ItemStack, Integer> function)
	{
		colorMultipliers.put(location, function);
	}

	public static void registerModel(Item item, ResourceLocation location)
	{
		ModelResourceLocation location1 = new ModelResourceLocation(item.getRegistryName(), "iventory");
		ModelLoader.setCustomMeshDefinition(item, (ItemStack stack) -> location1);
		ModelLoader.registerItemVariants(item, location1);
		location = new ResourceLocation(location.getResourceDomain(), "f_model/item/" + location.getResourcePath() + ".json");
		instance.acceptItem.put(item, location);
		instance.itemModelMap.put(location1, item);
	}

	private boolean isResourceLoading = false;
	private IResourceManager resourceManager;
	
	private Map<ModelResourceLocation, Item> itemModelMap = new HashMap();
	private Map<Item, ResourceLocation> acceptItem = new HashMap();
	
	private Map<Item, ItemModelCache> loadedModels = new HashMap();

	private Map<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> markMultiTextureLoaders;
	private Map<ResourceLocation, IMultiTextureCollection> loadedMultiTexturesMap;
	Map<Function<IResourceManager, Map<String, ResourceLocation>>, Map<String, ResourceLocation>> buildMultiTexturesMap;
	
	private Function<IResourceManager, Map<String, ResourceLocation>> loadTextureGetter(ResourceLocation location)
	{
		Function<IResourceManager, Map<String, ResourceLocation>> function = multiIconLoaders.get(location);
		if (function != null) return function;
		function = (IResourceManager manager) -> loadTextures(location).apply();
		markMultiTextureLoaders.put(location, function);
		return function;
	}

	private IMultiTextureCollection loadTextures(ResourceLocation location)
	{
		IMultiTextureCollection collection;
		if (loadedMultiTexturesMap == null)
		{
			loadedMultiTexturesMap = new HashMap();
		}
		else if ((collection = loadedMultiTexturesMap.get(location)) != null)
			return collection;
		//Create file in far texture map file.
		//Location is f_tm/[your path].json
		String l = "f_tm/" + location.getResourcePath();
		if(!l.endsWith(".json"))
		{
			l += ".json";
		}
		location = new ResourceLocation(location.getResourceDomain(), l);
		try
		{
			IResource resource = resourceManager.getResource(location);
			byte[] code = IOUtils.toByteArray(resource.getInputStream());
			resource.close();
			Reader reader = new InputStreamReader(new ByteArrayInputStream(code));
			collection = FarCoreItemModelLoader.GSON.fromJson(reader, IMultiTextureCollection.class);
		}
		catch (JsonParseException exception)
		{
			Log.cache(exception);
			return null;
		}
		catch (IOException exception)
		{
			Log.cache(exception);
			return null;
		}
		loadedMultiTexturesMap.put(location, collection);
		return collection;
	}
	
	private Function<ItemStack, String> loadSubmetaGetter(JsonElement json)
	{
		if(json.isJsonObject())
		{
			JsonObject object = json.getAsJsonObject();
			if (object.has("key")) return (ItemStack stack) -> object.get("key").getAsString();
			else if (object.has("parent"))//Raw type.
			{
				FarCoreVariantSubmetaProvider function = new FarCoreVariantSubmetaProvider(loadSubmetaGetter(object.get("parent")));
				if (object.has("variant"))
				{
					ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
					JsonObject object1 = object.getAsJsonObject("variant");
					for (Entry<String, JsonElement> entry : object1.entrySet())
					{
						builder.put(entry.getKey(), entry.getValue().getAsString());
					}
					function.setVariant(builder.build());
				}
				if (object.has("replacement"))
				{
					ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
					JsonObject object1 = object.getAsJsonObject("replacement");
					for (Entry<String, JsonElement> entry : object1.entrySet())
					{
						builder.put(entry.getKey(), entry.getValue().getAsString());
					}
					function.setReplacement(builder.build());
				}
				if (object.has("postfix"))
				{
					function.setPostfix(object.get("prefix").getAsString());
				}
				if (object.has("postfix"))
				{
					function.setPostfix(object.get("postfix").getAsString());
				}
				return function;
			}
			return NORMAL_FUNCTION;
		}
		else
		{
			String value = json.getAsString();
			return value.charAt(0) == '#' ? loadSubmetaGetter(new ResourceLocation(value.substring(1))) : (ItemStack stack) -> value;
		}
	}
	
	private Function<ItemStack, String> loadSubmetaGetter(ResourceLocation location)
	{
		Function<ItemStack, String> function = submetaProviders.get(location);
		if (function != null) return function;
		return NORMAL_FUNCTION;//Raw method.
	}
	
	private Function<ItemStack, Integer> loadColorMultiplier(ResourceLocation location)
	{
		return colorMultipliers.getOrDefault(location, NORMAL_MULTIPLIER);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		Log.reset();
		Log.info("Far Core Item Model Loader start model loading.");
		loadedMultiTexturesMap = null;
		markMultiTextureLoaders = new HashMap(multiIconLoaders);
		resourceManager = manager;
		isResourceLoading = true;
		loadedModels.clear();
		ProgressBar bar = ProgressManager.push("Loading FarCore Item Model", acceptItem.size());
		IResource resource = null;
		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		for(Entry<Item, ResourceLocation> entry : acceptItem.entrySet())
		{
			try
			{
				bar.step(entry.getValue().toString());
				ResourceLocation location = entry.getValue();
				resource = manager.getResource(location);
				byte[] codes = IOUtils.toByteArray(resource.getInputStream());
				try
				{
					resource.close();
				}
				catch (IOException exception)
				{
					exception.printStackTrace();
				}
				ItemModelCache cache = GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(codes), Charsets.UTF_8), ItemModelCache.class);
				if (colors != null)
				{
					cache.registerItemColor(entry.getKey(), colors);
				}
				loadedModels.put(entry.getKey(), cache);
			}
			catch (Exception exception)
			{
				Log.cache(exception);
			}
		}
		ProgressManager.pop(bar);
		buildMultiTexturesMap = new HashMap();
		bar = ProgressManager.push("Collect FarCore Textures", multiIconLoaders.size() + (loadedMultiTexturesMap != null ? loadedMultiTexturesMap.size() : 0));
		for (Entry<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> entry : markMultiTextureLoaders.entrySet())
		{
			bar.step(entry.getKey().toString());
			buildMultiTexturesMap.put(entry.getValue(), entry.getValue().apply(manager));
		}
		if (loadedMultiTexturesMap != null)
		{
			for (Entry<ResourceLocation, IMultiTextureCollection> entry : loadedMultiTexturesMap.entrySet())
			{
				bar.step(entry.getKey().toString());
				buildMultiTexturesMap.put(entry.getValue(), entry.getValue().apply());
			}
		}
		ProgressManager.pop(bar);
		isResourceLoading = false;
		markMultiTextureLoaders = null;
		loadedMultiTexturesMap = null;
		Log.logCachedExceptions();
		Log.info("Far Core Item Model Loader finished model loading.");
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return itemModelMap.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		Item item = itemModelMap.get(modelLocation);
		if (loadedModels.containsKey(item))
		{
			ItemModelCache modelCache = loadedModels.get(item);
			if (modelCache.model != null)
			{
				IModel model = ModelLoaderRegistry.getModelOrMissing(modelCache.model);
				if (model instanceof IRetexturableModel)
				{
					IRetexturableModel retexturableModel = (IRetexturableModel) model;
					return retexturableModel.retexture(modelCache.retextures);
				}
				return model;
			}
			return new ModelItemBase(this, modelCache);
		}
		return ModelLoaderRegistry.getMissingModel();
	}
	
	private static void init()
	{
		registerSubmetaProvider(new ResourceLocation("minecraft", "damage"), (ItemStack stack) -> Integer.toString(stack.getItemDamage()));
		registerSubmetaProvider(new ResourceLocation("forge", "registry_name"), (ItemStack stack) -> stack.getItem().getRegistryName().toString());
		registerSubmetaProvider(new ResourceLocation("forge", "contain_fluid"),
				(ItemStack stack) ->
		{
			FluidStack stack1 = FluidUtil.getFluidContained(stack);
			return stack1 == null ? "fluid:empty" : "fluid:" + stack1.getFluid().getName();
		});
		registerSubmetaProvider(new ResourceLocation(FarCore.ID, "material"), (ItemStack stack) -> "material:" + ItemMulti.getMaterial(stack).name);
		registerSubmetaProvider(new ResourceLocation(FarCore.ID, "display_fluid"), (ItemStack stack) -> "fluid:" + ItemFluidDisplay.getFluid(stack).getName());
		registerMultiIconProvider(new ResourceLocation("forge", "fluid"), (IResourceManager manager) ->
		{
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for (Entry<String, Fluid> fluid : FluidRegistry.getRegisteredFluids().entrySet())
			{
				builder.put("fluid:" + fluid.getKey(), fluid.getValue().getStill());
			}
			return builder.build();
		});
		for(MatCondition condition : MatCondition.register)
		{
			registerMultiIconProvider(new ResourceLocation(FarCore.ID, "group/" + condition.name), (IResourceManager manager) ->
			{
				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
				for (Mat material : Mat.filt(condition))
				{
					builder.put("material:" + material.name, new ResourceLocation(material.modid, "items/group/" + condition.name + "/" + material.name));
				}
				return builder.build();
			});
		}
		
		registerColorMultiplier(new ResourceLocation("minecraft", "armor"), (ItemStack stack) -> ((ItemArmor) stack.getItem()).getColor(stack));
		registerColorMultiplier(new ResourceLocation("minecraft", "banner"), (ItemStack stack) -> ItemBanner.getBaseColor(stack).getMapColor().colorValue);
		registerColorMultiplier(new ResourceLocation("forge", "fluid"), (ItemStack stack) ->
		{
			FluidStack stack1 = FluidUtil.getFluidContained(stack);
			return stack1 == null ? 0xFFFFFFFF : stack1.getFluid().getColor(stack1);
		});
		registerColorMultiplier(new ResourceLocation(FarCore.ID, "material"), (ItemStack stack) -> ItemMulti.getMaterial(stack).RGB);
		registerColorMultiplier(new ResourceLocation(FarCore.ID, "display_fluid"), (ItemStack stack) -> ItemFluidDisplay.getFluid(stack).getColor());
	}
}