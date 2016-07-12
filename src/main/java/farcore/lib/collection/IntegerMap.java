package farcore.lib.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import farcore.lib.collection.IntegerMap.Prop;

public class IntegerMap<T> implements Iterable<Prop<T>>
{
	public static class Prop<T>
	{
		public T tag;
		public int value;
		
		private Prop(){}
	}
	
	private final float extFactor;
	private int point;
	private int size;
	private int[] list;
	private Object[] keys;

	public IntegerMap(float v)
	{
		this.extFactor = v;
		this.list = new int[4];
		this.keys = new Object[4];
	}
	public IntegerMap()
	{
		this(0.25F);
	}
	
	private int freePoint()
	{
		while(point < keys.length)
		{
			if(keys[point] == null)
			{
				return point;
			}
			++point;
		}
		extList((int) (point * (1 + extFactor)) + 1);
		++point;
		return point;
	}
	
	private void extList(int size)
	{
		if(size() > size) return;
		int[] l = new int[size];
		Object[] k = new Object[size];
		System.arraycopy(list, 0, l, 0, list.length);
		System.arraycopy(keys, 0, k, 0, keys.length);
		list = l;
		keys = k;
	}
	
	private int indexOf(Object key)
	{
		int i = 0;
		for(Object string; i < keys.length;)
		{
			string = keys[i];
			if(string != null && string.equals(key))
			{
				return i;
			}
			++i;
		}
		return -1;
	}
	
	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size() == 0;
	}

	public boolean containsKey(Object key)
	{
		return indexOf(key) != -1;
	}

	public int get(Object key)
	{
		int v = indexOf(key);
		return v == -1 ? 0 : list[v];
	}
	
	public int getOrDefault(Object key, int value)
	{
		int v = indexOf(key);
		return v == -1 ? value : list[v];
	}

	public int put(T key, int value)
	{
		int v = indexOf(key);
		if(v != -1)
		{
			int r = list[v];
			list[v] = value;
			return r;
		}
		v = freePoint();
		keys[v] = key;
		list[v] = value;
		++size;
		return 0;
	}
	
	public int remove(Object key)
	{
		int v = indexOf(key);
		if(v == -1) return 0;
		int r = list[v];
		list[v] = 0;
		keys[v] = null;
		--size;
		point = v;
		return r;
	}

	public void clear()
	{
		Arrays.fill(list, 0);
		Arrays.fill(keys, null);
		size = 0;
	}

	public Set<T> keySet()
	{
		return new IntegerMapSet();
	}
	
	@Override
	public Iterator<Prop<T>> iterator()
	{
		return new IntegerMapItr();
	}
	
	private class IntegerMapItr implements Iterator<Prop<T>>
	{
		int pointer;
		
		private int nextPoint(boolean flag)
		{
			if(flag)
			{
				while(pointer < keys.length)
				{
					if(keys[pointer] != null)
					{
						return pointer++;
					}
					++pointer;
				}
			}
			else
			{
				int pointer = this.pointer;
				while(pointer < keys.length)
				{
					if(keys[pointer] != null)
					{
						return pointer++;
					}
					++pointer;
				}
			}
			return -1;
		}

		@Override
		public boolean hasNext()
		{
			return nextPoint(false) != -1;
		}

		@Override
		public Prop<T> next()
		{
			int v = nextPoint(true);
			Prop<T> prop = new Prop();
			prop.tag = (T) keys[v];
			prop.value = list[v];
			return prop;
		}
		
	}
	
	private class IntegerMapSet implements Set<T>
	{
		@Override
		public int size()
		{
			return size;
		}

		@Override
		public boolean isEmpty()
		{
			return IntegerMap.this.isEmpty();
		}

		@Override
		public boolean contains(Object o)
		{
			return IntegerMap.this.containsKey(o);
		}

		@Override
		public Iterator<T> iterator()
		{
			return (Iterator<T>) Arrays.asList(keys).iterator();
		}

		@Override
		public Object[] toArray()
		{
			return keys;
		}

		@Override
		public <E> E[] toArray(E[] a)
		{
			if(a.length < size())
			{
				return (E[]) Arrays.copyOf(keys, size(), a.getClass());
			}
			System.arraycopy(keys, 0, a, 0, keys.length);
			return a;
		}

		@Override
		public boolean add(T e)
		{
			throw new IllegalArgumentException();
		}

		@Override
		public boolean remove(Object o)
		{
			throw new IllegalArgumentException();
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			for(Object object : c)
			{
				if(!IntegerMap.this.containsKey(object))
					return false;
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends T> c)
		{
			throw new IllegalArgumentException();
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new IllegalArgumentException();
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new IllegalArgumentException();
		}

		@Override
		public void clear()
		{
			throw new IllegalArgumentException();
		}		
	}
}