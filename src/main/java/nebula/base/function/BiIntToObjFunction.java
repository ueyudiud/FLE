/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;

/**
 * The double coordinate to produces an object.
 * 
 * @param <R> the type of the result of the function
 * 
 * @see java.util.function.Function
 * @author ueyudiud
 */
@FunctionalInterface
public
interface BiIntToObjFunction<R>
{
	/**
	 * Create a new function that return a constant value.
	 * @param result
	 * @return
	 */
	static <R> BiIntToObjFunction<R> anyTo(R result)
	{
		return (x, z)-> result;
	}
	
	/**
	 * Apply result of double coordinate.
	 * @see java.util.function.Function#apply(Object)
	 * @param x
	 * @param z
	 * @return
	 */
	R apply(int x, int y);
	
	/**
	 * Return a composed function.
	 * @see java.util.function.Function#andThen(Function)
	 * @param function
	 * @return
	 */
	default <T> BiIntToObjFunction<T> andThen(Function<R, T> function)
	{
		Objects.requireNonNull(function);
		return (x, y)-> function.apply(apply(x, y));
	}
	
	default LongFunction<R> fromLong()
	{
		return l->apply((int) (l & 0xFFFFFFFF), (int) (l >> 32 & 0xFFFFFFFF));
	}
}