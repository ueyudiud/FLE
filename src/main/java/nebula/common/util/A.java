/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.ObjIntConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ObjectArrays;

import nebula.common.base.Judgable;

/**
 * @author ueyudiud
 */
public final class A
{
	private A() {}
	
	/**
	 * Copy array elements to a new array with selected length.
	 * @param array The source array.
	 * @param len The length of new array, use old array length if the select length is smaller than old array length.
	 * @return The copied array.
	 */
	public static int[] copyToLength(int[] array, int len)
	{
		int[] result = new int[len];
		System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
		return result;
	}
	
	/**
	 * Copy array elements to a new array with selected length.
	 * The result array type is same to old array type.
	 * @param array
	 * @param len
	 * @return
	 */
	public static <T> T[] copyToLength(T[] array, int len)
	{
		T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), len);
		System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
		return result;
	}
	
	/**
	 * Given action to every element in array.
	 * @see java.util.Collection#forEach(Consumer)
	 * @param iterable
	 * @param consumer
	 */
	public static <E> void executeAll(E[] iterable, Consumer<E> consumer)
	{
		for(E element : iterable) consumer.accept(element);
	}
	
	/**
	 * Given action to every element in array.
	 * @see java.util.Collection#forEach(Consumer)
	 * @param iterable
	 * @param consumer
	 */
	public static <E> void executeAll(E[] iterable, ObjIntConsumer<E> consumer)
	{
		for(int i = 0; i < iterable.length; ++i) consumer.accept(iterable[i], i);
	}
	
	/**
	 * Match same element in array, use {@code L.equal(element, arg)} to
	 * match objects.
	 * @param list The given array.
	 * @param arg The matched argument.
	 * @return
	 */
	public static <E> boolean contain(E[] list, @Nullable E arg)
	{
		for(E element : list) if(L.equal(element, arg)) return true;
		return false;
	}
	
	public static boolean contain(int[] list, int arg)
	{
		for(int element : list) if(element == arg) return true;
		return false;
	}
	
	public static boolean contain(char[] list, char arg)
	{
		for(char element : list) if(element == arg) return true;
		return false;
	}
	
	/**
	 * Match is target
	 * @param list
	 * @param checker
	 * @return
	 */
	public static <E> boolean and(E[] list, Judgable<E> checker)
	{
		for(E element : list) if (!checker.isTrue(element)) return false;
		return true;
	}
	
	public static <E> boolean and(int[] list, IntPredicate predicate)
	{
		for(int element : list) if (!predicate.test(element)) return false;
		return true;
	}
	
	public static <E> boolean and(long[] list, LongPredicate predicate)
	{
		for(long element : list) if (!predicate.test(element)) return false;
		return true;
	}
	
	public static <E> boolean or(E[] list, Judgable<E> checker)
	{
		for(E element : list) if (checker.isTrue(element)) return true;
		return false;
	}
	
	public static <E> boolean or(int[] list, IntPredicate predicate)
	{
		for(int element : list) if (predicate.test(element)) return true;
		return false;
	}
	
	public static <E> boolean or(long[] list, LongPredicate predicate)
	{
		for(long element : list) if (predicate.test(element)) return true;
		return false;
	}
	
	/**
	 * Create new integer array with same elements.
	 * @param length
	 * @param value
	 * @return
	 */
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
	
	/**
	 * Get first matched element index in list.
	 * @param list
	 * @param arg The matching target.
	 * @return The index of element, -1 means no element matched.
	 */
	public static <E> int indexOf(E[] list, E arg)
	{
		for(int i = 0; i < list.length; ++i) if(L.equal(list[i], arg)) return i;
		return -1;
	}
	
	/**
	 * Transform key array to target array.
	 * @param array
	 * @param elementClass
	 * @param function Transform function.
	 * @return
	 */
	public static <K, T> T[] transform(K[] array, Class<T> elementClass, Function<? super K, ? extends T> function)
	{
		T[] result = ObjectArrays.newArray(elementClass, array.length);
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	/**
	 * Transform key array to target array.
	 * @param array
	 * @param elementClass
	 * @param function Transform function.
	 * @return
	 */
	public static <T> T[] transform(int[] array, Class<T> elementClass, IntFunction<? extends T> function)
	{
		T[] result = ObjectArrays.newArray(elementClass, array.length);
		for(int i = 0; i < array.length; result[i] = function.apply(array[i]), ++i);
		return result;
	}
	
	/**
	 * Create a array with ranged number.<p>
	 * Examples :
	 * <code>
	 * Arrays.toString(rangeIntArray(1, 3));
	 * </code>
	 * and the result is {@code [1, 2]}
	 * @param from
	 * @param to
	 * @return
	 */
	public static int[] rangeIntArray(int from, int to)
	{
		int[] array = new int[to - from];
		for(int i = 0; i < array.length; array[i] = from + i, i++);
		return array;
	}
	
	/**
	 * Create a array with selected generator.<p>
	 * Examples :
	 * <code>
	 * Arrays.toString(createIntArray(3, i->i*i));
	 * </code>
	 * and the result is {@code [0, 1, 4]}
	 * @param from
	 * @param to
	 * @return
	 */
	public static int[] createIntArray(int length, IntUnaryOperator function)
	{
		int[] result = new int[length];
		for (int i = 0; i < length; result[i] = function.applyAsInt(i), ++i);
		return result;
	}
	
	public static long[] createLongArray(int length, IntToLongFunction function)
	{
		long[] result = new long[length];
		for (int i = 0; i < length; result[i] = function.applyAsLong(i), ++i);
		return result;
	}
	
	public static <E> E[] createArray(int length, @Nonnull E value)
	{
		E[] array = (E[]) Array.newInstance(value.getClass(), length);
		Arrays.fill(array, value);
		return array;
	}
}