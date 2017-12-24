/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import nebula.base.function.Applicable;

/**
 * The cache is to cache a element.
 * 
 * @author ueyudiud
 */
public class Cache<E> implements Serializable, Applicable<E>
{
	private static final long serialVersionUID = -3454337834646888382L;
	
	private static final Object NULL = new Object()
	{
		@Override
		public int hashCode()
		{
			return Objects.hashCode(null);
		}
		
		@Override
		public String toString()
		{
			return "null";
		}
	};
	
	/** The stored element. */
	@Nullable Object element;
	
	/**
	 * Create a new cache with <tt>null</tt> element.
	 */
	public Cache()
	{
		this.element = NULL;
	}
	
	/**
	 * Create a new cache with input argument element.
	 * 
	 * @param element the cached element.
	 */
	public Cache(@Nullable E element)
	{
		this.element = element;
	}
	
	/**
	 * Set a new element in cache.
	 * 
	 * @param element the new element.
	 */
	public void set(@Nullable E element)
	{
		this.element = element;
	}
	
	/**
	 * Get the cached element.
	 * 
	 * @return the cached element.
	 */
	@Nullable
	public E get()
	{
		return this.element == NULL ? null : (E) this.element;
	}
	
	// Optional like.
	public boolean isAbsent()
	{
		return this.element == NULL;
	}
	
	public final void consumeIfPresent(Consumer<? super E> consumer)
	{
		if (!isAbsent())
		{
			Objects.requireNonNull(consumer).accept(get());
		}
	}
	
	public final void setIfAbsent(E element)
	{
		if (isAbsent())
		{
			set(element);
		}
	}
	
	public final void setIfAbsent(Supplier<? extends E> supplier)
	{
		if (isAbsent())
		{
			set(supplier.get());
		}
	}
	
	// Optional like end.
	
	/**
	 * Get {@link java.util.Optional} value, which wrapped element if it is
	 * present.
	 * 
	 * @return the Optional.
	 */
	public Optional<E> optional()
	{
		return isAbsent() ? Optional.empty() : Optional.ofNullable((E) this.element);
	}
	
	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString()
	{
		return Objects.toString(this.element);
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.element);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return Objects.equals(this.element, obj);
	}
	
	/**
	 * Create a new {@link Cache} with same element contain.
	 */
	@Override
	public Object clone()
	{
		return new Cache<>(this.element);
	}
	
	@Override
	public E apply()
	{
		return get();
	}
}
