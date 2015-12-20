package flapi.util.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

public class JsonInputStream
{
	JsonElement element;
	
	public JsonInputStream(JsonElement ele)
	{
		element = ele;
	}
	
	public boolean contain(String tag)
	{
		return element.isJsonObject() ? element.getAsJsonObject().has(tag) : false;
	}
	
	public JsonInputStream sub(String tag)
	{
		return new JsonInputStream(element.isJsonObject() ? element.getAsJsonObject().get(tag) : JsonNull.INSTANCE);
	}
	
	public JsonInputStream sub(int id)
	{
		return new JsonInputStream(element.isJsonArray() ? element.getAsJsonArray().get(id) : element);
	}
	
	public int size()
	{
		return element.isJsonArray() ? element.getAsJsonArray().size() : 1;
	}
	
	public int getInteger(int defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsInt();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public long getLong(long defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsLong();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public double getDouble(double defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsDouble();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public long getLong(String tag, long defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsLong();
		}
		else if(element.isJsonObject())
		{
			return element.getAsJsonObject().get(tag).getAsLong();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public int getInteger(String tag, int defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsInt();
		}
		else if(element.isJsonObject())
		{
			return element.getAsJsonObject().get(tag).getAsInt();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public double getDouble(String tag, double defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsDouble();
		}
		else if(element.isJsonObject())
		{
			return element.getAsJsonObject().get(tag).getAsDouble();
		}
		else
		{
			return defaultValue;
		}
	}
	
	public String getString(String tag, String defaultValue)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsString();
		}
		else if(element.isJsonObject())
		{
			return element.getAsJsonObject().get(tag).getAsString();
		}
		else
		{
			return defaultValue;
		}
	}

	public boolean getBoolean(String tag)
	{
		if(element.isJsonPrimitive())
		{
			return element.getAsBoolean();
		}
		else if(element.isJsonObject())
		{
			return element.getAsJsonObject().get(tag).getAsBoolean();
		}
		return false;
	}
}