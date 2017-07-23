/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author ueyudiud
 */
public
class Cache<E> implements Serializable
{
	private static final long serialVersionUID = -3454337834646888382L;
	
	E element;
	
	public Cache()
	{
	}
	public Cache(E element)
	{
		this.element = element;
	}
	
	public void set(E element)
	{
		this.element = element;
	}
	
	public E get()
	{
		return this.element;
	}
	
	@Override
	public String toString()
	{
		return Objects.toString(this.element);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.element);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.element.equals(obj);
	}
	
	@Override
	public Object clone()
	{
		return new Cache<>(this.element);
	}
}