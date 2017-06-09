/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import nebula.Log;
import nebula.base.Ety;
import nebula.client.model.IStateMapperExt;
import nebula.client.model.flexible.NebulaModelDeserializer.Transform;
import nebula.client.util.IIconCollection;
import nebula.common.data.Misc;
import nebula.common.item.ItemFluidDisplay;
import nebula.common.util.IO;
import nebula.common.util.Jsons;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Nebula Model Loader
 * (Before: Nebula Item Model Loader & Nebula Block ModelLoader),
 * provide more flexible model loader for creator,
 * make more convenient model
 * file writing rules for resource pack maker.<p>
 * @author ueyudiud
 * @version 0.3
 */
public enum NebulaModelLoader implements ICustomModelLoader
{
	INSTANCE;
	
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	/**
	 * The default variant or key for model,
	 * use to load model with only single meta or model is missing
	 * a texture set.<br>
	 */
	public static final String NORMAL = "";
	/**
	 * The normal meta generator, it will return default variant.
	 */
	public static final Function<?, String> NORMAL_METAGENERATOR = Misc.anyTo(NORMAL);
	/**
	 * The default color (RGBa) in model quad.<br>
	 */
	public static final int NORMAL_COLOR = 0xFFFFFFFF;
	/**
	 * The normal color get function used to get color multiple, it will return default color.
	 */
	public static final ToIntFunction<?> NORMAL_MULTIPLIER = any->NORMAL_COLOR;
	/**
	 * The missing no icon, it always return when NebulaModelLoader can not find icon handler by resource location.
	 */
	public static final IIconCollection ICON_HANDLER_MISSING = new MissingnoIconHandler();
	
	private static final Map<ResourceLocation, Entry<ResourceLocation, JsonDeserializer<? extends IModel>>> MODEL_PROVIDERS = new HashMap<>();
	private static final Map<ResourceLocation, JsonDeserializer<? extends IModel>> MODEL_DESERIALIZERS = new HashMap<>();
	private static final Map<ResourceLocation, Function<ItemStack, String>> ITEM_META_GENERATOR = new HashMap<>();
	private static final Map<ResourceLocation, Function<IBlockState, String>> BLOCK_META_GENERATOR = new HashMap<>();
	private static final Map<ResourceLocation, ToIntFunction<IBlockState>> BUILTIN_BLOCK_COLORMULTIPLIER = new HashMap<>();
	private static final Map<ResourceLocation, ToIntFunction<ItemStack>> BUILTIN_ITEM_COLORMULTIPLIER = new HashMap<>();
	private static final Map<ResourceLocation, Supplier<Map<String, ResourceLocation>>> BUILTIN_TEXTURESET = new HashMap<>();
	private static final Map<ResourceLocation, Item> LOCATION_TO_ITEM_MAP = new HashMap<>();
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(SubmetaLoader.BlockSubmetaGetter.class, SubmetaLoader.BLOCK_LOADER)
			.registerTypeAdapter(SubmetaLoader.ItemSubmetaGetter.class, SubmetaLoader.ITEM_LOADER)
			.registerTypeAdapter(INebulaModelPart.class, NebulaModelDeserializer.BLOCK_MODEL_PART_DESERIALIZERS)
			.registerTypeAdapter(ModelPartCollection.class, ModelPartCollection.DESERIALIZER)
			.registerTypeAdapter(NebulaModelDeserializer.Transform.class, (JsonDeserializer<Transform>) (json, typeOfT, context)-> {
				Transform transform = new Transform();
				//TODO
				return transform;
			})
			.registerTypeAdapter(IModel.class, (JsonDeserializer<IModel>)
					(json, type, context)-> {
						String key = Jsons.getOrDefault(json.getAsJsonObject(), "loader", null);
						JsonDeserializer<? extends IModel> deserializer = INSTANCE.defaultDeserializer;
						if (key != null)
						{
							deserializer = MODEL_DESERIALIZERS.get(new ResourceLocation(key));
						}
						if (deserializer == null)
							if (INSTANCE.defaultDeserializer == null)
								throw new JsonParseException("No deserializer found.");
							else
							{
								deserializer = INSTANCE.defaultDeserializer;
								Log.debug("The deserializer \"{}\" not found, use default deserializer instead.", key);
							}
						return deserializer.deserialize(json, type, context);
					})
			.create();
	
