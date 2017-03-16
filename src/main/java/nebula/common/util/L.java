/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntConsumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Table;

import nebula.common.base.Judgable;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * The basic helper.
 * @author ueyudiud
 */
public class L
{
	/**
	 * General random number generator.
	 * @see java.util.Random
	 */
	private static final Random RNG = new Random();
	
	/**
	 * sqrt(2) / 2 value, used in some calculation.
	 */
	public static final double SQRT_DIV2 = 0.707106781186;
	
	/**
	 * Count enabled bit size.
	 * @param value
	 * @return
	 */
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
	
	/**
	 * Is two value similar.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean similar(float a, float b)
	{
		a -= b;
		return a > -1E-5F && a < 1E-5F;
	}
	
	/**
	 * Is two value similar.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean similar(double a, double b)
	{
		a -= b;
		return a > -1E-5 && a < 1E-5;
	}
	
	/**
	 * Cast value as ubyte (range from 0~255).
	 * @param value
	 * @return
	 */
	public static int unsignedToInt(byte value)
	{
		return (value & 0xFF);
	}
	
	/**
	 * Return v1 - v2 result as an integer.
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static int minusUbyte(byte v1, byte v2)
	{
		return unsignedToInt(v1) - unsignedToInt(v2);
	}
	
	/**
	 * Combine quarter byte data.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int index8i(int x, int y, int z)
	{
		return z << 4 | y << 2 | x;
	}
	
	/**
	 * Combine half byte data.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int index12i(int x, int y, int z)
	{
		return z << 8 | y << 4 | x;
	}
	
	/**
	 * Cast double value.
	 * @param d
	 * @return
	 */
	public static double cast(@Nullable Double d)
	{
		return d == null ? 0 : d.doubleValue();
	}
	
	/**
	 * Cast float value.
	 * @param f
	 * @return
	 */
	public static float cast(@Nullable Float f)
	{
		return f == null ? 0 : f.floatValue();
	}
	
	public static int cast(@Nullable Integer integer)
	{
		return integer == null ? 0 : integer.intValue();
	}
	
	public static short cast(@Nullable Short short1)
	{
		return short1 == null ? 0 : short1.shortValue();
	}
	
	/**
	 * Cast value as ubyte to int.
	 * @param val
	 * @return
	 */
	public static int castPositive(byte val)
	{
		return (val & 0xFF);
	}
	
	/**
	 * Get default random number generator.
	 * @return
	 */
	public static Random random()
	{
		return RNG;
	}
	
	/**
	 * Exit game.
	 */
	public static void exit()
	{
		exit(0, false);
	}
	
	public static void exit(int code, boolean hardExit)
	{
		FMLCommonHandler.instance().exitJava(code, hardExit);
	}
	
	/**
	 * Cast collection to an array.
	 * @param collection The casting col.
	 * @param clazz The result array class type.
	 * @return
	 */
	public static <T> T[] cast(Collection<? extends T> collection, Class<T> clazz)
	{
		T[] ret = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(ret);
	}
	
	/**
	 * Cast as an ArrayList.
	 * @param list
	 * @return
	 */
	public static <T> ArrayList<T> castArray(T...list)
	{
		if(list == null || list.length == 0) return new ArrayList();
		return new ArrayList(Arrays.asList(list));
	}
	
	/**
	 * Put transformed element into map.
	 * @param map
	 * @param collection
	 * @param function
	 */
	public static <K, V> void putAll(Map<K, V> map, Collection<? extends K> collection, Function<? super K, ? extends V> function)
	{
		collection.forEach(k->map.put(k, function.apply(k)));
	}
	
