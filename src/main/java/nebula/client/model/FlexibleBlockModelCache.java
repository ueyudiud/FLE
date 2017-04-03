/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.client.model.part.INebulaModelPart;
import nebula.client.model.part.NebulaModelPartDecoder;
import nebula.common.util.Jsons;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * @author ueyudiud
 */
public class FlexibleBlockModelCache
{
	static final JsonDeserializer<FlexibleBlockModelCache> DESERIALIZER = (json, typeOfT, context) -> {
		if (!json.isJsonObject()) throw new JsonParseException("The model should be object!");
		JsonObject object = json.getAsJsonObject();
		try
		{
			if (!object.has("part"))
				throw new JsonParseException("No model part dected!");
			FlexibleBlockModelCache cache = new FlexibleBlockModelCache();
			JsonElement json1 = object.get("part");
			if (json1.isJsonArray())
			{
				cache.parts = Jsons.getAsList(json1.getAsJsonArray(), e->context.deserialize(e, INebulaModelPart.class));
			}
			else
			{
				cache.parts = ImmutableList.of(context.deserialize(json1, INebulaModelPart.class));
			}
			if (object.has("textures"))
			{
				Map<String, String> map = Jsons.getAsMap(object.getAsJsonObject("textures"), JsonElement::getAsString);
				cache.particleLocation = map.getOrDefault("particle", TextureMap.LOCATION_MISSING_TEXTURE.toString());
				if (cache.particleLocation.charAt(0) == '#')
				{
					cache.particleLocation = map.getOrDefault(cache.particleLocation.substring(1), cache.particleLocation);
				}
				cache.parts.replaceAll(p->NebulaModelPartDecoder.replaceModelTexture(p, map));
			}
			cache.ambientOcclusion = Jsons.getOrDefault(object, "ambientOcclusion", true);
			return cache;
		}
		catch (JsonParseException exception)
		{
			throw exception;
		}
		catch (Exception exception)
		{
			throw new JsonParseException(exception);
		}
	};
	
	boolean ambientOcclusion;
	String particleLocation;
	List<INebulaModelPart> parts;
}