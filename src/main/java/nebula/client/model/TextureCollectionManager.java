/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 2.0
 */
@SideOnly(Side.CLIENT)
public class TextureCollectionManager
{
	private static final Map<String, ResourceLocation>							MISSING					= ImmutableMap.of("missing", TextureMap.LOCATION_MISSING_TEXTURE);
	private static final Map<String, Supplier<Map<String, ResourceLocation>>>	BUILT_IN_TEXTURE_SET	= new HashMap<>(16, 1.0F);
	
	static IResourceManager								resourceManager;
	static Map<String, Map<String, ResourceLocation>>	cache;
	static Map<String, Object>							loadedTextureCols;
	
	static void reload(IResourceManager manager)
	{
		resourceManager = manager;
		cache = new HashMap<>();
		loadedTextureCols = new HashMap<>(Math.max(BUILT_IN_TEXTURE_SET.size() + 4, 16), 1.0F);
		loadedTextureCols.putAll(BUILT_IN_TEXTURE_SET);
	}
	
	public static void registerTextureSupplier(ResourceLocation location, Supplier<Map<String, ResourceLocation>> supplier)
	{
		if (BUILT_IN_TEXTURE_SET.containsKey(location.toString())) throw new RuntimeException("Built in texture set name " + location + " already exist!");
		BUILT_IN_TEXTURE_SET.put(location.toString(), supplier);
	}
	
	public static Map<String, ResourceLocation> getResourceLocationCache(String key)
	{
		if (cache != null)
		{
			if (cache.containsKey(key)) return cache.get(key);
		}
		Object object = loadedTextureCols.get(key);
		if (object instanceof Supplier)
		{
			Map<String, ResourceLocation> map = ((Supplier<Map<String, ResourceLocation>>) object).get();
			cache.put(key, map);
			return map;
		}
		else if (object instanceof Map)
		{
			return (Map<String, ResourceLocation>) object;
		}
		else if (object instanceof String)
		{
			Map<String, ResourceLocation> map = ImmutableMap.of("all", new ResourceLocation((String) object));
			cache.put(key, map);
			return map;
		}
		else
		{
			return MISSING;
		}
	}
}
