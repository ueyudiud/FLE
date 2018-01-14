/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;

import javax.annotation.Nonnull;

import com.google.common.collect.Iterators;

import nebula.common.util.L;
import nebula.common.util.Maths;

/**
 * The <tt>int</tt> value map. Store key-values by
 * {@link nebula.base.IntegerEntry}.
 * <p>
 * 
 * The key of map can be <code>null</code>, the most of methods can use
 * <code>null</code> as key to put or get entry. The type casting exception will
 * be caught.
 * 
 * @author ueyudiud
 * @param <K> the type of keys.
 * @see java.util.Map
 */
public class HashIntMap<K> extends AbstractIntMap<K>
{
	private static final float DEFAULT_LOAD_FACTOR = 0.75F;
	
	/**
	 * The array load factor.
	 * <p>
	 * Use to determine how fast the array length increase when try to expend
	 * it.
	 * <p>
	 * The default value is <code>0.75F</code>
	 */
	private final float					loadFactor;
	int									size;
	int									sum;
	transient INode<IntegerEntry<K>>[]	entries;
	
	protected int hashcode(Object object)
	{
		return Objects.hashCode(object);
	}
	
	public HashIntMap()
	{
		this(4);
	}
	
	public HashIntMap(int initialCapacity)
	{
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	public HashIntMap(int initialCapacity, float loadFactor)
	{
		if (initialCapacity < 0) throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		this.loadFactor = loadFactor;
		this.entries = new INode[initialCapacity];
	}
	
	/**
	 * Copies data from {@link java.util.Map}, the value type should predicate
	 * be as Byte, Short, Integer or Long.
	 * 
	 * @param map the source data map.
	 */
	public HashIntMap(Map<? extends K, ? extends Number> map)
	{
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		this.entries = new INode[(int) (map.size() / this.loadFactor + 1)];
		this.size = map.size();
		for (Entry<? extends K, ? extends Number> entry : map.entrySet())
		{
			putUnchecked(new IntegerEntry<K>(entry.getKey(), entry.getValue().intValue()));
			this.sum += entry.getValue().intValue();
		}
	}
	
	public HashIntMap(IntMap<? extends K> map)
	{
		this.loadFactor = 0.75F;
		this.entries = new INode[(int) (map.size() / this.loadFactor + 1)];
		this.sum = map.sum();
		this.size = map.size();
		for (IntegerEntry<? extends K> entry : map)
		{
			putUnchecked(new IntegerEntry<K>(entry.key, entry.value));
		}
	}
	
	private Integer putChecked(K target, int value)
	{
		if (this.size + 1 > this.entries.length * this.loadFactor)
		{
			INode<IntegerEntry<K>>[] entries = this.entries;
			this.entries = new INode[(this.size + 1) + (this.size >> 1)];
			for (INode<IntegerEntry<K>> node : entries)
			{
				if (node == null) continue;
				for (IntegerEntry<K> entry : node)
				{
					putUnchecked(entry);
				}
			}
		}
		return putUnchecked(new IntegerEntry<>(target, value));
	}
	
	private Integer putUnchecked(IntegerEntry<K> entry)
	{
		int hashcode = hashcode(entry.key);
		int i = Maths.mod(hashcode, this.entries.length);
		if (this.entries[i] == null)
		{
			this.entries[i] = Node.first(entry);
			return null;
		}
		else
		{
			for (IntegerEntry<K> entry1 : this.entries[i])
			{
				if (Objects.equals(entry.key, entry1.key))
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
	
	private IntegerEntry<K> getEntry(Object key)
	{
		if (this.entries.length == 0) return null;
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if (this.entries[i] == null)
			return null;
		else
		{
			try
			{
				for (IntegerEntry<K> entry : this.entries[i])
				{
					if (Objects.equals(key, entry.key)) return entry;
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
	 * 
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
		return this.size == 0;
	}
	
	/**
	 * Match is key exist in map.
	 * 
	 * @param key the key.
	 * @return <code>true</code> if this map contains the key.
	 */
	public boolean containsKey(Object key)
	{
		return getEntry(key) != null;
	}
	
	/**
	 * Match is key-value entry exist in map.
	 * 
	 * @param key the key.
	 * @param value the value.
	 * @return <code>true</code> if this map contains the entry.
	 */
	public boolean contains(Object key, int value)
	{
		IntegerEntry<?> entry;
		return (entry = getEntry(key)) != null && entry.value == value;
	}
	
	public int getOrDefault(Object key, int value)
	{
		IntegerEntry<K> entry = getEntry(key);
		return entry == null ? value : entry.value;
	}
	
	@Override
	public OptionalInt getIf(Object key)
	{
		IntegerEntry<K> entry = getEntry(key);
		return entry == null ? OptionalInt.empty() : OptionalInt.of(entry.value);
	}
	
	public int put(K key, int value)
	{
		Integer old = putChecked(key, value);
		this.sum += value;
		if (old == null)
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
	
	/**
	 * Put key-value pair if <tt>contains(key)</tt> returns <code>false</code>,
	 * or replace mapped value to <tt>get(key) + amount</tt> for specific key.
	 * 
	 * @param key the key.
	 * @param amount the value to put or add.
	 */
	public void putOrAdd(K key, int amount)
	{
		IntegerEntry<K> entry = getEntry(key);
		if (entry != null)
		{
			entry.value += amount;
			this.sum += amount;
		}
		else
		{
			put(key, amount);
		}
	}
	
	/**
	 * Called this method when regard this collection value as probability.
	 * <p>
	 * For use, divided each value with their GCD.
	 * 
	 * @throws IllegalStateException when some value is non-positive.
	 */
	public void rescale() throws IllegalStateException
	{
		if (isEmpty()) return;
		
		Iterator<IntegerEntry<K>> iterator = iterator();
		int i = checkRescaleValue(iterator.next().value);
		while (iterator.hasNext())
		{
			i = Maths.gcd(i, checkRescaleValue(iterator.next().value));
			if (i == 1) return;
		}
		
		for (IntegerEntry<K> entry : this)
			entry.value /= i;
	}
	
	private static int checkRescaleValue(int i)
	{
		if (i <= 0) throw new IllegalStateException("Invalid rescale value.");
		return i;
	}
	
	private int removeAt(int id, INode<IntegerEntry<K>> node)
	{
		if (!node.hasLast()) this.entries[id] = node.next();
		int v = node.remove().value;
		this.sum -= v;
		return v;
	}
	
	public int remove(Object key)
	{
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if (this.entries[i] == null)
			return 0;
		else
		{
			INode<IntegerEntry<K>> node0 = INode.telomereNode(this.entries[i]);
			do
			{
				node0 = node0.next();
				if (L.equals(node0.value().key, key))
				{
					return removeAt(i, node0);
				}
			}
			while (node0.hasNext());
			return 0;
		}
	}
	
	public OptionalInt removeIf(Object key)
	{
		int hashcode = hashcode(key);
		int i = Maths.mod(hashcode, this.entries.length);
		if (this.entries[i] == null)
			return OptionalInt.empty();
		else
		{
			INode<IntegerEntry<K>> node0 = INode.telomereNode(this.entries[i]);
			do
			{
				node0 = node0.next();
				if (L.equals(node0.value().key, key))
				{
					return OptionalInt.of(removeAt(i, node0));
				}
			}
			while (node0.hasNext());
			return OptionalInt.empty();
		}
	}
	
	/**
	 * Clear all entries mapped.
	 */
	public void clear()
	{
		Arrays.fill(this.entries, null);
		this.size = 0;
		this.sum = 0;
	}
	
	/**
	 * Get <tt>Set</tt> collect all key present in this map.
	 * 
	 * @return the set.
	 */
	public Set<K> keySet()
	{
		return new IntegerMapSet();
	}
	
	@Override
	public Iterator<IntegerEntry<K>> iterator()
	{
		return new IntegerMapItr();
	}
	
	public void foreach(ObjIntConsumer<? super K> consumer)
	{
		for (IntegerEntry<? extends K> entry : this)
			consumer.accept(entry.key, entry.value);
	}
	
	/**
	 * Get sum of all value.
	 * 
	 * @return the sum.
	 */
	@Deprecated
	public int getSum()
	{
		return this.sum;
	}
	
	@Override
	public int sum()
	{
		return this.sum;
	}
	
	@Override
	public int hashCode()
	{
		int hashcode = 0;
		for (IntegerEntry<K> entry : this)
			hashcode += entry.hashCode();
		return hashcode;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		
		if (!(obj instanceof HashIntMap<?>)) return false;
		
		HashIntMap<?> map = (HashIntMap<?>) obj;
		
		if (map.size() != this.size) return false;
		
		try
		{
			for (IntegerEntry<?> entry : this)
			{
				if (!map.contains(entry.key, entry.value)) return false;
			}
		}
		catch (ClassCastException | NullPointerException e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		if (isEmpty()) return "{}";
		StringBuilder builder = new StringBuilder().append('{');
		Iterator<IntegerEntry<K>> iterator = iterator();
		builder.append(iterator.next());
		while (iterator.hasNext())
		{
			builder.append(',').append(iterator.next());
		}
		return builder.append('}').toString();
	}
	
	/**
	 * Transform all value.
	 * 
	 * @param operator the operator to transform value.
	 */
	public void transformAll(@Nonnull IntUnaryOperator operator)
	{
		this.sum = 0;
		for (IntegerEntry<K> entry : this)
		{
			int t = operator.applyAsInt(entry.value);
			this.sum += t;
			entry.setValue(t);
		}
	}
	
	class IntegerMapItr implements Iterator<IntegerEntry<K>>
	{
		boolean					modified;
		int						pointer	= -1;
		INode<IntegerEntry<K>>	currentNode;
		
		@Override
		public boolean hasNext()
		{
			if (this.currentNode == null || !this.currentNode.hasNext())
			{
				int pointer = this.pointer;
				while (++pointer < HashIntMap.this.entries.length && HashIntMap.this.entries[pointer] == null)
					;
				return pointer < HashIntMap.this.entries.length && HashIntMap.this.entries[pointer] != null;
			}
			return true;
		}
		
		@Override
		public IntegerEntry<K> next()
		{
			this.modified = false;
			if (this.currentNode == null || !this.currentNode.hasNext())
			{
				while (HashIntMap.this.entries[++ this.pointer] == null);
				this.currentNode = HashIntMap.this.entries[this.pointer];
				return this.currentNode.value();
			}
			this.currentNode = this.currentNode.next();
			return this.currentNode.value();
		}
		
		@Override
		public void remove()
		{
			if (this.modified) throw new IllegalStateException("The element already removed!");
			INode<IntegerEntry<K>> old = this.currentNode;
			removeAt(this.pointer, old);
			if (this.currentNode.hasLast())
			{
				this.currentNode = this.currentNode.last();
			}
			else
			{
				this.currentNode = null;
			}
			HashIntMap.this.sum -= old.remove().value;
			HashIntMap.this.size--;
			this.modified = true;
		}
	}
	
	class IntegerMapSet extends AbstractSet<K>
	{
		@Override
		public int size()
		{
			return HashIntMap.this.size;
		}
		
		@Override
		public boolean isEmpty()
		{
			return HashIntMap.this.isEmpty();
		}
		
		@Override
		public boolean contains(Object o)
		{
			return HashIntMap.this.containsKey(o);
		}
		
		@Override
		public Iterator<K> iterator()
		{
			return Iterators.transform(HashIntMap.this.iterator(), IntegerEntry::getKey);
		}
		
		@Override
		public Object[] toArray()
		{
			List<Object> list = new ArrayList<>();
			for (Object object : this)
			{
				list.add(object);
			}
			return list.toArray();
		}
		
		@Override
		public <E> E[] toArray(E[] a)
		{
			List<E> list = new ArrayList<>();
			for (Object object : this)
			{
				list.add((E) object);
			}
			return list.toArray(a);
		}
		
		@Override
		public boolean remove(Object o)
		{
			return HashIntMap.this.removeIf(o).isPresent();
		}
		
		@Override
		public boolean containsAll(Collection<?> c)
		{
			return c.stream().allMatch(HashIntMap.this::containsKey);
		}
		
		@Override
		public boolean removeAll(Collection<?> c)
		{
			boolean flag = false;
			for (Object value : c)
			{
				if (remove(value))
					flag = true;
			}
			return flag;
		}
		
		@Override
		public void clear()
		{
			HashIntMap.this.clear();
		}
	}
}
