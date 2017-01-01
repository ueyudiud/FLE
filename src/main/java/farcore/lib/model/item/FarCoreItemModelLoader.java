/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import farcore.FarCore;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemFluidDisplay;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.model.item.FarCoreItemSubmetaGetterLoader.SubmetaGetter;
import farcore.lib.util.Log;
import farcore.util.IO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
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

/**
 * Far Core Item Model Loader, provide more flexible
 * model loader for creator, make more convenient model
 * file writing rules for resource pack maker.<p>
 * 
 * The Far Core model use layer for cover, every layer is
 * independent from other layers. You can set different
 * texture selector, color multiplier, etc for each layer.
 * This is like icon get in the item class before 1.7.10.<p>
 * 
 * The texture selector can use JavaScript to write down,
 * but player need add <code>nashorn</code> by manual for it
 * might not included in runtime environment. (I don't exactly
 * know about this.)
 * The model loader also support to register a hard-coded
 * selector, here are some instance register in loader.<p>
 * 
 * The color multiplier is use to get different color by data
 * current in item stacks. It will take effects by <code>IItemColor</code>.<p>
 * 
 * This loader also access to load model layer with covert icon.
 * (Like layer of dyn-bucket model added by Forge)<p>
 * 
 * @author ueyudiud
 * @version 1.5
 */
@SideOnly(Side.CLIENT)
public enum FarCoreItemModelLoader implements ICustomModelLoader
{
	/**
	 * The loader instance.
	 */
	INSTANCE;
	
	/**
	 * The default variant or key for model,
	 * use to load model with only single meta or model is missing
	 * a texture set.<br>
	 */
	public static final String NORMAL = "";
	/**
	 * The particle key for model, use to get particle location.<br>
	 */
	public static final String PARTICLE = "particle";
	/**
	 * The default color (RGBa) in model quad.<br>
	 */
	public static final int NORMAL_COLOR = 0xFFFFFFFF;
	/**
	 * The normal texture key get function, return default key as result.<br>
	 * The target model layer is suggested only contain single texture.
	 */
	public static final SubmetaGetter NORMAL_FUNCTION = stack -> NORMAL;
	/**
	 * The normal color get function used to get color multiple, it will return default color.
	 */
	public static final ToIntFunction<ItemStack> NORMAL_MULTIPLIER = stack -> NORMAL_COLOR;
	/**
	 * The cached access loaded item map (Item load by FarCoreItemModelLoader -> ResourceLocation of model)).
	 */
	private static final Map<Item, ResourceLocation> ACCEPT_ITEMS = new HashMap();
	private static final Map<ResourceLocation, Item> MODELLOCATION_TO_ITEM = new HashMap();
	private static Map<Item, FarCoreItemModelCache> cache;
	
	private static IResourceManager manager;
	
	static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(FarCoreItemModelCache.class, FarCoreItemModelCache.DESERIALIZER_1)
			.registerTypeAdapter(FarCoreItemModelCache.FarCoreItemModelLayerCache.class, FarCoreItemModelCache.DESERIALIZER_2)
			.registerTypeAdapter(FarCoreTextureSet.class, FarCoreTextureSet.DESERIALIZER)
			.registerTypeAdapter(SubmetaGetter.class, FarCoreItemSubmetaGetterLoader.DESERIALIZER)
			.create();
	
	static IResourceManager getResourceManaer()
	{
		return manager;
	}
	
	/**
	 * Marked this item model should loaded by FarCoreItemModelLoaer.<br>
	 * This method should called before <code>FMLInitializationEvent</code>
	 * @param item The marked item.
	 * @param location The loading location.
	 */
	public static void registerModel(Item item, ResourceLocation location)
	{
		//The default model location of item.
		ModelResourceLocation location1 = new ModelResourceLocation(item.getRegistryName(), "iventory");
		//For stack to location logic, it will only return single model location, the variant will be match in model.
		ModelLoader.setCustomMeshDefinition(item, stack -> location1);
		//Register allowed build item variant.
		ModelLoader.registerItemVariants(item, location1);
		//The real location of model.
		location = new ResourceLocation(location.getResourceDomain(), "models/item/" + location.getResourcePath() + ".json");
		
		ACCEPT_ITEMS.put(item, location);
		MODELLOCATION_TO_ITEM.put(location1, item);
	}
	
	static final Function<Object, String> ERROR_REPORT_FUNCTION = object ->
	object instanceof ResourceLocation ? String.format("File not found : %s/%s", ((ResourceLocation) object).getResourceDomain(), ((ResourceLocation) object).getResourcePath()) : "";
	
