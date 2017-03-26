/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import java.util.AbstractList;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
class ArrayListArgument<E> extends AbstractList<E>
{
	private Object[] array;
	
	public ArrayListArgument(Object[] array)
	{
		this.array = array;
	}
	
	@Override
	public Object[] toArray()
	{
		return this.array;
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		if (a.length < this.array.length)
		{
			a = A.copyToLength(a, this.array.length);
		}
		System.arraycopy(this.array, 0, a, 0, this.array.length);
		return a;
	}
	
	@Override
	public E get(int index)
	{
		if (index >= this.array.length || index < 0)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return (E) this.array[index];
	}
	
	@Override
	public int size()
	{
		return this.array.length;
	}
}