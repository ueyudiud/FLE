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

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nebula.Log;
import nebula.common.data.Misc;
import nebula.common.util.IO;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Far Core Block Model Loader, provide more flexible
 * model loader for creator, make more convenient model
 * file writing rules for resource pack maker.<p>
 * @author ueyudiud
 * @version 0.1
 */
@SideOnly(Side.CLIENT)
public enum NebulaBlockModelLoader implements ICustomModelLoader
{
	INSTANCE;
	
	private static final Map<Block, ResourceLocation> ACCEPT_BLOCKS = new HashMap<>();
	private static final Map<ResourceLocation, Block> MODELLOCATION_TO_BLOCK = new HashMap<>();
	private Map<Block, FlexiableStateMap> map;
	
	private static IResourceManager manager;
	
	static final Gson GSON = new GsonBuilder()
			.create();
	
	public static void registerModel(Block block, ResourceLocation location)
	{
		//The default model location of block.
		ModelResourceLocation location1 = new ModelResourceLocation(block.getRegistryName(), "normal");
		//For stack to location logic, it will only return single model location, the variant will be match in model.
		ModelLoader.setCustomStateMapper(block, block1->
		Maps.toMap(block1.getBlockState().getValidStates(), (Function<IBlockState, ModelResourceLocation>) Misc.anyTo(location1)));
		//		//Register allowed build item variant.
		//		ModelLoader.registerItemVariants(item, location1);
		//The real location of model.
		location = new ResourceLocation(location.getResourceDomain(), "models/block1/" + location.getResourcePath() + ".json");
		
		ACCEPT_BLOCKS.put(block, location);
		MODELLOCATION_TO_BLOCK.put(location1, block);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		manager = resourceManager;
		Log.reset();
		for (Entry<Block, ResourceLocation> entry : ACCEPT_BLOCKS.entrySet())
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
					FlexiableStateMap map = GSON.fromJson(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(codes))), FlexiableStateMap.class);
					if (map != null)
					{
						this.map.put(entry.getKey(), map);
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
		return MODELLOCATION_TO_BLOCK.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return null;
	}
}