/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nebula.Log;
import nebula.client.model.part.INebulaModelPart;
import nebula.client.model.part.NebulaModelPartDecoder;
import nebula.common.util.IO;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Nebula Block Model Loader, provide more flexible
 * model loader for creator, make more convenient model
 * file writing rules for resource pack maker.<p>
 * @author ueyudiud
 * @version 0.1
 */
@SideOnly(Side.CLIENT)
public enum NebulaBlockModelLoader implements ICustomModelLoader
{
	/**
	 * The instance of model loader.
	 */
	INSTANCE;
	
	/**
	 * The accept location cache.
	 */
	private static final Map<ResourceLocation, ResourceLocation> ACCEPT_LOCATION = new HashMap<>();
	private Map<ResourceLocation, FlexibleBlockModelCache> map;
	
	private static IResourceManager manager;
	
	static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(INebulaModelPart.class, NebulaModelPartDecoder.INSTANCE)
			.registerTypeAdapter(FlexibleBlockModelCache.class, FlexibleBlockModelCache.DESERIALIZER)
			.create();
	
	public static void registerModel(ResourceLocation location, ResourceLocation location1)
	{
		ACCEPT_LOCATION.put(location, new ResourceLocation(location1.getResourceDomain(), "models/block1/" + location1.getResourcePath() + ".json"));
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		manager = resourceManager;
		this.map = new HashMap<>();
		Log.reset();
		for (Entry<ResourceLocation, ResourceLocation> entry : ACCEPT_LOCATION.entrySet())
		{
			ResourceLocation location = entry.getValue();
			byte[] codes = null;
			try
			{
				codes = IO.copyResource(manager, location);
			}
			catch (IOException exception)
			{
				;
			}
			if (codes != null)
			{
				try
				{
					FlexibleBlockModelCache cache = GSON.fromJson(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(codes))), FlexibleBlockModelCache.class);
					if (cache != null)
					{
						this.map.put(entry.getKey(), cache);
					}
				}
				catch (Exception exception)
				{
					Log.cache(exception);
				}
			}
		}
		Log.logCachedExceptions();
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return this.map.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return new FlexibleBlockModelUnbaked(this.map.get(modelLocation));
	}
}