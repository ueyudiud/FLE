/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author ueyudiud
 */
public class JsonDeserializerGateway<T> implements JsonDeserializer<T>
{
	private String splitKey;
	
	private Map<String, JsonDeserializer<? extends T>>	deserializers				= new HashMap<>();
	private JsonDeserializer<? extends T>				defaultDeserializer;
	private boolean										throwExceptionWhenNoMatched	= false;
	
	public JsonDeserializerGateway(String splitKey, JsonDeserializer<? extends T> deserializer)
	{
		this.splitKey = splitKey;
		this.defaultDeserializer = deserializer;
	}
	
	public JsonDeserializerGateway<T> setThrowExceptionWhenNoMatched()
	{
		this.throwExceptionWhenNoMatched = true;
		return this;
	}
	
	public void addDeserializer(String key, JsonDeserializer<? extends T> deserializer)
	{
		if (this.deserializers.containsKey(key)) throw new RuntimeException("The deserializer key '" + key + "' already exists.");
		this.deserializers.put(key, deserializer);
	}
	
	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		JsonDeserializer<? extends T> deserializer;
		JsonObject object = json.getAsJsonObject();
		if (object.has(this.splitKey))
		{
			if (this.throwExceptionWhenNoMatched)
			{
				deserializer = this.deserializers.get(object.get(this.splitKey).getAsString());
				if (deserializer == null) throw new JsonParseException("No deserializer found. got: " + object.get(this.splitKey).getAsString());
			}
			else
			{
				deserializer = this.deserializers.getOrDefault(object.get(this.splitKey).getAsString(), this.defaultDeserializer);
			}
		}
		else if (this.defaultDeserializer != null)
		{
			deserializer = this.defaultDeserializer;
		}
		else
			throw new JsonParseException("No deserializer tag found. Please select a deserializer with key '" + this.splitKey + "'");
		return deserializer.deserialize(json, typeOfT, context);
	}
}
