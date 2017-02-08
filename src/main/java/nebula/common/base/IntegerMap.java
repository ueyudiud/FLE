package nebula.common.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Iterators;

import nebula.common.util.L;
import nebula.common.util.Maths;

public class IntegerMap<T> implements Iterable<IntegerEntry<T>>
{
	private final float loadFactor;
	private int size;
	private INode<IntegerEntry<T>>[] entries;
	
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
	
	private Integer putChecked(T target, int value)
	{
		if(this.size + 1 > this.entries.length)
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
		return putUnchecked(new IntegerEntry(target, value));
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
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if(this.entries[i] == null) return null;
		else
		{
			for(IntegerEntry<T> entry : this.entries[i])
			{
				if(Objects.equals(key, entry.key))
					return entry;
			}
		}
		return null;
	}
	
	public int size()
	{
		return this.size;
	}
	
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	public boolean containsKey(Object key)
	{
		return getEntry(key) != null;
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
		if(old == null)
		{
			++this.size;
			return 0;
		}
		else
		{
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
		node.remove();
		return node.value().value;
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
			INode old = this.currentNode;
			removeAt(this.pointer, old);
			if(this.currentNode.hasLast())
			{
				this.currentNode = this.currentNode.last();
			}
			else
			{
				this.currentNode = null;
			}
			old.remove();
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