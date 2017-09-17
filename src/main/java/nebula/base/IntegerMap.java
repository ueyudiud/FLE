/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntUnaryOperator;

import com.google.common.collect.Iterators;

import nebula.common.util.L;
import nebula.common.util.Maths;

/**
 * The <tt>int</tt> value map.
 * @author ueyudiud
 * @param <T>
 * @see java.util.Map
 */
public class IntegerMap<T> implements Iterable<IntegerEntry<T>>
{
	/**
	 * The array load factor.<p>
	 * Use to determine how fast the array length
	 * increase when try to expend it.<p>
	 * The default value is <code>0.75F</code>
	 */
	private final float loadFactor;
	private int size;
	private int sum;
	transient INode<IntegerEntry<T>>[] entries;
	
	protected int hashcode(Object object)
	{
		return Objects.hashCode(object);
	}
	
	public IntegerMap()
	{
		this(4);
	}
	public IntegerMap(int initialCapacity)
	{
		this(initialCapacity, 0.75F);
	}
	public IntegerMap(int initialCapacity, float loadFactor)
	{
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		if (loadFactor <= 0 || Float.isNaN(loadFactor))
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		this.loadFactor = loadFactor;
		this.entries = new INode[initialCapacity];
	}
	
	public IntegerMap(IntegerMap<? extends T> map)
	{
		this.loadFactor = 0.75F;
		this.entries = new INode[(int) (map.size() / this.loadFactor + 1)];
		this.sum = map.getSum();
		this.size = map.size();
		for (IntegerEntry<? extends T> entry : map)
		{
			putUnchecked(new IntegerEntry<T>(entry.key, entry.value));
		}
	}
	
	private Integer putChecked(T target, int value)
	{
		if(this.size + 1 > this.entries.length * this.loadFactor)
		{
			INode<IntegerEntry<T>>[] entries = this.entries;
			this.entries = new INode[(this.size + 1) + (this.size >> 1)];
			for(INode<IntegerEntry<T>> node : entries)
			{
				if(node == null) continue;
				for(IntegerEntry<T> entry : node)
				{
					putUnchecked(entry);
				}
			}
		}
		return putUnchecked(new IntegerEntry<>(target, value));
	}
	
	private Integer putUnchecked(IntegerEntry<T> entry)
	{
		int hashcode = hashcode(entry.key);
		int i = Maths.mod(hashcode, this.entries.length);
		if(this.entries[i] == null)
		{
			this.entries[i] = Node.first(entry);
			return null;
		}
		else
		{
			for(IntegerEntry<T> entry1 : this.entries[i])
			{
				if(Objects.equals(entry.key, entry1.key))
				{
					int old = entry1.value;
					entry1.value = entry.value;
					return old;
				}
			}
			this.entries[i].addNext(entry);
			return null;
		}
	}
	
