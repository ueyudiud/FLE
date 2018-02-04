/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model;

import nebula.Nebula;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface INebulaCustomModelLoader extends ICustomModelLoader
{
	@Override
	default void onResourceManagerReload(IResourceManager resourceManager)
	{
		// Nothing to do.
	}
	
	@Override
	default boolean accepts(ResourceLocation modelLocation)
	{
		return getModID().equals(modelLocation.getResourceDomain()) && (modelLocation.getResourcePath().startsWith(getLoaderPrefix()) || modelLocation.getResourcePath().startsWith("models/block/" + getLoaderPrefix()) || modelLocation.getResourcePath().startsWith("models/item/" + getLoaderPrefix()));
	}
	
	default String getModID()
	{
		return Nebula.INNER_RENDER;
	}
	
	String getLoaderPrefix();
}
