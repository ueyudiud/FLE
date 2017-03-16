/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.client.model.FlexibleItemSubmetaGetterLoader.SubmetaGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FlexibleItemModelCache
{
	/**
	 * Model layer deserializer.
	 */
	public static final JsonDeserializer<NebulaItemModelLayerCache> DESERIALIZER_2 = (element, typeOfT, context) ->
	{
		NebulaItemModelLayerCache cache = new NebulaItemModelLayerCache();
		try
		{
			if(element.isJsonObject())
			{
				JsonObject object = element.getAsJsonObject();
				if(object.has("include"))
				{
					JsonElement element1 = object.get("include");
					if(element1.isJsonArray())
					{
						JsonArray array = element1.getAsJsonArray();
						for(JsonElement element2 : array)
						{
							cache.allowedVariants.add(element2.getAsString());
						}
					}
					else if(element1.isJsonObject())
					{
						if(element1.getAsJsonObject().has("converts"))
						{
							JsonElement element2 = element1.getAsJsonObject().get("converts");
							if(element2.isJsonArray())
							{
								for(JsonElement element3 : element2.getAsJsonArray())
								{
									cache.convertsAllowTextures.add(element3.getAsString());
								}
							}
							else
							{
								final String variant = element2.getAsString();
								cache.layerType = 2;
								cache.convertsAllowTextures.add(variant);
								cache.convertApplier = stack -> variant;
							}
						}
						if(element1.getAsJsonObject().has("textures"))
						{
							JsonElement element2 = element1.getAsJsonObject().get("textures");
							if(element2.isJsonArray())
							{
								for(JsonElement element3 : element2.getAsJsonArray())
								{
									cache.allowedVariants.add(element3.getAsString());
								}
							}
							else
							{
								final String variant = element2.getAsString();
								cache.variantApplier = stack -> variant;
								cache.allowedVariants.add(variant);
							}
						}
						else
						{
							cache.allowedVariants = null;
						}
					}
					else
					{
						cache.allowedVariants.add(element1.getAsString());
						if(!object.has("submeta") && element1.getAsString().charAt(0) == '#')
						{
							String key1 = element1.getAsString().substring(1);
							cache.variantApplier = stack -> key1;//Mark for normal variant load by texture set.
						}
					}
				}
				else
				{
					cache.allowedVariants = null;
				}
				if(object.has("colorMultiplier"))
				{
					cache.colorMultiplier = ColorMultiplier.getColorMultiplier(new ResourceLocation(object.get("colorMultiplier").getAsString()));
				}
				if(object.has("zOffset"))
				{
					cache.zOffset = object.get("zOffset").getAsFloat();
				}
				if(object.has("baseColor"))
				{
					cache.baseColor = object.get("color").getAsInt();
				}
				if(object.has("convert"))
				{
					if(cache.convertApplier != null)
						throw new JsonParseException("The cache already contain a convert icon!");
					cache.layerType = 2;
					cache.convertApplier = context.deserialize(object.get("convert"), SubmetaGetter.class);
				}
				else if(object.has("renderFull3D"))
				{
					cache.layerType = (byte) (object.get("renderFull3D").getAsBoolean() ? 0 : 1);
				}
				if(object.has("submeta"))
				{
					cache.variantApplier = context.deserialize(object.get("submeta"), SubmetaGetter.class);
				}
			}
			else
			{
				String key = element.getAsString();
				switch (key.charAt(0))
				{
				case '~'://Multi texture selector, only use in single locate.
					cache.variantApplier = FlexibleItemSubmetaGetterLoader.getSubmetaGetter(new ResourceLocation(key.substring(1)));
					cache.allowedVariants = null;
					break;
				case '#'://Fix at 1-29, meta marker will be removed in sub meta getter.
					String substring = key.substring(1);
					cache.variantApplier = stack -> substring;
					cache.allowedVariants.add(key);
					break;
				default:
					cache.variantApplier = stack -> key;
					cache.allowedVariants.add(key);
					break;
				}
			}
		}
		catch(Exception exception)
		{
			if(exception instanceof JsonParseException)
				throw exception;
			throw new JsonParseException(exception);
		}
		return cache;
	};
	/**
	 * Model deserializer.
	 */
	public static final JsonDeserializer<FlexibleItemModelCache> DESERIALIZER_1 = (element, typeOfT, context) ->
	{
		FlexibleItemModelCache cache = new FlexibleItemModelCache();
		try
		{
			JsonObject object = element.getAsJsonObject();
			if(object.has("textures"))
			{
				JsonElement element1 = object.get("textures");
				cache.textureCol = context.deserialize(element1, FlexibleTextureSet.class);
			}
			if(object.has("layers"))
			{
				JsonElement element12 = object.get("layers");
				if(element12.isJsonArray())
				{
					JsonArray array1 = element12.getAsJsonArray();
					cache.caches = new NebulaItemModelLayerCache[array1.size()];
					for(int i = 0; i < array1.size(); ++i)
					{
						JsonElement element2 = array1.get(i);
						NebulaItemModelLayerCache layerCache1 = context.deserialize(element2, NebulaItemModelLayerCache.class);
						layerCache1.index = i;
						cache.caches[i] = layerCache1;
					}
				}
				else
				{
					NebulaItemModelLayerCache layerCache2 = context.deserialize(element12, NebulaItemModelLayerCache.class);
					cache.caches = new NebulaItemModelLayerCache[]{ layerCache2 };
				}
			}
			else
			{
				cache.caches = new NebulaItemModelLayerCache[]{ new NebulaItemModelLayerCache() };
			}
		}
		catch(Exception exception)
		{
			if(exception instanceof JsonParseException)
				throw exception;
			throw new JsonParseException(exception);
		}
		return cache;
	};
	
	/**
	 * Cached model layer.
	 * @author ueyudiud
	 *
	 */
	public static class NebulaItemModelLayerCache
	{
		int index;
		float zOffset = 0.5F;
		int baseColor = 0xFFFFFFFF;
		List<String> allowedVariants = new ArrayList();
		List<String> convertsAllowTextures = new ArrayList();
		SubmetaGetter variantApplier = NebulaItemModelLoader.NORMAL_FUNCTION;
		ToIntFunction<ItemStack> colorMultiplier = ColorMultiplier.NORMAL_MULTIPLIER;
		
		byte layerType = 0;
		SubmetaGetter convertApplier = null;
	}
	
	FlexibleTextureSet textureCol = new FlexibleTextureSet();
	NebulaItemModelLayerCache[] caches;
	String particle;
}