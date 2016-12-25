/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.lib.util.Log;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public final class FarCoreItemTextureSetLoader
{
	private static final Map<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> MULTI_ICON_LOADERS = new HashMap();
	
	private static Map<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> markedTextureSets;
	private static Map<ResourceLocation, Map<String, ResourceLocation>> storedTextureSets;
	
	static void registerTextureSetsLoader(ResourceLocation location)
	{
		if(!markedTextureSets.containsKey(location))
		{
			if(MULTI_ICON_LOADERS.containsKey(location))
			{
				markedTextureSets.put(location, MULTI_ICON_LOADERS.get(location));
			}
			else
			{
				markedTextureSets.put(location, loadTextures(location));
			}
		}
	}
	
	/**
	 * 
	 * @since 1.1
	 * @param location
	 * @return
	 */
	static Function<IResourceManager, Map<String, ResourceLocation>> loadTextures(final ResourceLocation location)
	{
		return manager ->
		{
			//Create file in far texture map file.
			//Location is f_tm/[your path].json
			String l = "f_tm/" + location.getResourcePath();
			if(!l.endsWith(".json"))
			{
				l += ".json";
			}
			IMultiTextureCollection collection;
			ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), l);
			try
			{
				IResource resource = manager.getResource(location1);
				byte[] code = IOUtils.toByteArray(resource.getInputStream());
				resource.close();
				Reader reader = new InputStreamReader(new ByteArrayInputStream(code));
				collection = FarCoreItemModelLoader.GSON.fromJson(reader, IMultiTextureCollection.class);
			}
			catch (JsonParseException exception)
			{
				Log.cache(exception);
				return ImmutableMap.of();
			}
			catch (IOException exception)
			{
				Log.cache(exception);
				return ImmutableMap.of();
			}
			return collection.apply();
		};
	}
	
	static void loadAllTetures(IResourceManager manager)
	{
		ProgressBar bar = ProgressManager.push("Collect FarCore Textures", markedTextureSets.size());
		for (Entry<ResourceLocation, Function<IResourceManager, Map<String, ResourceLocation>>> entry : markedTextureSets.entrySet())
		{
			bar.step(entry.getKey().toString());
			try
			{
				storedTextureSets.put(entry.getKey(), entry.getValue().apply(manager));
			}
			catch (Exception exception)
			{
				Log.cache(entry.getKey());
			}
		}
		Log.logCachedInformations((Object object) -> ((ResourceLocation) object).toString(), "Failed to load texture collection from these path.");
		ProgressManager.pop(bar);
	}
	
	static void resetTextureSets()
	{
		markedTextureSets = new HashMap();
		storedTextureSets = new HashMap();
	}
	
	static void clearTextureSets()
	{
		markedTextureSets = null;
	}
	
	/**
	 * Get stored texture set.
	 * @param location
	 * @return
	 * @since 1.5
	 */
	static Map<String, ResourceLocation> getStoredTextureSet(ResourceLocation location)
	{
		if(storedTextureSets == null)
			throw new RuntimeException("The texture set map is not been built now.");
		return storedTextureSets.getOrDefault(location, ImmutableMap.of());
	}
	
	/**
	 * A usable functional applier builder.
	 * @param location
	 * @param iterable
	 * @param function
	 */
	public static void registerMultiIconProvider(ResourceLocation location, Set<String> set, com.google.common.base.Function<String, ResourceLocation> function)
	{
		registerMultiIconProvider(location, manager -> Maps.<String, ResourceLocation>asMap(set, function));
	}
	
	public static void registerMultiIconProvider(ResourceLocation location, Function<IResourceManager, Map<String, ResourceLocation>> function)
	{
		MULTI_ICON_LOADERS.put(location, function);
	}
	
	public static Function<IResourceManager, Map<String, ResourceLocation>> getFromJson(JsonElement json)
	{
		if (!json.isJsonObject())
			throw new JsonParseException("The json should be an object!");
		JsonObject object = json.getAsJsonObject();
		if (object.has("parent"))
		{
			FarCoreMultiTextureModifier function = new FarCoreMultiTextureModifier(loadTextures(new ResourceLocation(object.get("parent").getAsString())));
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
	}
}