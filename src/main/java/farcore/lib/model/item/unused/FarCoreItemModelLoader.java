package farcore.lib.model.item.unused;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.ToIntFunction;

import com.google.common.collect.Maps;

import farcore.lib.item.ItemBase;
import farcore.lib.model.item.FarCoreColorMultiplier;
import farcore.lib.model.item.FarCoreItemSubmetaGetterLoader;
import farcore.lib.model.item.FarCoreItemSubmetaGetterLoader.SubmetaGetter;
import farcore.lib.model.item.FarCoreTextureSet;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Far Core Item Model Loader, provide more flexible
 * model loader for creator, make more convenient model
 * file writing rules for resource pack maker.<br>
 * 
 * This type is out of date.
 * @author ueyudiud
 * @version 1.5
 */
@Deprecated
@SideOnly(Side.CLIENT)
public enum FarCoreItemModelLoader implements ICustomModelLoader
{
	INSTANCE;
	
	//	public static final String NORMAL = "";
	//	public static final String PARTICLE = "particle";
	//	public static final Function<ItemStack, String> NORMAL_FUNCTION = stack -> NORMAL;
	//	public static final Function<ItemStack, Integer> NORMAL_MULTIPLIER = stack -> 0xFFFFFFFF;
	//
	//	private static final Map<ResourceLocation, Function<ItemStack, Integer>> colorMultipliers = new HashMap();
	//
	//	public static final JsonDeserializer<FarCoreItemLayerUnbaked> MODEL_LAYER_DESERIALIZER =
	//			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	//	{
	//		FarCoreItemLayerUnbakedSimple layer = new FarCoreItemLayerUnbakedSimple();
	//		if (json.isJsonObject())
	//		{
	//			JsonObject object = json.getAsJsonObject();
	//			if (object.has("converts"))
	//			{
	//				layer.mark = 1;
	//				JsonElement element = object.get("coverts");
	//				if(!element.isJsonObject())
	//				{
	//					layer.coverts = FarCoreSubMetaGetterLoader.loadSubmetaGetter(FarCoreItemModelLoader.INSTANCE.resourceManager, element);
	//				}
	//				else
	//				{
	//					String value = element.getAsString();
	//					if(value.charAt(0) == '#')
	//					{
	//						layer.coverts = FarCoreSubMetaGetterLoader.loadSubmetaGetter(FarCoreItemModelLoader.INSTANCE.resourceManager, new ResourceLocation(value.substring(1)));
	//					}
	//					else
	//					{
	//						layer.coverts = (ItemStack stack) -> value;
	//						layer.covert.add(value);
	//					}
	//				}
	//				if (object.has("convertset"))
	//				{
	//					JsonObject object1 = object.getAsJsonObject("convertset");
	//					for (Entry<String, JsonElement> entry : object1.entrySet())
	//					{
	//						String key = entry.getKey();
	//						switch (key.charAt(0))
	//						{
	//						case '#' :
	//							layer.texturePool.add(key.substring(1));
	//							break;
	//						case '[' :
	//							layer.texturePool.add(key);
	//							FarCoreItemTextureSetLoader.registerTextureSetsLoader(new ResourceLocation(key.substring(1)));
	//							break;
	//						default:
	//							layer.textureSets.put(key, new ResourceLocation(entry.getValue().getAsString()));
	//							break;
	//						}
	//					}
	//				}
	//			}
	//			else if (object.has("renderFull3D"))
	//			{
	//				if(!object.get("renderFull3D").getAsBoolean())
	//				{
	//					layer.mark = 1;
	//				}
	//			}
	//			if (object.has("zLevel"))
	//			{
	//				layer.zOffset = object.get("zLevel").getAsFloat();
	//			}
	//			if (object.has("color"))
	//			{
	//				layer.baseColor = object.get("color").getAsInt();
	//			}
	//			if (object.has("textures"))
	//			{
	//				JsonElement json1 = object.get("textures");
	//				if (json1.isJsonArray())
	//				{
	//					JsonArray array = json1.getAsJsonArray();
	//					for (JsonElement json2 : array)
	//					{
	//						layer.texturePool.add(json2.getAsString());
	//					}
	//				}
	//				else if (json1.isJsonObject())
	//				{
	//					JsonObject object1 = json1.getAsJsonObject();
	//					for (Entry<String, JsonElement> entry : object1.entrySet())
	//					{
	//						String key = entry.getKey();
	//						switch (key.charAt(0))
	//						{
	//						case '#' :
	//							layer.texturePool.add(key.substring(1));
	//							break;
	//						case '[' :
	//							layer.texturePool.add(key);
	//							FarCoreItemTextureSetLoader.registerTextureSetsLoader(new ResourceLocation(key.substring(1)));
	//							break;
	//						default:
	//							layer.textureSets.put(key, new ResourceLocation(entry.getValue().getAsString()));
	//							break;
	//						}
	//					}
	//				}
	//				else throw new JsonParseException("The 'textures' must be an array or object, use single texture please use 'texture' for key.");
	//			}
	//			else if (object.has("texture"))
	//			{
	//				String key = object.get("texture").getAsString();
	//				switch (key.charAt(0))
	//				{
	//				case '#' :
	//					layer.texturePool.add(key.substring(1));
	//					break;
	//				case '[' :
	//					layer.texturePool.add(key);
	//					FarCoreItemTextureSetLoader.registerTextureSetsLoader(new ResourceLocation(key.substring(1)));
	//					break;
	//				default:
	//					layer.mark = 2;
	//					layer.base.add(NORMAL);
	//					layer.textureSets.put(NORMAL, new ResourceLocation(key));
	//					break;
	//				}
	//			}
	//			else
	//			{
	//				layer.texturePool = null;//Get all textures if no selected.
	//			}
	//			if (object.has("submeta"))
	//			{
	//				layer.function = FarCoreSubMetaGetterLoader.loadSubmetaGetter(FarCoreItemModelLoader.INSTANCE.resourceManager, object.get("submeta"));
	//			}
	//			if (object.has("colorMultiplier"))
	//			{
	//				layer.colorMultiplier = INSTANCE.loadColorMultiplier(new ResourceLocation(object.get("colorMultiplier").getAsString()));
	//			}
	//		}
	//		else if (json.isJsonArray())
	//			throw new JsonParseException("The json can not be an array.");
	//		else
	//		{
	//			String locate = json.getAsString();
	//			char chr = locate.charAt(0);
	//			switch (chr)
	//			{
	//			case '#' :
	//				layer.texturePool.add(locate.substring(1));
	//				break;
	//			case '[' :
	//				layer.texturePool.add(locate);
	//				FarCoreItemTextureSetLoader.registerTextureSetsLoader(new ResourceLocation(locate.substring(1)));
	//				break;
	//			case '~' ://Multi texture selector, only use in single locate.
	//				layer.texturePool = null;//Mark for include all textures.
	//				layer.function = FarCoreSubMetaGetterLoader.loadSubmetaGetter(FarCoreItemModelLoader.INSTANCE.resourceManager, new ResourceLocation(locate.substring(1)));
	//				break;
	//			default:
	//				layer.base.add(NORMAL);
	//				layer.textureSets.put(NORMAL, new ResourceLocation(locate));
	//				break;
	//			}
	//		}
	//		return layer;
	//	};
	//	public static final JsonDeserializer<ItemModelCache> ITEM_MODEL_CACHE_DESERIALIZER =
	//			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	//	{
	//		if(!json.isJsonObject())
	//			throw new JsonParseException("The json should be an object!");
	//		ItemModelCache cache = new ItemModelCache();
	//		JsonObject object = json.getAsJsonObject();
	//		if (object.has("parent"))
	//		{
	//			cache.model = new ResourceLocation(object.get("parent").getAsString());
	//			ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
	//			if (object.has("textures"))
	//			{
	//				for(Entry<String, JsonElement> entry : object.getAsJsonObject("textures").entrySet())
	//				{
	//					String value = entry.getValue().getAsString();
	//					builder.put(entry.getKey(), value);
	//				}
	//			}
	//			cache.retextures = builder.build();
	//			return cache;
	//		}
	//		ImmutableMap.Builder<String, ResourceLocation> textures = ImmutableMap.builder();
	//		ImmutableMap.Builder<String, ResourceLocation> multiTextures = ImmutableMap.builder();
	//		String particleLocate = null;
	//		if (object.has("include"))//Include textures.
	//		{
	//			JsonElement json1 = object.get("include");
	//			if (json1.isJsonObject())//Put more than one texture in to map.
	//			{
	//				JsonObject object1 = json1.getAsJsonObject();
	//				for (Entry<String, JsonElement> entry : object1.entrySet())
	//				{
	//					String value = entry.getValue().getAsString();
	//					if (PARTICLE.equals(entry.getKey()))
	//					{
	//						particleLocate = value;
	//						continue;
	//					}
	//					switch (value.charAt(0))
	//					{
	//					case '[' :
	//						ResourceLocation location1 = new ResourceLocation(value.substring(1));
	//						multiTextures.put(entry.getKey(), location1);
	//						break;
	//					default :
	//						textures.put(entry.getKey(), new ResourceLocation(value));
	//						break;
	//					}
	//				}
	//			}
	//			else//Put single texture or only one texture set into map.
	//			{
	//				String value = json1.getAsString();
	//				if(value.charAt(0) == '[')
	//				{
	//					ResourceLocation location1 = new ResourceLocation(value.substring(1));
	//					multiTextures.put(NORMAL, location1);
	//				}
	//				else
	//				{
	//					textures.put(NORMAL, new ResourceLocation(particleLocate = value));
	//				}
	//			}
	//		}
	//		cache.particle = particleLocate;
	//		cache.textures = textures.build();
	//		cache.multiTextures = multiTextures.build();
	//		if (object.has("layers"))
	//		{
	//			JsonElement json1 = object.get("layers");
	//			if (json1.isJsonArray())
	//			{
	//				JsonArray array = json1.getAsJsonArray();
	//				cache.layers = new FarCoreItemLayerUnbakedSimple[array.size()];
	//				for (int i = 0; i < array.size(); ++i)
	//				{
	//					FarCoreItemLayerUnbakedSimple layer = context.deserialize(array.get(i), FarCoreItemLayerUnbakedSimple.class);
	//					layer.layer = i;
	//					cache.layers[i] = layer;
	//				}
	//			}
	//			else if (json1.isJsonObject())
	//			{
	//				JsonObject object1 = json1.getAsJsonObject();
	//				int length = 0;
	//				List<FarCoreItemLayerUnbakedSimple> list = new ArrayList();
	//				while (object1.has("layer" + length))
	//				{
	//					JsonElement json2 = object1.get("layer" + length);
	//					FarCoreItemLayerUnbakedSimple layer = context.deserialize(json2, FarCoreItemLayerUnbakedSimple.class);
	//					layer.layer = length;
	//					list.add(length, layer);
	//					++length;
	//				}
	//				if (length == 0)
	//				{
	//					FarCoreItemLayerUnbakedSimple layer = context.deserialize(object1, FarCoreItemLayerUnbakedSimple.class);
	//					layer.layer = 0;
	//					cache.layers = new FarCoreItemLayerUnbakedSimple[]{ layer };
	//				}
	//				else
	//				{
	//					cache.layers = L.cast(list, FarCoreItemLayerUnbakedSimple.class);
	//				}
	//			}
	//			else
	//			{
	//				FarCoreItemLayerUnbakedSimple layer = context.deserialize(json1, FarCoreItemLayerUnbakedSimple.class);
	//				layer.layer = 0;
	//				cache.layers = new FarCoreItemLayerUnbakedSimple[]{ layer };
	//			}
	//		}
	//		else
	//		{
	//			FarCoreItemLayerUnbakedSimple layer = new FarCoreItemLayerUnbakedSimple();
	//			layer.layer = 0;
	//			layer.texturePool.add(NORMAL);
	//			cache.layers = new FarCoreItemLayerUnbakedSimple[]{ layer };
	//			if(!cache.textures.containsKey(NORMAL) && cache.multiTextures.isEmpty())
	//			{
	//				cache.textures.put(NORMAL, TextureMap.LOCATION_MISSING_TEXTURE);//If model doesn't contain any texture.
	//			}
	//		}
	//		return cache;
	//	};
	//
	//	static final Gson GSON = new GsonBuilder()
	//			.registerTypeAdapter(FarCoreItemLayerUnbakedSimple.class, MODEL_LAYER_DESERIALIZER)
	//			.registerTypeAdapter(ItemModelCache.class, ITEM_MODEL_CACHE_DESERIALIZER)
	//			.create();
	
