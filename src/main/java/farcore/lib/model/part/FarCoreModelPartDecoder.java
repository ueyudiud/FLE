/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.part;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class FarCoreModelPartDecoder implements JsonDeserializer<IFarCoreModelPart>
{
	public static final FarCoreModelPartDecoder INSTANCE = new FarCoreModelPartDecoder();
	
	private static final Map<ResourceLocation, IModelPartLoader> LOCATION_TO_LOADER_MAP = new HashMap();
	private static final Map<Class<? extends IFarCoreModelPart>, IModelPartLoader> CLASS_TO_LOADER_MAP = new HashMap();
	
	public static void registerDeserializer(ResourceLocation location, Class<? extends IFarCoreModelPart> targetClass, IModelPartLoader loader)
	{
		LOCATION_TO_LOADER_MAP.put(location, loader);
		CLASS_TO_LOADER_MAP.put(targetClass, loader);
	}
	
	private FarCoreModelPartDecoder() {}
	
	@Override
	public IFarCoreModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		try
		{
			JsonObject object = json.getAsJsonObject();
			if(!object.has("extend"))
			{
				if(object.has("parent"))
				{
					String location = object.get("parent").getAsString();
					IFarCoreModelPart part = loadModelPart(new ResourceLocation(location));
					IModelPartLoader loader = CLASS_TO_LOADER_MAP.get(part.getClass());
					if(loader == null)
						throw new JsonParseException("No allowed loader with class " + part.getClass() + " detected!");
					return loader.load(part, object, context);
				}
				else throw new JsonParseException("No model part creator detected!");
			}
			else
			{
				IFarCoreModelPart parent = null;
				String location = object.get("extend").getAsString();
				IModelPartLoader loader = LOCATION_TO_LOADER_MAP.get(new ResourceLocation(location));
				if(object.has("parent"))
				{
					location = object.get("parent").getAsString();
					parent = loadModelPart(new ResourceLocation(location));
				}
				if(location == null)
					throw new JsonParseException("The loader at '" + location + "' does not exist.");
				return loader.load(parent, object, context);
			}
		}
		catch (JsonParseException exception)
		{
			throw exception;
		}
		catch (Exception exception)
		{
			throw new JsonParseException(exception);
		}
	}
	
	public static IFarCoreModelPart loadModelPart(ResourceLocation location) throws Exception
	{
		return null;
	}
}