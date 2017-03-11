/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model.part;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author ueyudiud
 */
@FunctionalInterface
public interface IModelPartLoader<P extends INebulaModelPart>
{
	P load(@Nullable P parent, JsonObject json, JsonDeserializationContext context) throws JsonParseException, RuntimeException;
}