	/**
	 * Called when resource manager reload.<br>
	 * To re-register models and reload texture set in this phase.
	 */
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		this.manager = resourceManager;
		this.cache = new HashMap();
		Log.info("Far Core Item Model Loader start model loading.");
		Log.info("Clean caches.");
		FarCoreItemSubmetaGetterLoader.cleanCache();
		FarCoreColorMultiplier.cleanCache();
		FarCoreTextureSet.cleanCache();
		ProgressBar bar = ProgressManager.push("Loading FarCore Item Model", this.ACCEPT_ITEMS.size());
		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		for(Entry<Item, ResourceLocation> entry : this.ACCEPT_ITEMS.entrySet())
		{
			try
			{
				bar.step(entry.getValue().toString());
				byte[] code = IO.copyResource(resourceManager, entry.getValue());
				FarCoreItemModelCache cache = GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(code)), FarCoreItemModelCache.class);
				this.cache.put(entry.getKey(), cache);
				if(colors != null)
				{
					IItemColor color = FarCoreColorMultiplier.createColorMultiplier(cache);
					if(color != null)
					{
						colors.registerItemColorHandler(color, entry.getKey());
					}
				}
			}
			catch (FileNotFoundException exception)
			{
				Log.cache(new ResourceLocation(exception.getMessage()));
			}
			catch (Exception exception)
			{
				Log.cache(exception);
			}
		}
		ProgressManager.pop(bar);
		Log.logCachedInformations(ERROR_REPORT_FUNCTION, "Catching exceptions during loading models.");
		Log.info("Far Core Item Model Loader finished model loading.");
	}
	
	/**
	 * Match location is marked in loader.
	 * @param modelLocation
	 * @return True for loader can load this location.
	 */
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return MODELLOCATION_TO_ITEM.containsKey(modelLocation);
	}
	
	/**
	 * Find model in map by target location (Already build when reloading
	 * resource manager).
	 * @param modelLocation
	 * @return The built model.
	 */
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		Item item = MODELLOCATION_TO_ITEM.get(modelLocation);
		if(item == null)//If item is null, it means the loader of location shoudn't be this loader.
			throw new RuntimeException(String.format("The model location {%s} is not belong to FarCoreItemModelLoader. There must be some wrong of other model loader.", modelLocation));
		if(!cache.containsKey(item)) return ModelLoaderRegistry.getMissingModel();
		return new FarCoreItemModelUnbaked(cache.get(item));
	}
	
	static
	{
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation("minecraft", "damage"), stack -> Integer.toString(stack.getItemDamage()));
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation("forge", "registry_name"), stack -> stack.getItem().getRegistryName().toString());
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation("forge", "contain_fluid"), stack ->
		{
			FluidStack stack1 = FluidUtil.getFluidContained(stack);
			return stack1 == null ? "fluid:empty" : "fluid:" + stack1.getFluid().getName();
		});
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation(FarCore.ID, "material"), stack -> "material:" + ItemMulti.getMaterial(stack).name);
		FarCoreItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation(FarCore.ID, "display_fluid"), stack -> "fluid:" + ItemFluidDisplay.getFluid(stack).getName());
		FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation("forge", "fluid"), () ->
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
			FarCoreTextureSet.registerTextureSetApplier(new ResourceLocation(FarCore.ID, "group/" + condition.name), () ->
			{
				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
				for (Mat material : Mat.filt(condition))
				{
					builder.put("material:" + material.name, new ResourceLocation(material.modid, "items/group/" + condition.name + "/" + material.name));
				}
				return builder.build();
			});
		}
		
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation("minecraft", "armor"), stack -> ((ItemArmor) stack.getItem()).getColor(stack));
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation("minecraft", "banner"), stack -> ItemBanner.getBaseColor(stack).getMapColor().colorValue);
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation("forge", "fluid"), stack ->
		{
			FluidStack stack1 = FluidUtil.getFluidContained(stack);
			return stack1 == null ? 0xFFFFFFFF : stack1.getFluid().getColor(stack1);
		});
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation(FarCore.ID, "material"), stack -> ItemMulti.getMaterial(stack).RGB);
		FarCoreColorMultiplier.registerColorMultiplier(new ResourceLocation(FarCore.ID, "display_fluid"), stack -> ItemFluidDisplay.getFluid(stack).getColor());
	}
}