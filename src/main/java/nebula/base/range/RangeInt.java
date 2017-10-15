/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.range;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
public
class RangeInt extends AbstractList<Integer>
{
	private final int min, max;
	
	public static List<Integer> to(int min, int max)
	{
		return min >= max ? ImmutableList.of() : new RangeInt(min, max);
	}
	
	/**
	 * Create a ranged collection.
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<Integer> range(int min, int max)
	{
		return min >= max ? ImmutableList.of() : new RangeInt(min, max + 1);
	}
	
	private RangeInt(int min, int max)
	{
		if (min >= max)
			throw new IllegalArgumentException("The min value must less than max value!");
		this.max = max;
		this.min = min;
	}
	
	@Override
	public Integer get(int index)
	{
		int i = index + this.min;
		if (i >= this.max)
			throw new ArrayIndexOutOfBoundsException(index);
		return i;
	}
	
	@Override
	public int size()
	{
		return this.max - this.min;
	}
	
	@Override
	public boolean isEmpty()//The range will always not empty, with initializer checking.
	{
		return false;
	}
	
	@Override
	public boolean contains(Object o)
	{
		if (o instanceof Integer)
		{
			return (Integer) o >= this.min && (Integer) o < this.max;
		}
		return false;
	}
	
	@Override
	public Iterator<Integer> iterator()
	{
		return new Iterator<Integer>()
		{
			int current = RangeInt.this.min;
			
			@Override
			public boolean hasNext()
			{
				return this.current < RangeInt.this.max;
			}
			
			@Override
			public Integer next()
			{
				int i = this.current++;
				if (i >= RangeInt.this.max)
					throw new IndexOutOfBoundsException();
				return i;
			}
		};
	}
	
	private void rangeCheck(int index)
	{
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index: " + index + " Range: [" + this.min + ", " + this.max + ")");
	}
	
	@Override
	public ListIterator<Integer> listIterator(int index)
	{
		rangeCheck(index);
		return new ListIterator<Integer>()
		{
			int current = RangeInt.this.min + index;
			
			@Override
			public boolean hasNext()
			{
				return this.current < RangeInt.this.max;
			}
			
			@Override
			public Integer next()
			{
				int i = this.current++;
				rangeCheck(i);
				return i;
			}
			
			@Override
			public boolean hasPrevious()
			{
				return this.current > RangeInt.this.min;
			}
			
			@Override
			public Integer previous()
			{
				int i = --this.current;
				rangeCheck(i);
				return i;
			}
			
			@Override
			public int nextIndex()
			{
				return this.current - RangeInt.this.min;
			}
			
			@Override
			public int previousIndex()
			{
				return this.current - RangeInt.this.min - 1;
			}
			
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void set(Integer e)
			{
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void add(Integer e)
			{
				throw new UnsupportedOperationException();
			}
		};
	}
	
	@Override
	public Object[] toArray()
	{
		Integer[] array = new Integer[size()];
		return A.fill(array, i->i + this.min);
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		int size = size();
		if (a.length < size)
		{
			a = ObjectArrays.newArray(a, size);
		}
		return A.<T>fill(a, i->(T) Integer.valueOf(i + this.min));
	}
	
	@Override
	public boolean add(Integer e)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsAll(Collection<?> collection)
	{
		if (collection.isEmpty()) return true;
		if (collection instanceof RangeInt)
		{
			return ((RangeInt) collection).min >= this.min && ((RangeInt) collection).max >= this.max;
		}
		else
		{
			return collection.stream().allMatch(this::contains);
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends Integer> c)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
}