	public static class ItemModelCache
	{
		//		ResourceLocation model;
		//		ImmutableMap<String, String> retextures;
		//
		//		Map<String, ResourceLocation> textures;
		//		Map<String, ResourceLocation> multiTextures;
		//
		//		String particle;
		//
		//		FarCoreItemLayerUnbakedSimple[] layers;
		//
		//		void registerItemColor(Item item, ItemColors colors)
		//		{
		//			for (FarCoreItemLayerUnbakedSimple layer : this.layers)
		//			{
		//				if (layer.colorMultiplier != FarCoreItemModelLoader.NORMAL_MULTIPLIER)
		//				{
		//					colors.registerItemColorHandler((ItemStack stack, int tintIndex) -> L.cast(this.layers[tintIndex].colorMultiplier.apply(stack)), item);
		//					return;
		//				}
		//			}
		//		}
	}
	
	/**
	 * A usable functional applier builder.
	 * @param location
	 * @param iterable
	 * @param function
	 */
	@Deprecated
	public static void
	registerMultiIconProvider(ResourceLocation location, Set<String> set, com.google.common.base.Function<String, ResourceLocation> function)
	{
		registerMultiIconProvider(location, () -> Maps.<String, ResourceLocation>asMap(set, function));
	}
	
	@Deprecated
	public static void
	registerMultiIconProvider(ResourceLocation location, Callable<Map<String, ResourceLocation>> function)
	{
		FarCoreTextureSet.registerTextureSetApplier(location, function);
		//		FarCoreItemTextureSetLoader.registerMultiIconProvider(location, function);
	}
	
