/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import nebula.client.util.IIconCollection;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class TemplateIconHandler implements IIconCollection
{
	final Map<String, ResourceLocation>	textures;
	Map<String, String>					retextures;
	
	public static TemplateIconHandler fromJson(String json)
	{
		return new TemplateIconHandler(Maps.transformValues(new Gson().<Map<String, String>> fromJson(json, Map.class), ResourceLocation::new));
	}
	
	public TemplateIconHandler(Map<String, ResourceLocation> textures)
	{
		this.textures = textures;
		this.retextures = ImmutableMap.of();
	}
	
	private TemplateIconHandler(TemplateIconHandler handler, Map<String, String> retexture)
	{
		this.retextures = new HashMap<>(handler.retextures);
		this.retextures.putAll(retexture);
		this.textures = ImmutableMap.copyOf(Maps.transformEntries(handler.textures, this::get));
	}
	
	private ResourceLocation get(String key, ResourceLocation location)
	{
		return this.retextures.containsKey(key) ? get("#" + key, new ArrayList<>(4)) : location;
	}
	
	private ResourceLocation get(String key, List<String> list)
	{
		if (key.charAt(0) == '#')
		{
			String sub = key.substring(1);
			if (list.contains(sub)) throw new IllegalStateException("Looped texture key loading.");
			list.add(sub);
			if (this.retextures.containsKey(sub))
			{
				return get(this.retextures.get(sub), list);
			}
			else
				// Get missing texture as default.
				return TextureMap.LOCATION_MISSING_TEXTURE;
		}
		else
			return new ResourceLocation(key);
	}
	
	@Override
	public int size()
	{
		return this.textures.size();
	}
	
	@Override
	public Collection<ResourceLocation> resources()
	{
		return this.textures.values();
	}
	
	@Override
	public IIconCollection retexture(Map<String, String> map)
	{
		return new TemplateIconHandler(this, map);
	}
	
	@Override
	public Map<String, ResourceLocation> build()
	{
		return this.textures;
	}
}
