/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model.part;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.client.model.part.INebulaModelPart.INebulaRetexturableModelPart;
import nebula.common.util.Jsons;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class NebulaModelPartDecoder implements JsonDeserializer<INebulaModelPart>
{
	public static final NebulaModelPartDecoder INSTANCE = new NebulaModelPartDecoder();
	
	private static final Map<ResourceLocation, IModelPartLoader<?>> LOCATION_TO_LOADER_MAP = new HashMap<>();
	private static final Map<Class<? extends INebulaModelPart>, IModelPartLoader<?>> CLASS_TO_LOADER_MAP = new HashMap<>();
	
	static
	{
		registerDeserializer(new ResourceLocation("nebula", "quad"), PackedQuadData.class, PackedQuadData.LOADER);
		registerDeserializer(new ResourceLocation("nebula", "verticalcube"), PackedVerticalCube.class, PackedVerticalCube.LOADER);
	}
	
	public static void registerDeserializer(ResourceLocation location, Class<? extends INebulaModelPart> targetClass, IModelPartLoader<?> loader)
	{
		LOCATION_TO_LOADER_MAP.put(location, loader);
		CLASS_TO_LOADER_MAP.put(targetClass, loader);
	}
	
	private NebulaModelPartDecoder() {}
	
	@Override
	public INebulaModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		try
		{
			JsonObject object = json.getAsJsonObject();
			INebulaModelPart part;
			if(!object.has("extend"))
			{
				if(object.has("parent"))
				{
					String location = object.get("parent").getAsString();
					part = loadModelPart(new ResourceLocation(location));
					IModelPartLoader loader = CLASS_TO_LOADER_MAP.get(part.getClass());
					if(loader == null)
						throw new JsonParseException("No allowed loader with class " + part.getClass() + " detected!");
					part = loader.load(part, object, context);
				}
				else throw new JsonParseException("No model part creator detected!");
			}
			else
			{
				INebulaModelPart parent = null;
				String location = object.get("extend").getAsString();
				IModelPartLoader loader = LOCATION_TO_LOADER_MAP.get(new ResourceLocation(location));
				if(object.has("parent"))
				{
					location = object.get("parent").getAsString();
					parent = loadModelPart(new ResourceLocation(location));
				}
				if(loader == null)
					throw new JsonParseException("The loader at '" + location + "' does not exist.");
				part = loader.load(parent, object, context);
			}
			if (object.has("textures"))
			{
				if (!(part instanceof INebulaRetexturableModelPart))
					throw new JsonParseException("The type of part : " + part.getClass() + " does not "
							+ "implements INebulaRetexturableModelPart type, you can't retexture it.");
				JsonElement json1 = object.get("textures");
				if (json1.isJsonObject())
				{
					Map<String, String> map = Jsons.getAsMap(json1.getAsJsonObject(), JsonElement::getAsString);
					part = ((INebulaRetexturableModelPart) part).retexture(map);
				}
				else if (json1.isJsonPrimitive())//"all" texture key if only a primitive.
				{
					part = ((INebulaRetexturableModelPart) part).retexture(ImmutableMap.of("all", json1.getAsString()));
				}
				else throw new JsonParseException("Unknown retexture collction.");
			}
			return part;
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
	
	public static INebulaModelPart replaceModelTexture(INebulaModelPart part, Map<String, String> map)
	{
		if (part instanceof INebulaRetexturableModelPart)
		{
			return ((INebulaRetexturableModelPart) part).retexture(map);
		}
		return part;
	}
	
	public static INebulaModelPart loadModelPart(ResourceLocation location) throws Exception
	{
		throw new UnsupportedOperationException();//Not finish yet. TODO
	}
}