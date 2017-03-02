/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import nebula.Log;
import nebula.common.util.Strings;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class FlexiableStateMap
{
	public static final JsonDeserializer<FlexiableStateMap> DESERIALIZER1 = (json, typeOfT, context) -> {
		JsonObject object = json.getAsJsonObject();
		FlexiableStateMap map = new FlexiableStateMap();
		if (object.has("default"))
		{
			map.defaults = context.deserialize(object.get("default"), FlexiableStateMapEntry.class);
		}
		return map;
	};
	public static final JsonDeserializer<FlexiableStateMapEntry> DESERIALIZER2 = (json, typeOfT, context) -> {
		if (json.isJsonObject())
		{
			
		}
		else
		{
			
		}
		return null;
	};
	
	public FlexiableStateMap defaults;
	public Map<String, FlexiableStateMapEntry> variants;
	
	public static class FlexiableStateMapEntry
	{
		FlexiableStateMapEntry parent;
		Map<String, Object> textures;
		Map<String, Object> properties;
		
		public <T> Optional<T> get(String key, T def)
		{
			try
			{
				return Optional.ofNullable((T) this.properties.getOrDefault(key, def));
			}
			catch (Exception exception)
			{
				Log.warn("The {} is missing {} as property.", this.parent, key);
				return Optional.empty();
			}
		}
		
		public Optional<String> getString(String key)
		{
			return get(key, null);
		}
		
		public Optional<Integer> getInt(String key, int def)
		{
			return get(key, def);
		}
		
		public Optional<Integer> getInt(String key)
		{
			return get(key, null);
		}
		
		public Optional<Boolean> getBoolean(String key, boolean def)
		{
			return get(key, def);
		}
		
		public Optional<Boolean> getBoolean(String key)
		{
			return get(key, null);
		}
		
		public ResourceLocation getTextureLocation(String key)
		{
			return key == null ? TextureMap.LOCATION_MISSING_TEXTURE : getTextureLocation$("#" + key);
		}
		
		private ResourceLocation getTextureLocation$(String key)
		{
			if (key.charAt(0) == '#')
			{
				key = key.substring(1);
				String[] value = Strings.split(key, '|');
				if (value.length > 2)
					return TextureMap.LOCATION_MISSING_TEXTURE;
				if (value.length == 1)
				{
					value = new String[]{value[0], null};
				}
				if (this.textures != null)
				{
					Object function = this.textures.get(value[0]);
					if (function instanceof Function)
					{
						return ((Function<String, ResourceLocation>) function).apply(value[1]);
					}
					else if (function instanceof String)
					{
						return getTextureLocation$((String) function);
					}
					else if (function instanceof ResourceLocation)
					{
						return (ResourceLocation) function;
					}
					else if (function instanceof Map)
					{
						return ((Map<String, ResourceLocation>) function).getOrDefault(key, TextureMap.LOCATION_MISSING_TEXTURE);
					}
					else return TextureMap.LOCATION_MISSING_TEXTURE;
				}
				if (this.parent != null)
				{
					return this.parent.getTextureLocation$("#" + key);
				}
				else return TextureMap.LOCATION_MISSING_TEXTURE;
			}
			else
				return new ResourceLocation(key);
		}
	}
}