	@Deprecated
	public static void
	registerSubmetaProvider(ResourceLocation location, Map<Integer, String> map, ItemBase item)
	{
		registerSubmetaProvider(location, stack -> map.getOrDefault(item.getBaseDamage(stack), "missing"));
	}
	
	@Deprecated
	public static void
	registerSubmetaProvider(ResourceLocation location, SubmetaGetter function)
	{
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(location, function);
		//		FarCoreSubMetaGetterLoader.registerSubmetaProvider(location, function);
	}
	
	@Deprecated
	public static void
	registerColorMultiplier(ResourceLocation location, ToIntFunction<ItemStack> function)
	{
		FarCoreColorMultiplier.registerColorMultiplier(location, function);
		//		colorMultipliers.put(location, function);
	}
	
	@Deprecated
	public static void
	registerModel(Item item, ResourceLocation location)
	{
		farcore.lib.model.item.FarCoreItemModelLoader.registerModel(item, location);
		//		ModelResourceLocation location1 = new ModelResourceLocation(item.getRegistryName(), "iventory");
		//		ModelLoader.setCustomMeshDefinition(item, (ItemStack stack) -> location1);
		//		ModelLoader.registerItemVariants(item, location1);
		//		location = new ResourceLocation(location.getResourceDomain(), "f_model/item/" + location.getResourcePath() + ".json");
		//		INSTANCE.acceptItem.put(item, location);
		//		INSTANCE.itemModelMap.put(location1, item);
	}
	
