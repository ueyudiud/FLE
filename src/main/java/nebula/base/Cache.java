/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * The cache is to cache a element.
 * @author ueyudiud
 */
public
class Cache<E> implements Serializable
{
	private static final long serialVersionUID = -3454337834646888382L;
	
	/** The stored element. */
	@Nullable
	E element;
	
	/**
	 * Create a new cache with <tt>null</tt> element.
	 */
	public Cache()
	{
	}
	
	/**
	 * Create a new cache with input argument element.
	 * @param element the cached element.
	 */
	public Cache(@Nullable E element)
	{
		this.element = element;
	}
	
	/**
	 * Set a new element in cache.
	 * @param element the new element.
	 */
	public void set(@Nullable E element)
	{
		this.element = element;
	}
	
	/**
	 * Get the cached element.
	 * @return the cached element.
	 */
	@Nullable
	public E get()
	{
		return this.element;
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
}