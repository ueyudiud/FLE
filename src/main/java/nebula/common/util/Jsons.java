/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ObjectArrays;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import nebula.base.function.IntFunction;
import nebula.base.function.SequenceFunction;

/**
 * @author ueyudiud
 */
public final class Jsons
{
	private Jsons()
	{
	}
	
	public static Optional<String> getString(JsonObject object, String key)
	{
		return object.has(key) ? Optional.of(object.get(key).getAsString()) : Optional.empty();
	}
	
	public static OptionalInt getInt(JsonObject object, String key)
	{
		try
		{
			return object.has(key) ? OptionalInt.of(object.get(key).getAsInt()) : OptionalInt.empty();
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static OptionalLong getLong(JsonObject object, String key)
	{
		try
		{
			return object.has(key) ? OptionalLong.of(object.get(key).getAsLong()) : OptionalLong.empty();
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static OptionalDouble getDouble(JsonObject object, String key)
	{
		try
		{
			return object.has(key) ? OptionalDouble.of(object.get(key).getAsDouble()) : OptionalDouble.empty();
		}
		catch (UnsupportedOperationException | NumberFormatException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a number.", key));
		}
	}
	
	public static Optional<Boolean> getBoolean(JsonObject object, String key)
	{
		try
		{
			return object.has(key) ? Optional.of(object.get(key).getAsBoolean()) : Optional.empty();
		}
		catch (UnsupportedOperationException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not a boolean value.", key));
		}
	}
	
	public static String getOrDefault(JsonObject object, String key, String def)
	{
		return getString(object, key).orElse(def);
	}
	
	public static int getOrDefault(JsonObject object, String key, int def) throws JsonParseException
	{
		return getInt(object, key).orElse(def);
	}
	
	public static long getOrDefault(JsonObject object, String key, long def) throws JsonParseException
	{
		return getLong(object, key).orElse(def);
	}
	
	public static float getOrDefault(JsonObject object, String key, float def) throws JsonParseException
	{
		return (float) getDouble(object, key).orElse(def);
	}
	
	public static double getOrDefault(JsonObject object, String key, double def) throws JsonParseException
	{
		return getDouble(object, key).orElse(def);
	}
	
	public static boolean getOrDefault(JsonObject object, String key, boolean def) throws JsonParseException
	{
		return getBoolean(object, key).orElse(def);
	}
	
	public static int[] getIntArray(JsonObject object, String key, int length) throws JsonParseException
	{
		try
		{
			JsonArray array = object.getAsJsonArray(key);
			if (length < 0 || array.size() < length) throw new JsonParseException("The array size should be " + length + ", got " + array.size());
			int[] ret = new int[array.size()];
			for (int i = 0; i < array.size(); ++i)
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
			return getFloatArray(array, length);
		}
		catch (UnsupportedOperationException | ClassCastException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not an array.", key));
		}
	}
	
	public static float[] getFloatArray(JsonArray array, int length) throws JsonParseException
	{
		if (length < 0 || array.size() < length) throw new JsonParseException("The array size should be " + length + ", got " + array.size());
		float[] ret = new float[array.size()];
		for (int i = 0; i < array.size(); ++i)
		{
			ret[i] = array.get(i).getAsFloat();
		}
		return ret;
	}
	
	public static double[] getDoubleArray(JsonObject object, String key, int length) throws JsonParseException
	{
		try
		{
			JsonArray array = object.getAsJsonArray(key);
			if (length < 0 || array.size() < length) throw new JsonParseException("The array size should be " + length + ", got " + array.size());
			double[] ret = new double[array.size()];
			for (int i = 0; i < array.size(); ++i)
			{
				ret[i] = array.get(i).getAsDouble();
			}
			return ret;
		}
		catch (UnsupportedOperationException | ClassCastException exception)
		{
			throw new JsonParseException(String.format("The key '%s' is not an array.", key));
		}
	}
	
	public static <T> T[] getArray(JsonArray array, int len, Class<T> clazz, Function<JsonElement, T> function) throws JsonParseException
	{
		if (len >= 0 && array.size() != len)
		{
			throw new JsonParseException("The array length should be " + len + ", got " + array.size() + ".");
		}
		if (len < 0)
		{
			return L.cast(getAsList(array, function), clazz);
		}
		else
		{
			T[] a = ObjectArrays.newArray(clazz, len);
			A.fill(a, ((IntFunction<JsonElement>) array::get).andThen(function));
			return a;
		}
	}
	
	public static <E> Map<String, E> getAsMap(JsonObject object, Function<JsonElement, E> function)
	{
		Map<String, E> map = new HashMap<>();
		for (Entry<String, JsonElement> entry : object.entrySet())
			map.put(entry.getKey(), function.apply(entry.getValue()));
		return map;
	}
	
	public static <E> List<E> getAsList(JsonArray array, Function<JsonElement, E> function)
	{
		List<E> list = new ArrayList<>();
		for (JsonElement element : array)
			list.add(function.apply(element));
		return list;
	}
	
	public static <E> List<E> getAsList(JsonArray array, SequenceFunction<JsonElement, E> function)
	{
		List<E> list = new ArrayList<>();
		function.andThen((Consumer<E>) list::add).accept(array);
		return list;
	}
	
	public static JsonArray toJsonArray(byte[] array)
	{
		JsonArray result = new JsonArray();
		for (byte value : array)
			result.add(new JsonPrimitive(value));
		return result;
	}
	
	public static JsonArray toJsonArray(int[] array)
	{
		JsonArray result = new JsonArray();
		for (int value : array)
			result.add(new JsonPrimitive(value));
		return result;
	}
	
	public static JsonArray toJsonArray(char[] array)
	{
		JsonArray result = new JsonArray();
		for (char value : array)
			result.add(new JsonPrimitive(value));
		return result;
	}
	
	public static JsonArray toJsonArray(float[] array)
	{
		return toJsonArray(array, 0, array.length);
	}
	
	public static JsonArray toJsonArray(float[] array, int start, int end)
	{
		JsonArray result = new JsonArray();
		float value;
		for (int i = start; i < end; value = array[i++], result.add(new JsonPrimitive(value)));
		return result;
	}
	
	public static JsonArray toJsonArray(double[] array)
	{
		JsonArray result = new JsonArray();
		for (double value : array)
			result.add(new JsonPrimitive(value));
		return result;
	}
	
	public static JsonArray toJsonArray(String[] array)
	{
		JsonArray result = new JsonArray();
		for (String value : array)
			result.add(new JsonPrimitive(value));
		return result;
	}
}
