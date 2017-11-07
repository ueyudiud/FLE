/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.client.util.IIconCollection;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
class MissingnoIconHandler implements IIconCollection
{
	private static final ImmutableMap<String, ResourceLocation> BUILD = ImmutableMap.of(NebulaModelLoader.NORMAL, TextureMap.LOCATION_MISSING_TEXTURE);
	
	@Override
	public int size()
	{
		return 0;
	}
	
	@Override
	public Collection<ResourceLocation> resources()
	{
		return ImmutableList.of();
	}
	
	@Override
	public IIconCollection retexture(Map<String, String> map)
	{
		return this;
	}
	
	@Override
	public Map<String, ResourceLocation> build()
	{
		return BUILD;
	}
}
