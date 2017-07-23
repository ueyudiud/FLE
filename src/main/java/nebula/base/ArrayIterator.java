package nebula.base;

import java.util.Arrays;
import java.util.ListIterator;

public class ArrayIterator<E> implements ListIterator<E>
{
	private final E[] list;
	private int pos = 0;
	
	public ArrayIterator()
	{
		this(null);
	}
	public ArrayIterator(E[] list)
	{
		this.list = list;
	}
	
	@Override
	public boolean hasNext()
	{
		return this.list != null && this.pos < this.list.length;
	}
	
	@Override
	public E next()
	{
		return this.list[this.pos++];
	}
	
	@Override
	public boolean hasPrevious()
	{
		return false;
	}
	
	@Override
	public E previous()
	{
		return this.list[--this.pos];
	}
	
	public void reset()
	{
		this.pos = 0;
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(this.list);
	}
	
	@Override
	public int nextIndex()
	{
		return this.pos + 1;
	}
	
	@Override
	public int previousIndex()
	{
		return this.pos - 1;
	}
	
	@Override
	public void remove()
	{
		this.list[this.pos - 1] = null;
	}
	
	@Override
	public void set(E e)
	{
		this.list[this.pos - 1] = e;
	}
	
	@Override
	public void add(E e)
	{
		throw new UnsupportedOperationException();
	}
}