	private IntegerEntry<T> getEntry(Object key)
	{
		if (this.entries.length == 0) return null;
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if(this.entries[i] == null) return null;
		else
		{
			try
			{
				for(IntegerEntry<T> entry : this.entries[i])
				{
					if(Objects.equals(key, entry.key))
						return entry;
				}
			}
			catch (ClassCastException e)
			{
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Return the number of key-value mappings in this <tt>int</tt> value map.
	 * @return the number of key-value mappings in this map.
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return this.size;
	}
	
	/**
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	/**
	 * Match is key exist in map.
	 * @param key the key.
	 * @return <code>true</code> if this map contains the key.
	 */
	public boolean containsKey(Object key)
	{
		return getEntry(key) != null;
	}
	
	public boolean contains(Object key, int value)
	{
		IntegerEntry<?> entry;
		return (entry = getEntry(key)) != null && entry.value == value;
	}
	
	public int get(Object key)
	{
		return getOrDefault(key, 0);
	}
	
	public int getOrDefault(Object key, int value)
	{
		IntegerEntry<T> entry = getEntry(key);
		return entry == null ? value : entry.value;
	}
	
	public int put(T key, int value)
	{
		Integer old = putChecked(key, value);
		this.sum += value;
		if(old == null)
		{
			++this.size;
			return 0;
		}
		else
		{
			this.sum -= old;
			return old;
		}
	}
	
	private int removeAt(int id, INode<IntegerEntry<T>> node)
	{
		boolean flag1 = !node.hasLast();
		boolean flag2 = !node.hasNext();
		if(flag1 && flag2)
		{
			this.entries[id] = null;
		}
		else if(flag1)
		{
			this.entries[id] = node.next();
		}
		int v = node.remove().value;
		this.sum -= v;
		return v;
	}
	
	public int remove(Object key)
	{
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if(this.entries[i] == null) return 0;
		else
		{
			INode<IntegerEntry<T>> node0 = INode.telomereNode(this.entries[i]);
			do
			{
				node0 = node0.next();
				if(L.equal(node0.value().key, key))
				{
					return removeAt(i, node0);
				}
			}
			while (node0.hasNext());
			return 0;
		}
	}
	
	public void clear()
	{
		Arrays.fill(this.entries, null);
		this.size = 0;
		this.sum = 0;
	}
	
	public Set<T> keySet()
	{
		return new IntegerMapSet();
	}
	
	@Override
	public Iterator<IntegerEntry<T>> iterator()
	{
		return new IntegerMapItr();
	}
	
	/**
	 * Get sum of all value.
	 * @return the sum.
	 */
	public int getSum()
	{
		return this.sum;
	}
	
	@Override
	public int hashCode()
	{
		int hashcode = 0;
		for (IntegerEntry<T> entry : this)
			hashcode += entry.hashCode();
		return hashcode;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		
		if (!(obj instanceof IntegerMap<?>)) return false;
		
		IntegerMap<?> map = (IntegerMap<?>) obj;
		
		if (map.size() != this.size) return false;
		
		try
		{
			for (IntegerEntry<?> entry : this)
			{
				if (!map.contains(entry.key, entry.value))
					return false;
			}
		}
		catch (ClassCastException | NullPointerException e)
		{
			return false;
		}
		return true;
	}
	
	public void transformAll(IntUnaryOperator operator)
	{
		this.sum = 0;
		for (IntegerEntry<T> entry : this)
		{
			int t = operator.applyAsInt(entry.value);
			this.sum += t;
			entry.setValue(t);
		}
	}
	
	class IntegerMapItr implements Iterator<IntegerEntry<T>>
	{
		boolean modified;
		int pointer = -1;
		INode<IntegerEntry<T>> currentNode;
		
		@Override
		public boolean hasNext()
		{
			if (this.currentNode == null || !this.currentNode.hasNext())
			{
				int pointer = this.pointer + 1;
				while (pointer < IntegerMap.this.entries.length && IntegerMap.this.entries[pointer] != null)
				{
					++pointer;
				}
				return IntegerMap.this.entries[pointer] != null;
			}
			return true;
		}
		
		@Override
		public IntegerEntry<T> next()
		{
			this.modified = false;
			if(this.currentNode == null || !this.currentNode.hasNext())
			{
				while (IntegerMap.this.entries[++this.pointer] != null);
				this.currentNode = IntegerMap.this.entries[this.pointer];
				return this.currentNode.value();
			}
			this.currentNode = this.currentNode.next();
			return this.currentNode.value();
		}
		
		@Override
		public void remove()
		{
			if(this.modified) throw new IllegalStateException("The element already removed!");
			INode<IntegerEntry<T>> old = this.currentNode;
			removeAt(this.pointer, old);
			if(this.currentNode.hasLast())
			{
				this.currentNode = this.currentNode.last();
			}
			else
			{
				this.currentNode = null;
			}
			IntegerMap.this.sum -= old.remove().value;
			IntegerMap.this.size--;
			this.modified = true;
		}
	}
	
	class IntegerMapSet implements Set<T>
	{
		@Override
		public int size()
		{
			return IntegerMap.this.size;
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
			return Iterators.transform(IntegerMap.this.iterator(), entry -> entry.key);
		}
		
		@Override
		public Object[] toArray()
		{
			return Iterators.toArray(iterator(), Object.class);
		}
		
		@Override
		public <E> E[] toArray(E[] a)
		{
			return Iterators.toArray((Iterator<E>) iterator(), (Class<E>) a.getClass().getComponentType());
		}
		
		@Override
		public boolean add(T e)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsAll(Collection<?> c)
		{
			for(Object object : c) if(!IntegerMap.this.containsKey(object)) return false;
			return true;
		}
		
		@Override
		public boolean addAll(Collection<? extends T> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void clear()
		{
			throw new UnsupportedOperationException();
		}
	}
}