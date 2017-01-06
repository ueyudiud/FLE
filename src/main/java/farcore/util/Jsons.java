/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author ueyudiud
 */
public class Jsons
{
	public static int getOrDefault(JsonObject object, String key, int def) throws JsonParseException
	{
		try
		{
			return object.has(key) ? object.get(key).getAsInt() : def;
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static long getOrDefault(JsonObject object, String key, long def) throws JsonParseException
	{
		try
		{
			return object.has(key) ? object.get(key).getAsLong() : def;
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static float getOrDefault(JsonObject object, String key, float def) throws JsonParseException
	{
		try
		{
			return object.has(key) ? object.get(key).getAsFloat() : def;
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static double getOrDefault(JsonObject object, String key, double def) throws JsonParseException
	{
		try
		{
			return object.has(key) ? object.get(key).getAsDouble() : def;
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static boolean getOrDefault(JsonObject object, String key, boolean def) throws JsonParseException
	{
		try
		{
			return object.has(key) ? object.get(key).getAsBoolean() : def;
		}
		catch (UnsupportedOperationException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a boolean value.", key));
		}
	}
	
	public static int[] getIntArray(JsonObject object, String key, int length) throws JsonParseException
	{
		try
		{
			JsonArray array = object.getAsJsonArray(key);
			if(length < 0 || array.size() < length)
				throw new JsonParseException("The array size should be " + length + ", got " + array.size());
			int[] ret = new int[array.size()];
			for(int i = 0; i < array.size(); ++i)
			{
				ret[i] = array.get(i).getAsInt();
			}
			return ret;
		}
		catch (UnsupportedOperationException | ClassCastException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not an array.", key));
		}
	}
	
	public static float[] getFloatArray(JsonObject object, String key, int length) throws JsonParseException
	{
		try
		{
			JsonArray array = object.getAsJsonArray(key);
			if(length < 0 || array.size() < length)
				throw new JsonParseException("The array size should be " + length + ", got " + array.size());
			float[] ret = new float[array.size()];
			for(int i = 0; i < array.size(); ++i)
			{
				ret[i] = array.get(i).getAsInt();
			}
			return ret;
		}
		catch (UnsupportedOperationException | ClassCastException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not an array.", key));
		}
	}
	
	public static double[] getDoubleArray(JsonObject object, String key, int length) throws JsonParseException
	{
		try
		{
			JsonArray array = object.getAsJsonArray(key);
			if(length < 0 || array.size() < length)
				throw new JsonParseException("The array size should be " + length + ", got " + array.size());
			double[] ret = new double[array.size()];
			for(int i = 0; i < array.size(); ++i)
			{
				ret[i] = array.get(i).getAsInt();
			}
			return ret;
		}
		catch (UnsupportedOperationException | ClassCastException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not an array.", key));
		}
	}
}