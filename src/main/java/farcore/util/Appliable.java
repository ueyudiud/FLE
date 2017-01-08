/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

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
public interface Appliable<T>
{
	static Appliable<?> NULL = () -> null;
	
	default <V> Appliable<V> from(Function<T, V> function)
	{
		return () -> function.apply(apply());
	}
	
	T apply();
}