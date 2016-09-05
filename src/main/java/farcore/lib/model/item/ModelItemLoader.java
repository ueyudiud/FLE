package farcore.lib.model.item;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.Log;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

/**
 * Not finish yet.
 * @author ueyudiud
 *
 */
@Deprecated
public enum ModelItemLoader implements ICustomModelLoader, ItemMeshDefinition
{
	INSTANCE;
	
	private static interface IConditionMatcher<T>
	{
		boolean parseBool(T target, String tag);

		int parseInteger(T target, String tag);

		float parseFloat(T target, String tag);
		
		double parseDouble(T target, String tag);

		String parseString(T target, String tag);
	}

	private static final IConditionMatcher<ItemStack> BASE_MATCHER = new IConditionMatcher<ItemStack>()
	{
		private NBTTagCompound getCompound(NBTTagCompound nbt, String tag)
		{
			if(nbt == null) return NBTTagCompoundEmpty.instance;
			int idx = tag.indexOf(':');
			if(idx == -1)
				return nbt;
			else
			{
				String value = tag.substring(0, idx - 1);
				return getCompound(nbt.getCompoundTag(value), tag.substring(idx + 1));
			}
		}

		@Override
		public boolean parseBool(ItemStack target, String tag)
		{
			if(tag.startsWith("nbt:"))
			{
				if(target == null) return false;
				NBTTagCompound compound = getCompound(target.getTagCompound(), tag.substring(4));
				return compound.getBoolean(tag.substring(tag.lastIndexOf(':') + 1));
			}
			return false;
		}
		
		@Override
		public int parseInteger(ItemStack target, String tag)
		{
			if(tag.startsWith("nbt:"))
			{
				if(target == null) return 0;
				NBTTagCompound compound = getCompound(target.getTagCompound(), tag.substring(4));
				return compound.getInteger(tag.substring(tag.lastIndexOf(':') + 1));
			}
			else if("meta".equals(tag))
				return target.getItemDamage();
			else if("stackSize".equals(tag))
				return target.stackSize;
			return 0;
		}
		
		@Override
		public float parseFloat(ItemStack target, String tag)
		{
			if(tag.startsWith("nbt:"))
			{
				if(target == null) return 0;
				NBTTagCompound compound = getCompound(target.getTagCompound(), tag.substring(4));
				return compound.getFloat(tag.substring(tag.lastIndexOf(':') + 1));
			}
			return 0;
		}
		
		@Override
		public double parseDouble(ItemStack target, String tag)
		{
			if(tag.startsWith("nbt:"))
			{
				if(target == null) return 0;
				NBTTagCompound compound = getCompound(target.getTagCompound(), tag.substring(4));
				return compound.getDouble(tag.substring(tag.lastIndexOf(':') + 1));
			}
			return 0;
		}
		
		@Override
		public String parseString(ItemStack target, String tag)
		{
			if(tag.startsWith("nbt:"))
			{
				if(target == null) return "";
				NBTTagCompound compound = getCompound(target.getTagCompound(), tag.substring(4));
				return compound.getString(tag.substring(tag.lastIndexOf(':') + 1));
			}
			return "";
		}
	};
	private static final JsonDeserializer<IDataChecker<?>> DATA_CHECKER_DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if(!json.isJsonObject())
			throw new JsonParseException("Fail to parse json, the json is not an object.");
		return null;
	};
	
	private static final Map<Item, ResourceLocation> STATE_LOCATIONS = new HashMap();
	private static final Map<Item, ModelResourceLocation> MODEL_LOCATIONS = new HashMap();
	private Map<ModelResourceLocation, IModel> models;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		int size = STATE_LOCATIONS.size();
		ProgressBar bar = ProgressManager.push("Far Item Models", size);
		for(Entry<Item, ResourceLocation> entry : STATE_LOCATIONS.entrySet())
		{
			bar.step("Loading " + entry.getKey().getRegistryName().toString());
			Log.reset();
			Item item = entry.getKey();
			ResourceLocation location = entry.getValue();
			ModelResourceLocation location2 = MODEL_LOCATIONS.get(item);
			location = getRealLocation(location);
			IResource resource = null;
			try
			{
				resource = resourceManager.getResource(location);
				JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
				models.put(location2, loadModel(reader));
				resource.close();
				resource = null;
			}
			catch (IOException exception)
			{
				Log.cache(exception);
			}
			finally
			{
				if(resource != null)
				{
					try
					{
						resource.close();
					}
					catch(IOException exception2){}
				}
			}
			Log.logCachedExceptions();
		}
	}
	
	private ModelItemBase loadModel(JsonReader reader)
	{
		return null;
	}
	
	private ResourceLocation getRealLocation(ResourceLocation location)
	{
		return new ResourceLocation(location.getResourceDomain(), "itemstates/" + location.getResourcePath() + ".json");
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return MODEL_LOCATIONS.containsKey(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return models.getOrDefault(modelLocation, ModelLoaderRegistry.getMissingModel());
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return MODEL_LOCATIONS.get(stack.getItem());
	}
}