	public static <K, V> void putAll(Map<K, V> map, Collection<? extends K> collection, V constant)
	{
		collection.forEach(k->map.put(k, constant));
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
	
	//=============================Fake multimap method start================================
	
	/**
	 * Put numerous values into fake Multimap.
	 * @param map
	 * @param key
	 * @param values
	 * @see com.google.common.collect.Multimap
	 */
	public static <K, V> void put(Map<K, List<V>> map, K key, V...values)
	{
		switch (values.length)
		{
		case 0 : return;
		case 1 : put(map, key, values[0]);
		break;
		default: put(map, key, Arrays.<V>asList(values));
		break;
		}
	}
	
	/**
	 * Put all values in collection into fake Multimap
	 * @param map
	 * @param key
	 * @param values
	 * @see com.google.common.collect.Multimap#putAll(com.google.common.collect.Multimap)
	 */
	public static <K, V> void put(Map<K, List<V>> map, K key, Collection<? extends V> values)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			map.put(key, list = new ArrayList(values));
		}
		else
		{
			list.addAll(values);
		}
	}
	
	/**
	 * Remove single value from fake Multimap
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.Multimap#remove(Object, Object)
	 */
	public static <K, V> boolean remove(Map<K, List<V>> map, K key, V value)
	{
		List<V> list = map.get(key);
		return list != null ? list.remove(value) : false;
	}
	
	/**
	 * Check is value belong the set of the key mapped by fake Multimap
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.Multimap#containsEntry(Object, Object)
	 */
	public static <K, V> boolean contain(Map<K, List<V>> map, K key, V value)
	{
		return map.containsKey(key) && map.get(key).contains(value);
	}
	
	//==============================Fake multimap method end=================================
	
	/**
	 * Put a value into two-layer map.
	 * @param map
	 * @param key
	 * @param value1
	 * @param value2
	 */
	public static <K, V1, V2> void put(Map<K, Map<V1, V2>> map, K key, V1 value1, V2 value2)
	{
		Map<V1, V2> m = map.get(key);
		if(m == null)
		{
			map.put(key, m = new HashMap(ImmutableMap.of(value1, value2)));
		}
		else
		{
			m.put(value1, value2);
		}
	}
	
	/**
	 * Get a map from two-layer value map or create a new HashMap.
	 * @param map
	 * @param key
	 * @return
	 */
	public static <K, K1, V1> Map<K1, V1> getOrPut(Map<K, Map<K1, V1>> map, K key)
	{
		Map<K1, V1> map2 = map.get(key);
		if(map2 == null)
		{
			map.put(key, map2 = new HashMap());
		}
		return map2;
	}
	
	//====================================Functional method start==================================
	
	/**
	 * Cast a map as a function, the code is like this.<p>
	 * <code>
	 * T apply(K key) {<br>
	 *  return map.get(key);<br>
	 * }<br>
	 * </code>
	 * @param map
	 * @return The value store in map.
	 * @see java.util.Map#get(Object)
	 */
	public static <K, V> Function<K, V> toFunction(Map<K, V> map)
	{
		return map::get;
	}
	
	/**
	 * Cast a map as a function, the code is like this.<p>
	 * <code>
	 * T defaultResult;
	 * T apply(K key) {<br>
	 *  return map.getOrDefault(key, defaultResult);<br>
	 * }<br>
	 * </code>
	 * @param map
	 * @param defaultValue The default result of function, it will return when key element does not contain in map.
	 * @return
	 * @see java.util.Map#getOrDefault(Object, Object)
	 */
	public static <K, V> Function<K, V> toFunction(Map<K, V> map, V defaultValue)
	{
		return key -> map.getOrDefault(key, defaultValue);
	}
	
	//=====================================Functional method end===================================
	
	public static <T> boolean contain(Collection<? extends T> collection, Judgable<T> checker)
	{
		if(collection == null || collection.isEmpty()) return false;
		for(T target : collection) if(checker.isTrue(target)) return true;
		return false;
	}
	
	public static <T> T get(Optional<T> optional, T def)
	{
		return optional.isPresent() ? optional.get() : def;
	}
	
	public static <T> T get(com.google.common.base.Optional<T> optional, T def)
	{
		return optional.isPresent() ? optional.get() : def;
	}
	
	public static <T> T get(Collection<? extends T> collection, Judgable<T> judgable)
	{
		if (collection == null || collection.isEmpty()) return null;
		for (T target : collection) if (judgable.isTrue(target)) return target;
		return null;
	}
	
	public static <T> Set<T> containSet(Collection<? extends T> collection, Judgable<T> checker)
	{
		if(collection == null || collection.isEmpty()) return ImmutableSet.of();
		Builder<T> builder = ImmutableSet.builder();
		collection.forEach(t-> { if (checker.isTrue(t)) builder.add(t); });
		return builder.build();
	}
	
	/**
	 * Select an element in list randomly.<p>
	 * Will use general random number generator for random generate.
	 * @param list
	 * @return
	 */
	public static <T> T random(T...list)
	{
		return random(list, RNG);
	}
	
	/**
	 * Select an element in list randomly.
	 * @param random The select random generator.
	 * @param list
	 * @return
	 */
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
	
	/**
	 * Select element randomly.
	 * @param list
	 * @param random
	 * @return Return random element in list, if list length is 0, return null as result.
	 */
	public static <T> T random(@Nullable T[] list, Random random)
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
		{
			switch (collection.size())
			{
			case 0 : return null;
			case 1 : return Iterables.getOnlyElement(collection);
			default: return Iterators.get(collection.iterator(), random.nextInt(collection.size()));
			}
		}
	}
	
	public static boolean equal(@Nullable Object arg1, @Nullable Object arg2)
	{
		return arg1 == arg2 ? true :
			(arg1 == null ^ arg2 == null) ? false :
				arg1.equals(arg2);
	}
	
	/**
	 * Get minimum values from integer array.
	 * @param values
	 * @return
	 */
	public static int min(int...values)
	{
		int ret = Integer.MAX_VALUE;
		for(int i : values) if(i < ret) ret = i;
		return ret;
	}
	
	/**
	 * Get minimum values from float array.
	 * @param values
	 * @return
	 */
	public static float min(float...values)
	{
		float ret = Float.MAX_VALUE;
		for(float i : values) if(i < ret) ret = i;
		return ret;
	}
	
	/**
	 * Get max values from integer array.
	 * @param values
	 * @return
	 */
	public static int max(int...values)
	{
		int ret = Integer.MIN_VALUE;
		for(int i : values) if(i > ret) ret = i;
		return ret;
	}
	
	/**
	 * Get max values from float array.
	 * @param values
	 * @return
	 */
	public static float max(float...values)
	{
		float ret = Float.MIN_VALUE;
		for(float i : values) if(i > ret) ret = i;
		return ret;
	}
	
	/**
	 * Get integer ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 The first max or minimum number.
	 * @param m2 The second max or minimum number.
	 * @param target The ranged number.
	 * @return
	 */
	public static int range(int m1, int m2, int target)
	{
		int v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Get float ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 The first max or minimum number.
	 * @param m2 The second max or minimum number.
	 * @param target The ranged number.
	 * @return
	 */
	public static float range(float m1, float m2, float target)
	{
		float v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Get double ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 The first max or minimum number.
	 * @param m2 The second max or minimum number.
	 * @param target The ranged number.
	 * @return
	 */
	public static double range(double m1, double m2, double target)
	{
		double v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Consume a ranged number from 0 to selected length (exclude right bound).<p>
	 * @param len
	 * @param consumer
	 */
	public static void consume(int len, IntConsumer consumer)
	{
		for (int i = 0; i < len; consumer.accept(i++));
	}
	
	/**
	 * Consume a ranged number from a to b (exclude right bound and include left bound).<p>
	 * @param start
	 * @param end
	 * @param consumer
	 */
	public static void consume(int start, int end, IntConsumer consumer)
	{
		for (int i = start; i < end; consumer.accept(i++));
	}
	
	/**
	 * Match is number in range(Include left and right bound).
	 * @param max
	 * @param min
	 * @param target
	 * @return
	 */
	public static boolean inRange(double max, double min, double target)
	{
		return target <= max && target >= min;
	}
	
	/**
	 * Generate a random number by general random number generator.
	 * @param bound
	 * @return
	 * @see java.util.Random#nextInt(int)
	 */
	public static int nextInt(int bound)
	{
		return nextInt(bound, RNG);
	}
	
	/**
	 * 
	 * @param bound
	 * @param rand
	 * @return
	 */
	public static int nextInt(int bound, Random rand)
	{
		if (bound < 0) throw new IllegalArgumentException("The bound must be possitive number!");
		switch (bound)
		{
		case 0 :
		case 1 : return 0;
		default: return rand.nextInt(bound);
		}
	}
	
	@SuppressWarnings("hiding")
	public static <R, C, V> V getOrDefault(Table<R, C, V> table, R rowKey,
			C columnKey, V defaultValue)
	{
		return table.contains(rowKey, columnKey) ? table.get(rowKey, columnKey) : defaultValue;
	}
	
	public static <E> ArrayList<E> castToArrayListOrWrap(Collection<?> col)
	{
		return col instanceof ArrayList ? (ArrayList<E>) col : new ArrayList(col);
	}
	
	public static int indexOf(int[] list, int arg)
	{
		for(int i = 0; i < list.length; ++i) if(list[i] == arg) return i;
		return -1;
	}
	
	public static <S, K, T> Function<S, T> withCastIn(Function<K, T> function, Class<K> clazz)
	{
		return resource -> function.apply((K) resource);
	}
	
	public static <K, M, T> Function<K, T> withCastOut(Function<K, M> function, Class<T> clazz)
	{
		return resource -> (T) function.apply(resource);
	}
}