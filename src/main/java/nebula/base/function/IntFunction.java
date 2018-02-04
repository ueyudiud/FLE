/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.function;

import java.util.function.Function;

/**
 * The implements of {@link java.util.function.IntFunction}
 * 
 * @author ueyudiud
 */
public interface IntFunction<R> extends java.util.function.IntFunction<R>
{
	static <R> IntFunction<R> any(R result)
	{
		return i -> result;
	}
	
	default <V> IntFunction<V> andThen(Function<? super R, ? extends V> function)
	{
		return i -> function.apply(apply(i));
	}
}
