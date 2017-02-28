/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;

import com.google.common.collect.ObjectArrays;

import nebula.common.base.Judgable;

/**
 * @author ueyudiud
 */
public class A
{
	public static int[] copyToLength(int[] array, int len)
	{
		int[] result = new int[len];
		System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
		return result;
	}
	
	public static <T> T[] copyToLength(T[] array, int len)
	{
		T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), len);
		System.arraycopy(array, 0, result, 0, Math.min(len, array.length));
		return result;
	}
	
	public static <E> void executeAll(E[] iterable, Consumer<E> consumer)
	{
		for(E element : iterable) consumer.accept(element);
	}
	
	public static <E> boolean contain(E[] list, E arg)
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
	
	public static <E> int indexOf(E[] list, E arg)
	{
		for(int i = 0; i < list.length; ++i) if(L.equal(list[i], arg)) return i;
		return -1;
	}
	
	public static <K, T> T[] transform(K[] array, Class<T> elementClass, Function<K, T> function)
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
}