/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Represents function that can direct return a result without argument.<p>
 * 
 * Uses for some argument which value will be initialize after method called.
 * 
 * @param <T> The output type.
 * 
 * @author ueyudiud
 */
@FunctionalInterface
public interface Appliable<T> extends Callable<T>
{
	static Appliable<?> NULL = () -> null;
	
	default <V> Appliable<V> andThen(Function<T, V> function)
	{
		Objects.requireNonNull(function);
		return () -> function.apply(apply());
	}
	
	default <E> Function<E, T> anyTo()
	{
		return a->apply();
	}
	
	@Override
	default T call()
	{
		return apply();
	}
	
	T apply();
}