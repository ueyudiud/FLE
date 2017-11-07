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
	int size();
	
	Collection<ResourceLocation> resources();
	
	IIconCollection retexture(Map<String, String> map);
	
	Map<String, ResourceLocation> build();
}