	private static final Function<Object, String> ERROR_REPORT_FUNCTION = object ->
	object instanceof ResourceLocation ? String.format("File not found : %s/%s", ((ResourceLocation) object).getResourceDomain(), ((ResourceLocation) object).getResourcePath()) : "";
	
	/**
	 * Register model deserializer.
	 * @param location
	 * @param deserializer
	 */
	public static void registerDeserializer(ResourceLocation location, JsonDeserializer<? extends IModel> deserializer)
	{
		MODEL_DESERIALIZERS.put(location, deserializer);
	}
	
	/**
	 * Mark this model should loaded by NebulaModelLoaer.<br>
	 * This method should called before <code>FMLInitializationEvent</code>
	 * @param item The marked item.
	 * @param location The loading location.
	 */
	public static void registerModel(Item item, ResourceLocation location)
	{
		//The default model location of item.
		ModelResourceLocation location1 = new ModelResourceLocation(item.getRegistryName(), "inventory");
		//For stack to location logic, it will only return single model location, the variant will be match in model.
		ModelLoader.setCustomMeshDefinition(item, stack->location1);
		//Register allowed build item variant.
		ModelLoader.registerItemVariants(item, location1);
		//The real location of model.
		location = new ResourceLocation(location.getResourceDomain(), "models/item/" + location.getResourcePath() + ".json");
		
		LOCATION_TO_ITEM_MAP.put(location1, item);
		registerModel(location1, location, NebulaModelDeserializer.ITEM);
	}
	
	public static void registerModel(Block block, IStateMapperExt mapper, IBlockState state, ResourceLocation location)
	{
		if (block != null)
			LOCATION_TO_ITEM_MAP.put(location, Item.getItemFromBlock(block));//Is it needed split block and item map?
		registerModel(mapper.getLocationFromState(state), new ResourceLocation(location.getResourceDomain(), "models/block1/" + location.getResourcePath() + ".json"), NebulaModelDeserializer.BLOCK);
	}
	
	public static void registerModel(
			ResourceLocation location, ResourceLocation location2)
	{
		registerModel(location, location2, null);
	}
	
	/**
	 * Mark this model should loaded by NebulaModelLoader.
	 * @param location The model loader predicated
	 * @param location2
	 * @param deserializer
	 */
	public static void registerModel(
			ResourceLocation location, ResourceLocation location2,
			@Nullable JsonDeserializer<? extends IModel> deserializer)
	{
		MODEL_PROVIDERS.put(location, new Ety<>(location2, deserializer));
	}
	
	public static void registerItemMetaGenerator(ResourceLocation location,
			Function<ItemStack, String> function)
	{
		ITEM_META_GENERATOR.put(location, function);
	}
	
	public static void registerBlockMetaGenerator(ResourceLocation location,
			Function<IBlockState, String> function)
	{
		BLOCK_META_GENERATOR.put(location, function);
	}
	
	public static void registerBlockColorMultiplier(ResourceLocation location, ToIntFunction<IBlockState> colorMultiplier)
	{
		BUILTIN_BLOCK_COLORMULTIPLIER.put(location, colorMultiplier);
	}
	
	public static void registerItemColorMultiplier(ResourceLocation location, ToIntFunction<ItemStack> colorMultiplier)
	{
		BUILTIN_ITEM_COLORMULTIPLIER.put(location, colorMultiplier);
	}
	
	public static void registerTextureSet(ResourceLocation location, Supplier<Map<String, ResourceLocation>> map)
	{
		BUILTIN_TEXTURESET.put(location, map);
	}
	
