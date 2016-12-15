/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ObjectArrays;

import farcore.lib.util.IDataChecker;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author ueyudiud
 */
public class L
{
	private static final Random RNG = new Random();
	
	public static int bitCounts(byte value)
	{
		int c = 0;
		while (value != 0)
		{
			if((value & 0x1) != 0)
			{
				++c;
			}
			value >>= 1;
		}
		return c;
	}
	
	public static boolean similar(float a, float b)
	{
		a -= b;
		return a > -1E-5F && a < 1E-5F;
	}
	
	public static boolean similar(double a, double b)
	{
		a -= b;
		return a > -1E-5 && a < 1E-5;
	}
	
	public static int unsignedToInt(byte value)
	{
		return (value & 0xFF);
	}
	
	public static int compareUnsigned(byte v1, byte v2)
	{
		return unsignedToInt(v1) - unsignedToInt(v2);
	}
	
	public static int index8i(int x, int y, int z)
	{
		return z << 4 | y << 2 | x;
	}
	
	public static int index12i(int x, int y, int z)
	{
		return z << 8 | y << 4 | x;
	}
	
	public static double cast(@Nullable Double d)
	{
		return d == null ? 0 : d.doubleValue();
	}
	
	public static float cast(@Nullable Float f)
	{
		return f == null ? 0 : f.floatValue();
	}
	
	public static int cast(@Nullable Integer integer)
	{
		return integer == null ? 0 : integer.intValue();
	}
	
	public static short cast(Short short1)
	{
		return short1 == null ? 0 : short1.shortValue();
	}
	
	public static int castPositive(byte val)
	{
		return (val & 0xFF);
	}
	
	public static Random random()
	{
		return RNG;
	}
	
	public static void exit()
	{
		exit(0, false);
	}
	
	public static void exit(int code, boolean hardExit)
	{
		FMLCommonHandler.instance().exitJava(code, hardExit);
	}
	
	public static <T> T[] cast(Collection<? extends T> collection, Class<T> clazz)
	{
		T[] ret = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(ret);
	}
	
	public static <T> ImmutableList<T> castImmutable(T...list)
	{
		return ImmutableList.copyOf(list);
	}
	
	public static <T> ArrayList<T> castArray(T...list)
	{
		if(list == null || list.length == 0) return new ArrayList();
		return new ArrayList(Arrays.asList(list));
	}
	
	public static <K, T> T[] transform(K[] array, Class<T> elementClass, Function<K, T> function)
	{
		T[] result = ObjectArrays.newArray(elementClass, array.length);
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	public static <K, V> void putAll(Map<K, V> map, Collection<? extends K> collection, Function<? super K, ? extends V> function)
	{
		for(K key : collection) { map.put(key, function.apply(key)); }
	}
	
	public static <K, V> void putAll(Map<K, V> map, Collection<? extends K> collection, V constant)
	{
		for(K key : collection) { map.put(key, constant); }
	}
	
	public static <K, V> void put(Map<K, List<V>> map, K key, V value)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			map.put(key, list = new ArrayList());
		}
		list.add(value);
	}
	
	public static <K, V> void put(Map<K, List<V>> map, K key, V...values)
	{
		if(values.length == 0) return;
		if(values.length == 1) put(map, key, values[0]);
		else put(map, key, Arrays.<V>asList(values));
	}
	
	public static <K, V> void put(Map<K, List<V>> map, K key, Collection<? extends V> values)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			map.put(key, list = new ArrayList(values));
		}
		else
		{
			list.addAll(list);
		}
	}
	
	public static <K, V> boolean remove(Map<K, List<V>> map, K key, V value)
	{
		List<V> list = map.get(key);
		return list != null ? list.remove(value) : false;
	}
	
	public static <K, V> Function<K, V> toFunction(Map<K, V> map)
	{
		return key -> map.get(key);
	}
	
	public static <K, V> Function<K, V> toFunction(Map<K, V> map, V defaultValue)
	{
		return key -> map.getOrDefault(key, defaultValue);
	}
	
	public static <T> boolean contain(Collection<? extends T> collection, IDataChecker<T> checker)
	{
		if(collection == null || collection.isEmpty()) return false;
		for(T target : collection)
			if(checker.isTrue(target)) return true;
		return false;
	}
	
	public static <T> Set<T> containSet(Collection<? extends T> collection, IDataChecker<T> checker)
	{
		if(collection == null || collection.isEmpty()) return ImmutableSet.of();
		Builder<T> builder = ImmutableSet.builder();
		for(T target : collection)
			if(checker.isTrue(target))
			{
				builder.add(target);
			}
		return builder.build();
	}
	
	public static <T> T random(T...list)
	{
		return random(list, RNG);
	}
	
	public static <T> T random(Random random, T...list)
	{
		return random(list, random);
	}
	
	public static char random(Random random, char...list)
	{
		return list == null || list.length == 0 ? null :
			list.length == 1 ? list[0] :
				list[random.nextInt(list.length)];
	}
	
	public static <T> T random(T[] list, Random random)
	{
		return list == null || list.length == 0 ? null :
			list.length == 1 ? list[0] :
				list[random.nextInt(list.length)];
	}
	
	public static <T> T random(Collection<T> collection, Random random)
	{
		if(collection instanceof List)
			return (T) ((List) collection).get(random.nextInt(((List) collection).size()));
		else
			return (T) random(collection.toArray(), random);
	}
	
	public static int[] fillIntArray(int length, int value)
	{
		switch(length)
		{
		case 0 : return new int[0];
		case 1 : return new int[]{value};
		default:
			int[] ret = new int[length];
			Arrays.fill(ret, value);
			return ret;
		}
	}
	
	public static boolean equal(@Nullable Object arg1, @Nullable Object arg2)
	{
		return arg1 == arg2 ? true :
			(arg1 == null ^ arg2 == null) ? false :
				arg1.equals(arg2);
	}
	
	public static int min(int...values)
	{
		int ret = Integer.MAX_VALUE;
		for(int i : values) if(i < ret) ret = i;
		return ret;
	}
	
	public static float min(float...values)
	{
		float ret = Float.MAX_VALUE;
		for(float i : values) if(i < ret) ret = i;
		return ret;
	}
	
	public static int max(int...values)
	{
		int ret = Integer.MIN_VALUE;
		for(int i : values) if(i > ret) ret = i;
		return ret;
	}
	
	public static float max(float...values)
	{
		float ret = Float.MIN_VALUE;
		for(float i : values) if(i > ret) ret = i;
		return ret;
	}
	
	public static int range(int m1, int m2, int target)
	{
		int v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	public static float range(float m1, float m2, float target)
	{
		float v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	public static double range(double m1, double m2, double target)
	{
		double v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	public static boolean inRange(double max, double min, double target)
	{
		return target <= max && target >= min;
	}
	
	public static int nextInt(int bound)
	{
		return nextInt(bound, RNG);
	}
	
	public static int nextInt(int bound, Random rand)
	{
		return bound <= 0 ? bound : rand.nextInt(bound);
	}
}