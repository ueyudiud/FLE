/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import java.util.Collection;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public interface IIconHandler
{
	Collection<ResourceLocation> resources();
	
	IIconHandler retexture(Map<String, String> map);
	
	Map<String, ResourceLocation> build();
}