	public IResourceManager manager;
	public ResourceLocation currentLocation;
	public Item currentItem;
	
	private JsonDeserializer<? extends IModel> defaultDeserializer;
	
	private Map<ResourceLocation, Map<String, ResourceLocation>> cacheLocations;
	private Map<ResourceLocation, IModel> models;
	private Map<ResourceLocation, ModelPartCollection> parts;
	
	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		Log.info("Nebula Model Loader start model loading.");
		this.manager = manager;
		Log.info("Clean caches.");
		this.models = new HashMap<>();
		this.cacheLocations = new HashMap<>();
		this.parts = new HashMap<>();
		ProgressBar bar = ProgressManager.push("Loading Nebula Model", MODEL_PROVIDERS.size());
		BlockColors colors1 = Minecraft.getMinecraft().getBlockColors();
		ItemColors colors2 = Minecraft.getMinecraft().getItemColors();
		for (Entry<ResourceLocation, Entry<ResourceLocation, JsonDeserializer<? extends IModel>>> entry : MODEL_PROVIDERS.entrySet())
		{
			try
			{
				bar.step(entry.getValue().getKey().toString());
				this.defaultDeserializer = entry.getValue().getValue();
				this.currentLocation = entry.getValue().getKey();
				this.currentItem = LOCATION_TO_ITEM_MAP.get(entry.getKey());
				byte[] code = IO.copyResource(manager, entry.getValue().getKey());
				IModel cache = GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(code)), IModel.class);
				this.models.put(entry.getKey(), cache);
				if(cache instanceof IRecolorableModel && colors1 != null && colors2 != null)
				{
					((IRecolorableModel) cache).registerColorMultiplier(colors1);
					((IRecolorableModel) cache).registerColorMultiplier(colors2);
				}
				this.currentItem = null;
				this.currentLocation = null;
				this.defaultDeserializer = null;
			}
			catch (FileNotFoundException exception)
			{
				Log.cache(new ResourceLocation(exception.getMessage()));
			}
			catch (Exception exception)
			{
				Log.cache(new RuntimeException("Fail to load model of name \"" + entry.getKey() + "\"", exception));
			}
		}
		ProgressManager.pop(bar);
		Log.logCachedInformations(ERROR_REPORT_FUNCTION, "Catching exceptions during loading models.");
		Log.info("Nebula Model Loader finished model loading.");
	}
	
	@SubscribeEvent
	public void onModelBaked(ModelBakeEvent event)
	{
		for (IModel model : this.models.values())
		{
			if (model instanceof FlexibleModel)
			{
				((FlexibleModel) model).loadResources();
			}
		}
		this.parts = null;
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		if (modelLocation instanceof ModelResourceLocation)
		{
			ResourceLocation location = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
			if (this.models.containsKey(location))
				return true;
			return this.models.containsKey(modelLocation);//TODO
		}
		else return this.models.containsKey(modelLocation);
	}
	
	/**
	 * Get meta applier.
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")//It is safe.
	public static Function<ItemStack, String> loadItemMetaGenerator(String location)
	{
		return ITEM_META_GENERATOR.getOrDefault(new ResourceLocation(location), (Function<ItemStack, String>) NORMAL_METAGENERATOR);
	}
	
	/**
	 * Get icon/iconset by location.
	 * @param location
	 * @return
	 */
	public static IIconCollection loadIconHandler(String location)
	{
		//TODO
		if (location.charAt(0) == '[')
			return new TemplateIconHandler(getTextureSet(new ResourceLocation(location.substring(1))));
		return new SimpleIconHandler(NORMAL, new ResourceLocation(location));//Default loader instead.
	}
	
	/**
	 * Get color multiplier use for item model.
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ToIntFunction<ItemStack> loadItemColorMultiplier(String location)
	{
		return BUILTIN_ITEM_COLORMULTIPLIER.getOrDefault(new ResourceLocation(location), (ToIntFunction<ItemStack>) NORMAL_MULTIPLIER);
	}
	
	/**
	 * Get texture set, which is used for {@link IIconCollection}.<p>
	 * The loader will find resources at {@code "[domain]:models/textureset/[path]"}.
	 * The file is format as {@code key=value}, and use {@code #} to mark a line of annotation.<p>
	 * The loader will find in inner resources supplier when not found any mapping in resources,
	 * and will throw an exception of file not found when no textures set found.
	 * @param location
	 * @return
	 */
	public static Map<String, ResourceLocation> getTextureSet(ResourceLocation location)
	{
		return INSTANCE.cacheLocations.computeIfAbsent(location, INSTANCE::loadTextureSet);
	}
	
	private Map<String, ResourceLocation> loadTextureSet(ResourceLocation location)
	{
		ResourceLocation location2 = location;
		location = new ResourceLocation(location.getResourceDomain(), "models/textureset/" + location.getResourcePath() + ".txt");
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(IO.copyResource(this.manager, location))));
			String key;
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			while ((key = reader.readLine()) != null)
			{
				if (key.length() == 0 || key.charAt(0) == '#')//For annotate.
					continue;
				int idx;
				if ((idx = key.indexOf('=')) == -1)
					throw new RuntimeException("\"" + key + "\" is missing a '=' for key pair.");
				builder.put(key.substring(0, idx), new ResourceLocation(key.substring(idx + 1)));
			}
			return builder.build();
		}
		catch (IOException e)
		{
			Supplier<Map<String, ResourceLocation>> supplier = BUILTIN_TEXTURESET.get(location2);
			if (supplier != null) return supplier.get();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get model from model loader (For the nebula model loader will remove provided model).
	 * @param location
	 * @return
	 */
	public static IModel getModel(ResourceLocation location)
	{
		IModel model = INSTANCE.models.get(location);
		return (model == null) ? ModelLoaderRegistry.getModelOrMissing(location) : model;
	}
	
	public static INebulaModelPart getModelPart(String location)
	{
		ModelResourceLocation location2 = new ModelResourceLocation(location);
		return INSTANCE.parts.computeIfAbsent(new ResourceLocation(location2.getResourceDomain(), location2.getResourcePath()), INSTANCE::loadModelPart).getModelPart(location2.getVariant());
	}
	
	public ModelPartCollection loadModelPart(ResourceLocation location)
	{
		location = new ResourceLocation(location.getResourceDomain(), "models/parts/" + location.getResourcePath() + ".json");
		try (IResource resource = this.manager.getResource(location))
		{
			return GSON.fromJson(new InputStreamReader(resource.getInputStream()), ModelPartCollection.class);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Model part " + location + " not found.");
		}
	}
	
	/**
	 * Nebula model will be remove when loading model,
	 * it means this can not be parent model directly,
	 * or use {@link #getModel(net.minecraft.util.ResourceLocation)} instead.
	 * @return The loaded model.
	 */
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		IModel model = this.models.remove(modelLocation);
		if (this.models.isEmpty())
			this.models = null;//Clean cache.
		if (model == null)//If model is null, it means the loader of location shoudn't be this loader.
			throw new RuntimeException(
					String.format("The model location \"%s\" is not belong to NebulaModelLoader. There must be some wrong of other model loader.", modelLocation));
		return model;
	}
	
	static
	{
		registerItemMetaGenerator(new ResourceLocation("forge", "contain_fluid"), stack ->
		{
			FluidStack stack1 = FluidUtil.getFluidContained(stack);
			return stack1 == null ? "empty" : stack1.getFluid().getName();
		});
		registerItemMetaGenerator(new ResourceLocation("nebula", "display_fluid"),
				stack->ItemFluidDisplay.getFluid(stack).getName());
		
		registerTextureSet(new ResourceLocation("forge", "fluid"), ()-> {
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for (Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet())
			{
				builder.put(entry.getKey(), entry.getValue().getStill());
			}
			return builder.build();
		});
	}
}