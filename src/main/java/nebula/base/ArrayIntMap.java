/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import nebula.common.util.Maths;

/**
 * @author ueyudiud
 */
public class ArrayIntMap<E> extends AbstractIntMap<E>
{
	private Object[] keys;
	private int[] values;
	private int size, sum;
	private int free = 0;
	private Set<E> keySet;
	
	public ArrayIntMap()
	{
		this(16);
	}
	
	public ArrayIntMap(int initaialCapacity)
	{
		this.keys = new Object[initaialCapacity];
		this.values = new int[initaialCapacity];
	}
	
	@Override
	public Iterator<IntegerEntry<E>> iterator()
	{
		return new Iterator<IntegerEntry<E>>()
		{
			int pos;
			int count;
			
			@Override
			public boolean hasNext()
			{
				return this.count < ArrayIntMap.this.size;
			}
			
			@Override
			public IntegerEntry<E> next()
			{
				while (ArrayIntMap.this.keys[this.pos] == null)
					this.pos ++;
				this.count ++;
				IntegerEntry<E> entry = new IntegerEntry(ArrayIntMap.this.keys[this.pos], ArrayIntMap.this.values[this.pos]);
				this.pos ++;
				return entry;
			}
			
			@Override
			public void remove()
			{
				ArrayIntMap.this.keys[this.pos] = null;
				ArrayIntMap.this.sum -= ArrayIntMap.this.values[this.pos];
				ArrayIntMap.this.size --;
				ArrayIntMap.this.free = Math.min(this.pos, ArrayIntMap.this.free);
			}
		};
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
	
	private int indexOfKey(@Nonnull Object key)
	{
		for (int i = 0; i < this.keys.length; ++i)
		{
			Object k = this.keys[i];
			if (k != null && key.equals(k))
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return key != null && indexOfKey(key) != -1;
	}
	
	@Override
	public boolean contains(Object key, int value)
	{
		if (key == null)
			return false;
		int id = indexOfKey(key);
		return this.values[id] == value;
	}
	
	@Override
	public OptionalInt getIf(Object key)
	{
		if (key == null)
			return OptionalInt.empty();
		int id = indexOfKey(key);
		return id == -1 ? OptionalInt.empty() : OptionalInt.of(this.values[id]);
	}
	
	private void ensureCapacity(int len)
	{
		if (this.size + len > this.keys.length)
		{
			this.keys = Arrays.copyOf(this.keys, this.size + len + 16);
			this.values = Arrays.copyOf(this.values, this.size + len + 16);
		}
	}
	
	private void free()
	{
		while (this.keys[this.free] != null)
			++ this.free;
	}
	
	@Override
	public int put(E key, int value)
	{
		ensureCapacity(1);
		free();
		this.keys[this.free] = key;
		int old = this.values[this.free];
		this.values[this.free] = value;
		this.free ++;
		this.size ++;
		this.sum += (value - old);
		return old;
	}
	
	@Override
	public void putAll(IntMap<? extends E> map)
	{
		ensureCapacity(map.size());
		this.sum += map.sum();
		this.size += map.size();
		for (IntegerEntry<? extends E> entry : map)
		{
			free();
			this.keys[this.free] = entry.getKey();
			this.values[this.free] = entry.getValue();
			this.free ++;
		}
	}
	
	@Override
	public void putOrAdd(E key, int amount)
	{
		int idx = indexOfKey(key);
		if (idx != -1)
		{
			this.values[idx] += amount;
			this.sum += amount;
		}
		else
		{
			put(key, idx);
		}
	}
	
	private static int checkRescaleValue(int i)
	{
		if (i <= 0) throw new IllegalStateException("Invalid rescale value.");
		return i;
	}
	
	@Override
	public void rescale() throws IllegalStateException
	{
		if (isEmpty()) return;
		
		int scale = 0;
		int i = 0;
		while (this.keys[i] != null)
			++ i;
		scale = this.values[i ++];
		for (; i < this.values.length; ++i)
		{
			if (this.keys[i] != null)
			{
				scale = Maths.gcd(scale, checkRescaleValue(this.values[i]));
			}
		}
		for (i = 0; i < this.values.length; this.values[i ++] /= scale);
	}
	
	@Override
	public OptionalInt removeIf(Object key)
	{
		if (key == null)
			return OptionalInt.empty();
		int idx = indexOfKey(key);
		if (idx == -1)
			return OptionalInt.empty();
		this.free = Math.min(this.free, idx);
		this.size --;
		int result = this.values[idx];
		this.sum -= result;
		return OptionalInt.of(result);
	}
	
	@Override
	public void clear()
	{
		Arrays.fill(this.keys, null);
		this.size = 0;
		this.sum = 0;
		this.free = 0;
	}
	
	@Override
	public int sum()
	{
		return this.sum;
	}
	
	@Override
	public Set<E> keySet()
	{
		if (this.keySet == null)
		{
			this.keySet = new KeySet();
		}
		return this.keySet;
	}
	
	@Override
	public OptionalInt find(Predicate<E> predicate)
	{
		for (int i = 0; i < this.keys.length; ++i)
		{
			if (this.keys[i] != null && predicate.test((E) this.keys[i]))
			{
				return OptionalInt.of(this.values[i]);
			}
		}
		return OptionalInt.empty();
	}
	
	public void transformAll(@Nonnull IntUnaryOperator operator)
	{
		this.sum = 0;
		for (int i = 0; i < this.values.length; ++i)
		{
			if (this.keys[i] != null)
			{
				this.sum += this.values[i] = operator.applyAsInt(this.values[i]);
			}
		}
	}
	
	class KeySet extends AbstractSet<E>
	{
		@Override
		public Iterator<E> iterator()
		{
			return new Iterator<E>()
			{
				int pos = 0;
				int count = 0;
				
				@Override
				public boolean hasNext()
				{
					return this.count < ArrayIntMap.this.size;
				}
				
				@Override
				public E next()
				{
					while (ArrayIntMap.this.keys[this.pos] == null)
						++ this.pos;
					++ this.count;
					return (E) ArrayIntMap.this.keys[this.pos];
				}
				
				@Override
				public void remove()
				{
					ArrayIntMap.this.keys[this.pos] = null;
					ArrayIntMap.this.sum -= ArrayIntMap.this.values[this.pos];
					ArrayIntMap.this.size --;
					ArrayIntMap.this.free = Math.min(this.pos, ArrayIntMap.this.free);
				}
				
				@Override
				public void forEachRemaining(Consumer<? super E> action)
				{
					int i = this.pos;
					for (; i < ArrayIntMap.this.keys.length; ++i)
					{
						if (ArrayIntMap.this.keys[i] != null)
						{
							action.accept((E) ArrayIntMap.this.keys[i]);
						}
					}
					this.count = ArrayIntMap.this.size;
				}
			};
		}
		
		@Override
		public int size()
		{
			return ArrayIntMap.this.size;
		}
	}
}
