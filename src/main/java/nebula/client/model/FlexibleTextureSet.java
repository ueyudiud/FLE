/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import nebula.common.util.IO;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The texture set of item model.
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FlexibleTextureSet
{
	public static final Map<ResourceLocation, Callable<Map<String, ResourceLocation>>> TEXTURE_SET_APPLIERS = new HashMap();
	
	private static final Map<String, ResourceLocation> MISSING_TEXTURE_MAP = ImmutableMap.of(NebulaItemModelLoader.NORMAL, TextureMap.LOCATION_MISSING_TEXTURE);
	
	private static final Map<ResourceLocation, Map<String, ResourceLocation>> STORED_LOCATIONS = new HashMap();
	
	static final JsonDeserializer<FlexibleTextureSet> DESERIALIZER = (json, typeOfT, context) ->
	{
		FlexibleTextureSet set = new FlexibleTextureSet();
		if(json.isJsonObject())
		{
			for(Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
			{
				set.putTexture(entry.getKey(), entry.getValue().getAsString());
			}
		}
		else
		{
			set.putTexture(NebulaItemModelLoader.NORMAL, json.getAsString());
		}
		return set;
	};
	
	static void cleanCache()
	{
		STORED_LOCATIONS.clear();
	}
	
	public static void registerTextureSetApplier(ResourceLocation location, Callable<Map<String, ResourceLocation>> callable)
	{
		TEXTURE_SET_APPLIERS.put(location, callable);
	}
	
	public static Map<String, ResourceLocation> getTextureSet(ResourceLocation location) throws Exception
	{
		if(TEXTURE_SET_APPLIERS.containsKey(location))
		{
			return TEXTURE_SET_APPLIERS.get(location).call();
		}
		return getTextureSetFromResource(location);
	}
	
	public static Map<String, ResourceLocation> getTextureSetFromResource(ResourceLocation location) throws Exception
	{
		if(STORED_LOCATIONS.containsKey(location))
		{
			return STORED_LOCATIONS.get(location);
		}
		if(NebulaItemModelLoader.getResourceManaer() != null)
		{
			InputStream stream = new ByteArrayInputStream(IO.copyResource(NebulaItemModelLoader.getResourceManaer(), location));
			Map<String, ResourceLocation> map = NebulaItemModelLoader.GSON.fromJson(new InputStreamReader(stream), FlexibleTextureSet.class).buildTextureMap();
			STORED_LOCATIONS.put(location, map);
			return map;
		}
		throw new IllegalStateException("The resource manager is not loading resources!");
	}
	
	Map<String, TextureValue> textureSet = new HashMap();
	
	public void putTexture(String key, String value)
	{
		this.textureSet.put(key, new TextureValueSingle(value));
	}
	
	public void putTexture(String key, Map<String, String> value)
	{
		Map<String, ResourceLocation> list = Maps.transformEntries(value, (k, v)  -> new ResourceLocation(v));
		this.textureSet.put(key, new TextureValueSet(list));
	}
	
	public Map<String, ResourceLocation> buildVariantMap(String key)
	{
		try
		{
			if(key.charAt(0) == '#')
			{
				String substring = key.substring(1);
				Map<String, ResourceLocation> map = this.textureSet.get(substring).getRealTextureLocation();
				if(key.length() == 0)
					return map;
				if(map.size() == 1 && map.containsKey(NebulaItemModelLoader.NORMAL))
					return ImmutableMap.of(substring, map.get(NebulaItemModelLoader.NORMAL));
				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
				for(Entry<String, ResourceLocation> entry : map.entrySet())
				{
					builder.put(substring + "." + entry.getKey(), entry.getValue());
				}
				return builder.build();
			}
			return ImmutableMap.of(NebulaItemModelLoader.NORMAL, new ResourceLocation(key));
		}
		catch (Exception exception)
		{
			return MISSING_TEXTURE_MAP;
		}
	}
	
	public Map<String, ResourceLocation> findMap(String key)
	{
		try
		{
			return this.textureSet.get(key).getRealTextureLocation();
		}
		catch (Exception exception)
		{
			return MISSING_TEXTURE_MAP;
		}
	}
	
	public Map<String, ResourceLocation> buildTextureMap()
	{
		if(this.textureSet.size() == 1)
		{
			try
			{
				return this.textureSet.values().iterator().next().getRealTextureLocation();
			}
			catch (Exception exception)
			{
				return MISSING_TEXTURE_MAP;
			}
		}
		else
		{
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for(Entry<String, TextureValue> entry : this.textureSet.entrySet())
			{
				try
				{
					Map<String, ResourceLocation> map = entry.getValue().getRealTextureLocation();
					if(map.size() == 1)
					{
						builder.put(entry.getKey(), map.values().iterator().next());
					}
					else for(Entry<String, ResourceLocation> entry1 : map.entrySet())
					{
						builder.put(entry.getKey() + "." + entry1.getKey(), entry1.getValue());
					}
				}
				catch (Exception exception)
				{
					builder.put(entry.getKey(), TextureMap.LOCATION_MISSING_TEXTURE);
				}
			}
			return builder.build();
		}
	}
	
	private abstract class TextureValue
	{
		abstract Map<String, ResourceLocation> getRealTextureLocation() throws Exception;
	}
	
	private class TextureValueSet extends TextureValue
	{
		String key;
		Map<String, ResourceLocation> locations;
		
		TextureValueSet(String key)
		{
			this.key = key;
		}
		TextureValueSet(Map<String, ResourceLocation> locations)
		{
			this.locations = locations;
		}
		
		@Override
		Map<String, ResourceLocation> getRealTextureLocation() throws Exception
		{
			return this.locations == null ? FlexibleTextureSet.this.textureSet.get(this.key).getRealTextureLocation() : this.locations;
		}
	}
	
	private class TextureValueSingle extends TextureValue
	{
		String key;
		
		TextureValueSingle(String key)
		{
			if(key.indexOf('.') != -1)
				throw new IllegalArgumentException("The '.' can not contain in texture location!");
			this.key = key;
		}
		
		@Override
		Map<String, ResourceLocation> getRealTextureLocation() throws Exception
		{
			switch (this.key.charAt(0))
			{
			case '[' :
				String value = this.key.substring(1);
				return FlexibleTextureSet.this.textureSet.containsKey(this.key) ? FlexibleTextureSet.this.textureSet.get(value).getRealTextureLocation() :
					FlexibleTextureSet.getTextureSet(new ResourceLocation(value));
			case '#' :
				return FlexibleTextureSet.this.textureSet.containsKey(this.key.substring(1)) ? FlexibleTextureSet.this.textureSet.get(this.key.substring(1)).getRealTextureLocation() : MISSING_TEXTURE_MAP;
			default :
				return ImmutableMap.of(NebulaItemModelLoader.NORMAL, new ResourceLocation(this.key));
			}
		}
	}
}