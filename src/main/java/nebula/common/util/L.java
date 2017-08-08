/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Table;

import nebula.base.Judgable;
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
	 * <tt>sqrt(2)</tt>, used in some calculation.
	 */
	public static final double SQRT2 = 1.4142135623730951;
	
	/**
	 * Throw an exception.
	 * @param <X> the fake result type.
	 * @param t the exception.
	 * @throws java.lang.RuntimeException
	 */
	public static <X> X throwWhenGet(RuntimeException t)
	{
		throw t;
	}
	
	/**
	 * Count enabled bit size.
	 * @param value
	 * @return size of enabled bit size, <tt>0x29</tt> will return <tt>3</tt> for example.
	 */
	public static int bitCounts(byte value)
	{
		int c = 0;
		while (value != 0)
		{
			if((value & 0x1) != 0)
				++c;
			value >>= 1;
		}
		return c;
	}
	
	/**
	 * Return <tt>true</tt> if two value are similar.<br>
	 * Use <tt>|a-b|<1.0^10-5</tt> to check.
	 */
	public static boolean similar(float a, float b)
	{
		a -= b;
		return a > -1E-5F && a < 1E-5F;
	}
	
	/**
	 * Return <tt>true</tt> if two value are similar.<br>
	 * Use <tt>|a-b|<1.0^10-10</tt> to check.
	 */
	public static boolean similar(double a, double b)
	{
		a -= b;
		return a > -1E-10 && a < 1E-10;
	}
	
	/**
	 * Cast value as ubyte (range from 0~255).
	 * @param value
	 * @return the unsigned int value.
	 */
	public static int uint(byte value)
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
		return uint(v1) - uint(v2);
	}
	
	/**
	 * Combine quarter byte data.
	 * @param x first value.
	 * @param y second value.
	 * @param z third value.
	 * @return the combined data.
	 */
	public static int index8i(int x, int y, int z)
	{
		return (x & 0x3) << 4 | (y & 0x3) << 2 | (z & 0x3);
	}
	
	/**
	 * Combine half byte data.
	 * @param x first value.
	 * @param y second value.
	 * @param z third value.
	 * @return the combined data.
	 */
	public static int index12i(int x, int y, int z)
	{
		return (x & 0xF) << 8 | (y & 0xF) << 4 | (z & 0xF);
	}
	
	/**
	 * Combine byte data.
	 * @param x first value.
	 * @param y second value.
	 * @param z third value.
	 * @return the combined data.
	 */
	public static int index24i(int x, int y, int z)
	{
		return (x & 0xFF) << 16 | (y & 0xFF) << 8 | (z & 0xFF);
	}
	
	/**
	 * Cast {@link java.lang.Double} value to <tt>double</tt>
	 * value safety.<p>
	 * If argument is <tt>null</tt>, the result will be 0.
	 * @param d
	 * @return casted value.
	 */
	public static double cast(@Nullable Double d)
	{
		return d == null ? 0 : d.doubleValue();
	}
	
	/**
	 * Cast {@link java.lang.Float} value to <tt>float</tt>
	 * value safety.<p>
	 * If argument is <tt>null</tt>, the result will be 0.
	 * @param d
	 * @return casted value.
	 */
	public static float cast(@Nullable Float f)
	{
		return f == null ? 0 : f.floatValue();
	}
	
	/**
	 * Cast {@link java.lang.Integer} value to <tt>int</tt>
	 * value safety.<p>
	 * If argument is <tt>null</tt>, the result will be 0.
	 * @param i
	 * @return casted value.
	 */
	public static int cast(@Nullable Integer i)
	{
		return i == null ? 0 : i.intValue();
	}
	
	/**
	 * Cast {@link java.lang.Short} value to <tt>short</tt>
	 * value safety.<p>
	 * If argument is <tt>null</tt>, the result will be 0.
	 * @param s
	 * @return casted value.
	 */
	public static short cast(@Nullable Short s)
	{
		return s == null ? 0 : s.shortValue();
	}
	
	/**
	 * Cast object to any type, use to casting wildcard.
	 * @param <C> a cast type.
	 * @param arg a casting argument.
	 * @return the casted value (Just the argument input).
	 */
	@SuppressWarnings("unchecked")
	public static <C> C castAny(@Nullable Object arg)
	{
		return (C) arg;
	}
	
	/**
	 * Cast value as <tt>unsigned byte</tt> to <tt>int</tt>.
	 * @param val
	 * @return the unsigned byte value.
	 */
	public static int castPositive(byte val)
	{
		return (val & 0xFF);
	}
	
	/**
	 * Get default random number generator.
	 * @return random number generator.
	 */
	public static Random random()
	{
		return RNG;
	}
	
	/**
	 * Exit game.
	 * @see net.minecraftforge.fml.common.FMLCommonHandler#exitJava(int, boolean)
	 */
	public static void exit()
	{
		exit(0, false);
	}
	
	/**
	 * Exit game with exit code.
	 * @param code
	 * @param hardExit
	 * @see net.minecraftforge.fml.common.FMLCommonHandler#exitJava(int, boolean)
	 */
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
	public static <T> T[] cast(@Nonnull Collection<? extends T> collection, @Nonnull Class<T> clazz)
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
	public static <K, V> void putAll(@Nonnull Map<K, V> map, @Nonnull Collection<? extends K> collection, @Nonnull Function<? super K, ? extends V> function)
	{
		collection.forEach(k->map.put(k, function.apply(k)));
	}
	
	public static <K, V> void putAll(@Nonnull Map<K, V> map, @Nonnull Collection<? extends K> collection, @Nullable V constant)
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
	 * @param <K> key type.
	 * @param <V> value type.
	 * @param map a fake multimap.
	 * @param key
	 * @param values
	 * @see com.google.common.collect.Multimap
	 */
	public static <K, V> void put(@Nonnull Map<K, List<V>> map, @Nullable K key, V...values)
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
	 * @param <K> key type.
	 * @param <V> value type.
	 * @param map a fake multimap.
	 * @param key
	 * @param values
	 * @see com.google.common.collect.Multimap#putAll(com.google.common.collect.Multimap)
	 */
	public static <K, V> void put(@Nonnull Map<K, List<V>> map, @Nullable K key, Collection<? extends V> values)
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
	 * @param <K> key type.
	 * @param <V> value type.
	 * @param map a fake multimap.
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.Multimap#remove(Object, Object)
	 */
	public static <K, V> boolean remove(@Nonnull Map<K, List<V>> map, @Nullable K key, @Nullable V value)
	{
		List<V> list = map.get(key);
		return list != null ? list.remove(value) : false;
	}
	
	/**
	 * Check is value belong the set of the key mapped by fake Multimap
	 * @param <K> key type.
	 * @param <V> value type.
	 * @param map a fake multimap.
	 * @param key key.
	 * @param value value in values collection.
	 * @return
	 * @see com.google.common.collect.Multimap#containsEntry(Object, Object)
	 */
	public static <K, V> boolean contain(@Nonnull Map<K, List<V>> map, @Nullable K key, @Nullable V value)
	{
		return map.containsKey(key) && map.get(key).contains(value);
	}
	
	//==============================Fake multimap method end=================================
	
	/**
	 * Put a value into two-layer map.
	 * @param map two-layer map.
	 * @param key first key of map.
	 * @param value1 second key of map (The key in value map of first map).
	 * @param value2 value of map (The value in value map of first map).
	 * @see com.google.common.collect.Table#put(Object, Object, Object)
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
	 * Get a map from two-layer value map or create a new {@link java.util.HashMap}.
	 * @param map two-layer map.
	 * @param key first key of map.
	 * @return the value map of first key,
	 * a new hashmap will be put if not present entry with key.
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
	 * T defaultResult;<br>
	 * T apply(K key) = map.getOrDefault(key, defaultResult);
	 * </code><p>
	 * Uses when there are only limited elements in function.
	 * @param map The function mapping.
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
	 * Select an element in list randomly.<p>
	 * All elements has same chance to be select.
	 * @param random The select random generator.
	 * @param list
	 * @return
	 */
	public static <T> T random(Random random, T...list)
	{
		return random(list, random);
	}
	
	/**
	 * Select element randomly.<p>
	 * All elements has same chance to be select.
	 * @param list
	 * @param random
	 * @return Return random element in list, if list length is 0, return null as result.
	 */
	public static <T> T random(@Nullable T[] list, Random random)
	{
		return list == null || list.length == 0 ? null : list[list.length == 1 ? 0 : random.nextInt(list.length)];
	}
	
	/**
	 * Select element randomly.<p>
	 * All elements has same chance to be select.
	 * @param collection
	 * @param random
	 * @return Return random element in collection, if list length is 0, return null as result.
	 */
	public static <T> T random(Collection<T> collection, Random random)
	{
		if(collection instanceof List)
			return ((List<?>) collection).size() == 0 ? null :
				(T) ((List) collection).get(random.nextInt(((List) collection).size()));
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
	
	/**
	 * Match two element.<p>
	 * Return {@code true} if two argument are equal. Different from
	 * {@link java.util.Objects#equals(Object, Object)}, the argument input
	 * that method will be non-null.
	 * @param arg1
	 * @param arg2
	 * @return
	 * @see java.util.Objects#equals(Object, Object)
	 */
	public static boolean equal(@Nullable Object arg1, @Nullable Object arg2)
	{
		return arg1 == arg2 ? true : (arg1 == null ^ arg2 == null) ? false : arg1.equals(arg2);
	}
	
	/**
	 * Get minimum values from {@code int} array.
	 * @param values a int array.
	 * @return if array length is 0, the result will be {@link Integer#MAX_VALUE}
	 */
	public static int min(int...values)
	{
		int ret = Integer.MAX_VALUE;
		for(int i : values) if(i < ret) ret = i;
		return ret;
	}
	
	/**
	 * Get minimum values from {@code float} array.
	 * @param values a int array.
	 * @return if array length is 0, the result will be {@link Float#MAX_VALUE}
	 */
	public static float min(float...values)
	{
		float ret = Float.MAX_VALUE;
		for(float i : values) if(i < ret) ret = i;
		return ret;
	}
	
	/**
	 * Get max values from {@code int} array.
	 * @param values a int array.
	 * @return if array length is 0, the result will be {@link Integer#MIN_VALUE}
	 */
	public static int max(int...values)
	{
		int ret = Integer.MIN_VALUE;
		for(int i : values) if(i > ret) ret = i;
		return ret;
	}
	
	/**
	 * Get max values from {@code float} array.
	 * @param values a float array.
	 * @return if array length is 0, the result will be {@link Float#MIN_VALUE}
	 */
	public static float max(float...values)
	{
		float ret = Float.MIN_VALUE;
		for(float i : values) if(i > ret) ret = i;
		return ret;
	}
	
	/**
	 * Get {@code int} ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 the first max or minimum number.
	 * @param m2 the second max or minimum number.
	 * @param target the ranged number.
	 * @return
	 */
	public static int range(int m1, int m2, int target)
	{
		int v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Get {@code long} value ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 the first max or minimum number.
	 * @param m2 the second max or minimum number.
	 * @param target the ranged number.
	 * @return
	 */
	public static long range(long m1, long m2, long target)
	{
		long v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Get {@code float} ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 the first max or minimum number.
	 * @param m2 the second max or minimum number.
	 * @param target the ranged number.
	 * @return
	 */
	public static float range(float m1, float m2, float target)
	{
		float v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Get {@code double} ranged in a and b (include a and b).<p>
	 * If number is out of bound, return minimum number if value is
	 * lower than minimum number or return max number if value is
	 * higher than max number.
	 * @param m1 the first max or minimum number.
	 * @param m2 the second max or minimum number.
	 * @param target the ranged number.
	 * @return
	 */
	public static double range(double m1, double m2, double target)
	{
		double v;
		return target > (v = Math.max(m1, m2)) ? v :
			target < (v = Math.min(m1, m2)) ? v : target;
	}
	
	/**
	 * Accept a ranged number from 0 to selected length (exclude right bound).<p>
	 * @param len length to stop (exclude itself).
	 * @param consumer to accept value.
	 */
	public static void consume(int len, IntConsumer consumer)
	{
		consume(0, len, consumer);
	}
	
	/**
	 * Consume a ranged number from a to b (exclude right bound and include left bound).<p>
	 * The start value should be more than end value.
	 * @param start value in start (include itself).
	 * @param end value in end (exclude itself).
	 * @param consumer to accept value.
	 * @throws IllegalArgumentException if start is less than end.
	 */
	public static void consume(int start, int end, IntConsumer consumer)
	{
		if (start > end)
			throw new IllegalArgumentException(start + " to " + end + " does not contain any number.");
		for (int i = start; i < end; consumer.accept(i++));
	}
	
	/**
	 * Match is number in range(Include left and right bound).<p>
	 * Return {@code true} if target is less equal than max value
	 * and great equal than minimum value.
	 * @param max a max {@code double} value.
	 * @param min a minimum {@code double} value.
	 * @param target value to be check.
	 * @return
	 */
	public static boolean inRange(double max, double min, double target)
	{
		return target <= max && target >= min;
	}
	
	/**
	 * Match is number in range(Include left and right bound).<p>
	 * Return {@code true} if target is less equal than max value
	 * and great equal than minimum value.
	 * @param max a max {@code float} value.
	 * @param min a minimum {@code float} value.
	 * @param target value to be check.
	 * @return
	 */
	public static boolean inRange(float max, float min, float target)
	{
		return target <= max && target >= min;
	}
	
	/**
	 * Match is number in range(Include left and right bound).<p>
	 * Return {@code true} if target is less equal than max value
	 * and great equal than minimum value.
	 * @param max a max {@code int} value.
	 * @param min a minimum {@code int} value.
	 * @param target value to be check.
	 * @return
	 */
	public static boolean inRange(int max, int min, int target)
	{
		return target <= max && target >= min;
	}
	
	/**
	 * Generate a random {@code int} value by general random number generator.<p>
	 * If bound is zero or one, it will always return 0 as result.
	 * @param bound the max value (exclude itself) can be return.
	 * @return the random number.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int nextInt(int bound)
	{
		return nextInt(bound, RNG);
	}
	
	/**
	 * Generate a random {@code int} value by specific random number generator.<p>
	 * If bound is zero or one, it will always return 0 as result.
	 * @param bound the max value (exclude itself) can be return.
	 * @param rand a random number generator.
	 * @return the random number.
	 * @see java.util.Random#nextInt(int)
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
	
	/**
	 * Get value or return default value from table.
	 * @param table
	 * @param rowKey
	 * @param columnKey
	 * @param defaultValue
	 * @see com.google.common.collect.Table#get(Object, Object)
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <R, C, V> V getOrDefault(Table<R, C, V> table, R rowKey,
			C columnKey, V defaultValue)
	{
		return table.contains(rowKey, columnKey) ? table.get(rowKey, columnKey) : defaultValue;
	}
	
	/**
	 * Wrap collection as a {@link java.util.ArrayList} for mutable,
	 * or return argument directly if it is instance of {@link java.util.ArrayList}.
	 * @param col a collection.
	 * @return
	 */
	public static <E> ArrayList<E> castToArrayListOrWrap(Collection<?> col)
	{
		return col instanceof ArrayList ? (ArrayList<E>) col : new ArrayList(col);
	}
	
	/**
	 * Cast wildcard type to specific type.
	 * @param <S> transformed function input type.
	 * @param <K> function in argument input type.
	 * @param <T> function output type.
	 * @param function
	 * @return
	 */
	public static <S, K, T> Function<S, T> withCastIn(Function<K, T> function)
	{
		return resource -> function.apply((K) resource);
	}
	
	/**
	 * Cast wildcard type to specific type.
	 * @param <K> function input type.
	 * @param <M> function in argument output type.
	 * @param <T> transformed function output type.
	 * @param function
	 * @return
	 */
	public static <K, M, T> Function<K, T> withCastOut(Function<K, M> function)
	{
		return resource -> (T) function.apply(resource);
	}
	
	/**
	 * Collect elements from iterator.
	 * @param iterable The iterator provider.
	 * @param func The transform function.
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <R, T> Set<R> collect(Iterable<? extends T> iterable, BiConsumer<T, Collection<R>> consumer)
	{
		Set<R> set = new HashSet<>();
		for (T t : iterable)
		{
			consumer.accept(t, set);
		}
		return set;
	}
}