/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.OptionalInt;
import java.util.Set;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;

import javax.annotation.Nonnull;

/**
 * @author ueyudiud
 */
public interface IntMap<E> extends Iterable<IntegerEntry<E>>
{
	/**
	 * Return a immutable empty IntegerMap.
	 * 
	 * @return the IntegerMap.
	 */
	static <E> IntMap<E> empty()
	{
		return (IntMap<E>) ImmutableEmptyIntMap.INSTANCE;
	}
	
	static <E> IntMap<E> of(E key, int value)
	{
		return new ImmutableSingleIntMap<>(key, value);
	}
	
	static <E> IntMap<E> copyOf(@Nonnull IntMap<? extends E> map)
	{
		switch (map.size())
		{
		case 0 :
			return empty();
		case 1 :
			IntegerEntry<? extends E> entry = map.iterator().next();
			return of(entry.getKey(), entry.getValue());
		default:
			return new HashIntMap<>(map);
		}
	}
	
	int size();
	
	default boolean isEmpty()
	{
		return size() == 0;
	}
	
	boolean containsKey(Object key);
	
	boolean contains(Object key, int value);
	
	/**
	 * Get value which specified key is mapped.
	 * <p>
	 * 
	 * @param key
	 * @return the value of key mapped.
	 * @see java.util.Map#get(Object)
	 */
	default int get(Object key)
	{
		return getOrDefault(key, 0);
	}
	
	default int getOrDefault(Object key, int value)
	{
		return getIf(key).orElse(value);
	}
	
	@Nonnull OptionalInt getIf(Object key);
	
	default int put(@Nonnull IntegerEntry<E> entry)
	{
		return put(entry.getKey(), entry.getValue());
	}
	
	int put(E key, int value);
	
	void putOrAdd(E key, int amount);
	
	/**
	 * Called this method when regard this collection value as probability.
	 * <p>
	 * For use, divided each value with their GCD.
	 * 
	 * @throws IllegalStateException when some value is non-positive.
	 */
	void rescale() throws IllegalStateException;
	
	default int remove(Object key, int def)
	{
		return removeIf(key).orElse(def);
	}
	
	@Nonnull OptionalInt removeIf(Object key);
	
	void clear();
	
	int sum();
	
	Set<E> keySet();
	
	default void foreach(@Nonnull ObjIntConsumer<? super E> consumer)
	{
		for (IntegerEntry<? extends E> entry : this)
			consumer.accept(entry.getKey(), entry.getValue());
	}
	
	/**
	 * Return <code>true</code> if two map are equal.
	 * 
	 * @param obj
	 * @return
	 */
	boolean equals(Object obj);
	
	/**
	 * Return the hashcode of map.
	 * 
	 * @return the hashcode.
	 */
	int hashCode();
	
	/**
	 * 
	 * @return
	 */
	String toString();
	
	/**
	 * Transform all value.
	 * 
	 * @param operator the operator to transform value.
	 */
	default void transformAll(@Nonnull IntUnaryOperator operator)
	{
		for (IntegerEntry<E> entry : this)
		{
			entry.setValue(operator.applyAsInt(entry.value));
		}
	}
}
