package farcore.lib.model.loader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import farcore.lib.model.part.BlockstateInformationContainer;
import jline.internal.InputStreamReader;

public class ModelStateSelector
{
	private static final JsonDeserializer<BlockstateInformationContainer> DESERIALIZER =
			(JsonElement json, Type typeOfT, JsonDeserializationContext context) ->
	{
		if(!(json instanceof JsonObject)) throw new JsonParseException("The type should be object!");
		BlockstateInformationContainer container = new BlockstateInformationContainer();
		JsonObject object = (JsonObject) json;
		if(object.has("parent"))
		{
			container.parentName = object.get("parent").getAsString();
		}
		return null;
	};
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(BlockstateInformationContainer.class, DESERIALIZER)
			.create();
	
	public static BlockstateInformationContainer loadFromStream(InputStream stream) throws IOException
	{
		try
		{
			byte[] bytes = IOUtils.toByteArray(new InputStreamReader(stream));
			return loadFromJson(
					GSON.fromJson(new JsonReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)))), BlockstateInformationContainer.class));
		}
		catch (JsonParseException exception)
		{
			throw new IOException("Invalid stream.", exception);
		}
		catch (IOException exception)
		{
			throw new IOException(exception);
		}
		finally
		{
			if(stream != null)
			{
				try
				{
					stream.close();
				}
				catch(Exception exception2)
				{
					throw new IOException(exception2);
				}
			}
		}
	}

	private static BlockstateInformationContainer loadFromJson(JsonReader jsonReader)
	{
		return null;
	}
}