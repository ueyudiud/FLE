/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.client.util.IIconHandler;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class SimpleIconHandler implements IIconHandler
{
	final String key;
	final ResourceLocation location;
	
	public SimpleIconHandler(String key, ResourceLocation location)
	{
		this.key = key;
		this.location = location;
	}
	
	@Override
	public Collection<ResourceLocation> resources()
	{
		return ImmutableList.of(this.location);
	}
	
	@Override
	public IIconHandler retexture(Map<String, String> map)
	{
		return map.containsKey(this.key) ? new SimpleIconHandler(this.key, new ResourceLocation(map.get(this.key))) : this;//No retexture will allowed.
	}
	
	@Override
	public Map<String, ResourceLocation> build()
	{
		return ImmutableMap.of(this.key, this.location);
	}
}