/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;

/**
 * The double <code>int</code> coordinate to produces an object function.
 * 
 * @param <R> the type of the result of the function
 * 
 * @see java.util.function.Function
 * @author ueyudiud
 */
@FunctionalInterface
public interface BiIntToObjFunction<R>
{
	/**
	 * Create a new function that return a constant value.
	 * 
	 * @param result the constant result that function always return.
	 * @return the function.
	 */
	static <R> BiIntToObjFunction<R> anyTo(R result)
	{
		return (x, z) -> result;
	}
	
	/**
	 * Apply result of double coordinate.
	 * 
	 * @see java.util.function.Function#apply(Object)
	 * @param x the first value.
	 * @param z the last value.
	 * @return the applied value.
	 */
	R apply(int x, int y);
	
	/**
	 * Return a composed function.
	 * 
	 * @see java.util.function.Function#andThen(Function)
	 * @param function the function applied after.
	 * @return the composed function.
	 */
	default <T> BiIntToObjFunction<T> andThen(Function<R, T> function)
	{
		Objects.requireNonNull(function);
		return (x, y) -> function.apply(apply(x, y));
	}
	
	/**
	 * Split <code>long</code> value as two <code>int</code> value for input,
	 * and create new composed function.
	 * 
	 * @return the composed LongFunction.
	 */
	default LongFunction<R> fromLong()
	{
		return l -> apply((int) (l & 0xFFFFFFFF), (int) (l >> 32 & 0xFFFFFFFF));
	}
}
