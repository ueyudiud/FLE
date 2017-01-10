/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * @author ueyudiud
 */
public enum OrderModelLoader implements ICustomModelLoader
{
	INSTANCE;
	
	private static final Map<ResourceLocation, IModel> MODEL_MAP = new HashMap();
	
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
		if(modelLocation instanceof ModelResourceLocation)
		{
			modelLocation = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
		}
		return MODEL_MAP.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		if(modelLocation instanceof ModelResourceLocation)
		{
			modelLocation = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
		}
		return MODEL_MAP.get(modelLocation);
	}
}