/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.function;

import java.util.function.Function;

/**
 * Until MC used new version Guava,
 * use this extension for both java function and
 * guava function used.
 * <p>
 * <b>
 * DO NOT USE THIS VALUE AS METHOD INPUT PARAMETERS,
 * IT ONLY USE FOR CASTION, AND MAY REMOVED WITHOUT CALLED.
 * </b>
 * 
 * @author ueyudiud
 */
@Deprecated
@FunctionalInterface
public interface Func<K, R> extends Function<K, R>, com.google.common.base.Function<K, R>
{
	@Override R apply(K key);
	
	@Override
	default <V> Func<K, V> andThen(Function<? super R, ? extends V> after)
	{
		return k -> after.apply(apply(k));
	}
	
	@Override
	default <V> Function<V, R> compose(Function<? super V, ? extends K> before)
	{
		return k -> apply(before.apply(k));
	}
}
