/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For model which is load without resource reloading.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public enum OrderModelLoader implements ICustomModelLoader
{
	INSTANCE;
	
	private static final Map<ResourceLocation, IModel> MODEL_MAP = new HashMap<>();
	
	public static <V> void putModel(IForgeRegistryEntry<V> value, IModel model)
	{
		putModel(value.getRegistryName(), model);
	}
	
	public static void putModel(ResourceLocation location, IModel model)
	{
		MODEL_MAP.put(location, model);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return MODEL_MAP.containsKey(removeVariant(modelLocation));
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return MODEL_MAP.get(removeVariant(modelLocation));
	}
	
	private ResourceLocation removeVariant(ResourceLocation location)
	{
		if (location instanceof ModelResourceLocation)
		{
			return new ResourceLocation(location.getResourceDomain(), location.getResourcePath());
		}
		return location;
	}
}
