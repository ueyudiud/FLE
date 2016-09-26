package farcore.lib.collection;

import java.util.Arrays;
import java.util.Iterator;

public class IteratorList<E> implements Iterator<E>
{
	private final E[] list;
	private int pos = 0;
	
	public IteratorList()
	{
		this(null);
	}
	public IteratorList(E[] list)
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
	
	public E last()
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
}