/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base.function;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

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
public interface Applicable<T> extends Callable<T>, Supplier<T>
{
	abstract class AppliableCached<T> implements Applicable<T>
	{
		T cache;
		
		@Override
		public T apply()
		{
			if (cache == null)
			{
				cache = apply$();
			}
			
			return cache;
		}
		
		protected abstract T apply$();
	}
	
	static Applicable<?> NULL = () -> null;
	
	/**
	 * Return a <code>Applicable</code> which apply constant result.
	 * @param value the constant result.
	 * @return the constant result applier.
	 */
	@SuppressWarnings("unchecked")//It is safe.
	static <V> Applicable<V> to(V value)
	{
		return value == null ? (Applicable<V>) NULL : ()-> value;
	}
	
	static <V> AppliableCached<V> wrapCached(Applicable<V> appliable)
	{
		if (appliable instanceof AppliableCached) return (AppliableCached<V>) appliable;
		return new AppliableCached<V>()
		{
			@Override
			protected V apply$()
			{
				return appliable.apply();
			}
		};
	}
	
	static <V> Applicable<V> or(BooleanSupplier supplier, Applicable<V> a1, Applicable<V> a2)
	{
		return () -> supplier.getAsBoolean() ? a1.apply() : a2.apply();
	}
	
	default <V> Applicable<V> andThen(Function<T, V> function)
	{
		Objects.requireNonNull(function);
		return () -> function.apply(apply());
	}
	
	default <E> Function<E, T> anyTo()
	{
		return a->apply();
	}
	
	@Override
	default T get()
	{
		return apply();
	}
	
	@Override
	default T call()
	{
		return apply();
	}
	
	default Optional<T> applyOptional()
	{
		return this == NULL ? Optional.empty() : Optional.ofNullable(apply());
	}
	
	/**
	 * Get apply value.
	 * @throws IllegalStateException When target is not initialize or connected to server, etc.
	 * @return The applied value.
	 */
	T apply();
}