	//	private boolean isResourceLoading = false;
	//	private IResourceManager resourceManager;
	//
	//	private Map<ModelResourceLocation, Item> itemModelMap = new HashMap();
	//	private Map<Item, ResourceLocation> acceptItem = new HashMap();
	//
	//	private Map<Item, ItemModelCache> loadedModels = new HashMap();
	
	/**
	 * 
	 * @since 1.1
	 * @param location
	 * @return
	 */
	//	@Deprecated
	//	private void //Function<IResourceManager, Map<String, ResourceLocation>>
	//	loadTextureGetter(ResourceLocation location)
	//	{
	//		FarCoreItemTextureSetLoader.registerTextureSetsLoader(location);
	//	}
	
	/**
	 * 
	 * @since 1.1
	 * @param location
	 * @return
	 */
	//	@Deprecated
	//	Function<IResourceManager, Map<String, ResourceLocation>>
	//	loadTextures(ResourceLocation location)
	//	{
	//		return FarCoreItemTextureSetLoader.loadTextures(location);
	//	}
	
	/**
	 * 
	 * @since 1.1
	 * @param json
	 * @return
	 */
	//	@Deprecated
	//	private Function<ItemStack, String>
	//	loadSubmetaGetter(JsonElement json)
	//	{
	//		return FarCoreSubMetaGetterLoader.loadSubmetaGetter(this.resourceManager, json);
	//	}
	
