/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.function;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents function that can direct return a result without argument.
 * <p>
 * Uses for some argument which value will be initialize after method called.
 * <p>
 * This interface extends {@link java.util.concurrent.Callable} and
 * {@link java.util.function.Supplier} both to improved the compatibility in
 * some other methods casting.
 * 
 * @param <T> the type of result applied by {@link #get()}.
 * 
 * @author ueyudiud
 */
@FunctionalInterface
public interface Applicable<T> extends Callable<T>, Supplier<T>
{
	static Applicable<?> NULL = () -> null;
	
	/**
	 * Return a <code>Applicable</code> which apply constant result.
	 * 
	 * @param value the constant result.
	 * @return the constant result applier.
	 */
	static <V> Applicable<V> to(V value)
	{
		return value == null ? (Applicable<V>) NULL : () -> value;
	}
	
	/**
	 * Created a cache for Applicable.
	 * <p>
	 * The value get from {@link #apply()} will be stored and return directly
	 * when {@link #apply()} called next time.
	 * <p>
	 * If applicable is already the cached type, the argument will be return
	 * directly.
	 * 
	 * @param applicable the applicable to wrap.
	 * @return the cached Applicable.
	 */
	static <V> ApplicableCached<V> asCached(Supplier<? extends V> applicable)
	{
		if (applicable instanceof ApplicableCached) return (ApplicableCached<V>) applicable;
		return new ApplicableCached<>(applicable);
	}
	
	static <V> Applicable<V> or(@Nonnull BooleanSupplier supplier, @Nonnull Supplier<V> a1, @Nonnull Supplier<V> a2)
	{
		return () -> supplier.getAsBoolean() ? a1.get() : a2.get();
	}
	
	static <V> V orApply(@Nullable Supplier<? extends V> supplier, V value)
	{
		return supplier == null ? value : supplier.get();
	}
	
	default <V> Applicable<V> andThen(@Nonnull Function<? super T, ? extends V> function)
	{
		Objects.requireNonNull(function);
		return () -> function.apply(get());
	}
	
	/**
	 * @see java.util.Optional#ifPresent(Consumer)
	 * @param consumer the consumer.
	 */
	default void consumeIfPresent(Consumer<? super T> consumer)
	{
		this.<T>applyOptional().ifPresent(consumer);
	}
	
	default <E> Function<E, T> anyTo()
	{
		return a -> get();
	}
	
	/**
	 * Get applied value, the value <i>should</i> be a constant, or want to
	 * return a no-constant value, you can use such as {@link Selector} instead.
	 * 
	 * @throws IllegalStateException when target is not initialize or can not
	 *             provide in time, etc.
	 * @return the applied value.
	 */
	@Override
	@Nullable T get();
	
	@Override
	default T call() throws IllegalStateException
	{
		return get();
	}
	
	default <T1> Optional<T1> applyOptional()
	{
		return this == NULL ? Optional.empty() : Optional.ofNullable((T1) apply());
	}
	
	@Deprecated
	@Nullable default T apply()
	{
		return get();
	}
}
