/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import java.util.Collection;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

/**
 * The texture supplier.
 * @author ueyudiud
 */
public interface IIconCollection
{
	Collection<ResourceLocation> resources();
	
	IIconCollection retexture(Map<String, String> map);
	
	Map<String, ResourceLocation> build();
}