	/**
	 * 
	 * @since 1.4
	 * @param location
	 * @return
	 */
	//	@Deprecated
	//	private Function<ItemStack, String>
	//	loadSubmetaGetter(ResourceLocation location)
	//	{
	//		return FarCoreSubMetaGetterLoader.loadSubmetaGetter(this.resourceManager, location);
	//	}
	
	/**
	 * 
	 * @since 1.2
	 * @param location
	 * @return
	 */
	//	@Deprecated
	//	private Function<ItemStack, Integer> loadColorMultiplier(ResourceLocation location)
	//	{
	//		return colorMultipliers.getOrDefault(location, NORMAL_MULTIPLIER);
	//	}
	//
	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		//		Log.reset();
		//		Log.info("Far Core Item Model Loader start model loading.");
		//		FarCoreItemTextureSetLoader.resetTextureSets();
		//		FarCoreSubMetaGetterLoader.resetFunctions();
		//		this.resourceManager = manager;
		//		this.isResourceLoading = true;
		//		this.loadedModels.clear();
		//		ProgressBar bar = ProgressManager.push("Loading FarCore Item Model", this.acceptItem.size());
		//		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		//		for(Entry<Item, ResourceLocation> entry : this.acceptItem.entrySet())
		//		{
		//			try
		//			{
		//				bar.step(entry.getValue().toString());
		//				ItemModelCache cache = loadItemModelCache(entry.getValue());
		//				if (colors != null)
		//				{
		//					cache.registerItemColor(entry.getKey(), colors);
		//				}
		//				this.loadedModels.put(entry.getKey(), cache);
		//			}
		//			catch (Exception exception)
		//			{
		//				Log.cache(exception);
		//			}
		//		}
		//		ProgressManager.pop(bar);
		//		Log.logCachedInformations(null, "Catching exceptions during loading models.");
		//		FarCoreItemTextureSetLoader.loadAllTetures(manager);
		//		this.isResourceLoading = false;
		//		FarCoreSubMetaGetterLoader.clearFunctions();
		//		FarCoreItemTextureSetLoader.clearTextureSets();
		//		Log.info("Far Core Item Model Loader finished model loading.");
	}
	
	//	private ItemModelCache loadItemModelCache(ResourceLocation location) throws Exception
	//	{
	//		byte[] codes;
	//		IResource resource;
	//		try
	//		{
	//			resource = this.resourceManager.getResource(location);
	//			codes = IOUtils.toByteArray(resource.getInputStream());
	//			resource.close();
	//		}
	//		catch (IOException exception)
	//		{
	//			throw new RuntimeException("Fail to load resource from {" + location.toString() + "}.", exception);
	//		}
	//		return GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(codes), Charsets.UTF_8), ItemModelCache.class);
	//	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return false;
		//		return this.itemModelMap.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		//		Item item = this.itemModelMap.get(modelLocation);
		//		if (this.loadedModels.containsKey(item))
		//		{
		//			ItemModelCache modelCache = this.loadedModels.get(item);
		//			if (modelCache.model != null)
		//			{
		//				IModel model = ModelLoaderRegistry.getModelOrMissing(modelCache.model);
		//				if (model instanceof IRetexturableModel)
		//				{
		//					IRetexturableModel retexturableModel = (IRetexturableModel) model;
		//					return retexturableModel.retexture(modelCache.retextures);
		//				}
		//				return model;
		//			}
		//			return new FarCoreItemModel(modelCache);
		//		}
		throw new RuntimeException(String.format("The model location {%s} is not belong to FarCoreItemModelLoader. There must be some wrong of other model loader.", modelLocation));
	}
	
	static
	{
		//		registerSubmetaProvider(new ResourceLocation("minecraft", "damage"), stack -> Integer.toString(stack.getItemDamage()));
		//		registerSubmetaProvider(new ResourceLocation("forge", "registry_name"), stack -> stack.getItem().getRegistryName().toString());
		//		registerSubmetaProvider(new ResourceLocation("forge", "contain_fluid"), stack ->
		//		{
		//			FluidStack stack1 = FluidUtil.getFluidContained(stack);
		//			return stack1 == null ? "fluid:empty" : "fluid:" + stack1.getFluid().getName();
		//		});
		//		registerSubmetaProvider(new ResourceLocation(FarCore.ID, "material"), stack -> "material:" + ItemMulti.getMaterial(stack).name);
		//		registerSubmetaProvider(new ResourceLocation(FarCore.ID, "display_fluid"), stack -> "fluid:" + ItemFluidDisplay.getFluid(stack).getName());
		//		registerMultiIconProvider(new ResourceLocation("forge", "fluid"), () ->
		//		{
		//			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		//			for (Entry<String, Fluid> fluid : FluidRegistry.getRegisteredFluids().entrySet())
		//			{
		//				builder.put("fluid:" + fluid.getKey(), fluid.getValue().getStill());
		//			}
		//			return builder.build();
		//		});
		//		for(MatCondition condition : MatCondition.register)
		//		{
		//			registerMultiIconProvider(new ResourceLocation(FarCore.ID, "group/" + condition.name), () ->
		//			{
		//				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
		//				for (Mat material : Mat.filt(condition))
		//				{
		//					builder.put("material:" + material.name, new ResourceLocation(material.modid, "items/group/" + condition.name + "/" + material.name));
		//				}
		//				return builder.build();
		//			});
		//		}
		//
		//		registerColorMultiplier(new ResourceLocation("minecraft", "armor"), stack -> ((ItemArmor) stack.getItem()).getColor(stack));
		//		registerColorMultiplier(new ResourceLocation("minecraft", "banner"), stack -> ItemBanner.getBaseColor(stack).getMapColor().colorValue);
		//		registerColorMultiplier(new ResourceLocation("forge", "fluid"), stack ->
		//		{
		//			FluidStack stack1 = FluidUtil.getFluidContained(stack);
		//			return stack1 == null ? 0xFFFFFFFF : stack1.getFluid().getColor(stack1);
		//		});
		//		registerColorMultiplier(new ResourceLocation(FarCore.ID, "material"), stack -> ItemMulti.getMaterial(stack).RGB);
		//		registerColorMultiplier(new ResourceLocation(FarCore.ID, "display_fluid"), stack -> ItemFluidDisplay.getFluid(stack).getColor());
	}
}