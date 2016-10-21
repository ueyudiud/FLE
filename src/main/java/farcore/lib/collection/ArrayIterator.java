package farcore.lib.collection;

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
		return list != null && pos < list.length;
	}

	@Override
	public E next()
	{
		return list[pos++];
	}
	
	@Override
	public boolean hasPrevious()
	{
		return false;
	}
	
	@Override
	public E previous()
	{
		return list[--pos];
	}

	public void reset()
	{
		pos = 0;
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(list);
	}

	@Override
	public int nextIndex()
	{
		return pos + 1;
	}

	@Override
	public int previousIndex()
	{
		return pos - 1;
	}

	@Override
	public void remove()
	{
		list[pos - 1] = null;
	}

	@Override
	public void set(E e)
	{
		list[pos - 1] = e;
	}

	@Override
	public void add(E e)
	{
		throw new UnsupportedOperationException();
	}
}