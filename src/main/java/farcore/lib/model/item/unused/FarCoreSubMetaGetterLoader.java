/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.data.Config;
import farcore.lib.item.ItemBase;
import farcore.lib.util.Log;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public final class FarCoreSubMetaGetterLoader
{
	private static final Map<ResourceLocation, Function<ItemStack, String>> SUBMETA_PROVIDER = new HashMap();
	private static Map<ResourceLocation, Function<ItemStack, String>> loadedFunctions;
	
	public static void registerSubmetaProvider(ResourceLocation location, Map<Integer, String> map, ItemBase item)
	{
		registerSubmetaProvider(location, stack -> map.getOrDefault(item.getBaseDamage(stack), "missing"));
	}
	
	public static void registerSubmetaProvider(ResourceLocation location, Function<ItemStack, String> function)
	{
		SUBMETA_PROVIDER.put(location, function);
	}
	
	static void resetFunctions()
	{
		loadedFunctions = new HashMap();
	}
	
	static void clearFunctions()
	{
		loadedFunctions = null;
	}
	
	static Function<ItemStack, String> loadSubmetaGetter(IResourceManager manager, JsonElement json)
	{
		try
		{
			if(json.isJsonObject())
			{
				JsonObject object = json.getAsJsonObject();
				if (object.has("key")) return (ItemStack stack) -> object.get("key").getAsString();
				else if (object.has("parent"))//Raw type.
				{
					FarCoreVariantSubmetaProvider function = new FarCoreVariantSubmetaProvider(loadSubmetaGetter(manager, object.get("parent")));
					if (object.has("variant"))
					{
						ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
						JsonObject object1 = object.getAsJsonObject("variant");
						for (Entry<String, JsonElement> entry : object1.entrySet())
						{
							builder.put(entry.getKey(), entry.getValue().getAsString());
						}
						function.setVariant(builder.build());
					}
					if (object.has("replacement"))
					{
						ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
						JsonObject object1 = object.getAsJsonObject("replacement");
						for (Entry<String, JsonElement> entry : object1.entrySet())
						{
							builder.put(entry.getKey(), entry.getValue().getAsString());
						}
						function.setReplacement(builder.build());
					}
					if (object.has("postfix"))
					{
						function.setPostfix(object.get("prefix").getAsString());
					}
					if (object.has("postfix"))
					{
						function.setPostfix(object.get("postfix").getAsString());
					}
					return function;
				}
				return FarCoreItemModelLoader.NORMAL_FUNCTION;
			}
			else
			{
				String value = json.getAsString();
				return value.charAt(0) == '#' ? loadSubmetaGetter(manager, new ResourceLocation(value.substring(1))) : (ItemStack stack) -> value;
			}
		}
		catch (Exception exception)
		{
			if(exception instanceof JsonParseException)
				throw exception;
			throw new JsonParseException(exception);
		}
	}
	
	/**
	 * 
	 * @since 1.4
	 * @param location
	 * @return
	 */
	static Function<ItemStack, String> loadSubmetaGetter(IResourceManager manager, ResourceLocation location)
	{
		Function<ItemStack, String> function = SUBMETA_PROVIDER.get(location);
		if (function != null) return function;
		if (Config.useJavascriptInResource)
		{
			if(loadedFunctions == null) loadedFunctions = new HashMap();
			if (loadedFunctions.containsKey(location))
				return loadedFunctions.get(location);
			IResource resource = null;
			byte[] values;
			try
			{
				ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), "f_model/mg/" + location.getResourcePath() + ".js");
				resource = manager.getResource(location1);
				values = IOUtils.toByteArray(resource.getInputStream());
			}
			catch(IOException exception)
			{
				values = null;
			}
			finally
			{
				if(resource != null)
				{
					try
					{
						resource.close();
					}
					catch(IOException exception2)
					{
						;
					}
				}
			}
			if(values != null)
			{
				try
				{
					FarCoreJSSubmetaProvider provider = new FarCoreJSSubmetaProvider(values);
					loadedFunctions.put(location, provider);
					return provider;
				}
				catch(ScriptException exception)
				{
					Log.cache(exception);
				}
			}
		}
		return FarCoreItemModelLoader.NORMAL_FUNCTION;
	}
}