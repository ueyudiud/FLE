/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import java.util.Collection;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The texture supplier.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public interface IIconCollection
{
	/**
	 * Return all icon contains in this collection.
	 * 
	 * @return the collection size.
	 */
	int size();
	
	/**
	 * Get loaded resource by this collection.
	 * 
	 * @return the resources collection.
	 */
	Collection<ResourceLocation> resources();
	
	/**
	 * <tt>retexture</tt> action, relocate resource by input
	 * argument.
	 * 
	 * @param map
	 * @return the retextered icon collection.
	 */
	IIconCollection retexture(Map<String, String> map);
	
	/**
	 * Build icon in collection.
	 * @return
	 */
	Map<String, ResourceLocation> build();
}
