package farcore.collection;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.sun.istack.internal.Nullable;

import farcore.collection.abs.Stack;

/**
 * The util about collection.
 * 
 * @author ueyudiud
 *		
 */
public class CollectionUtil
{
	private static final Map eMap = new AbstractMap()
	{
		private Set set;
		
		@Override
		public int size()
		{
			return 0;
		}
		
		@Override
		public Object put(Object key, Object value)
		{
			return null;
		}
		
		@Override
		public void putAll(Map map)
		{
			;
		}
		
		@Override
		public Object remove(Object target)
		{
			return null;
		}
		
		@Override
		public Set entrySet()
		{
			if (set == null)
				set = new HashSet();
			return set;
		}
		
		@Override
		public Collection values()
		{
			return entrySet();
		}
		
		@Override
		public boolean containsKey(Object key)
		{
			return false;
		}
		
		public boolean containsValue(Object value)
		{
			return false;
		}
		
		@Override
		public void clear()
		{
			;
		}
		
		@Override
		public Object clone()
		{
			try
			{
				return super.clone();
			}
			catch (Throwable e)
			{
				return this;
			}
		}
		
		@Override
		public boolean isEmpty()
		{
			return true;
		}
	};
	
	/**
	 * Return an empty map.
	 * 
	 * @return
	 */
	public static <K, V> Map<K, V> emptyMap()
	{
		return eMap;
	}
	
	/**
	 * Make a new map by function.
	 * 
	 * @param keys
	 * @param entries
	 * @return
	 */
	public static <K, V> Map<K, V> asMap(Iterator<K> keys,
			Function<? super K, V> entries)
	{
		return Maps.toMap(keys, entries);
	}
	
	/**
	 * Make a new map by entries.
	 * 
	 * @param keys
	 * @param entries
	 * @return The map. <K> The key of map. <V> The value of map.
	 */
	public static <K, V> Map<K, V> asMap(Iterator<K> keys,
			Entry<K, V>... entries)
	{
		Map<K, V> builder = new LinkedHashMap<K, V>();
		while (keys.hasNext())
		{
			K key = keys.next();
			for (Entry<K, V> entry : entries)
			{
				if (entry.getKey().equals(key))
				{
					builder.put(key, entry.getValue());
					break;
				}
			}
		}
		return new HashMap<K, V>(builder);
	}
	
	/**
	 * 
	 * @param keys
	 * @param entries
	 * @return
	 */
	public static <K, V> Map<K, V> asMap(Iterable<K> keys,
			Entry<K, V>... entries)
	{
		return asMap(keys.iterator(), entries);
	}
	
	public static <K, V> Map<K, V> asMap(Iterable<K> keys,
			Function<? super K, V> entries)
	{
		return Maps.toMap(keys, entries);
	}
	
	/**
	 * Make a new map with entries.
	 * 
	 * @param values
	 * @return
	 */
	public static <K, V> Map<K, V> asMap(Entry<K, V>... values)
	{
		Map<K, V> builder = new LinkedHashMap<K, V>();
		for (Entry<K, V> entry : values)
		{
			builder.put(entry.getKey(), entry.getValue());
		}
		return new HashMap<K, V>(builder);
	}
	
	public static <K, V> Map<K, V> copy(Map<K, V> map)
	{
		return new HashMap<K, V>(map);
	}
	
	/**
	 * Create an entry with key and value.
	 * 
	 * @param k
	 * @param v
	 * @return Entry.
	 * @see farcore.collection.CollectionUtil.FleEntry
	 */
	public static <K, V> Entry<K, V> e(@Nullable K k, @Nullable V v)
	{
		return new FleEntry(k, v);
	}
	
	public static class FleEntry<K, V> implements Entry<K, V>
	{
		K key;
		V value;
		
		public FleEntry(K aKey, V aValue)
		{
			if (aKey == null || aValue == null)
				throw new NullPointerException();
			key = aKey;
			value = aValue;
		}
		
		@Override
		public K getKey()
		{
			return key;
		}
		
		@Override
		public V getValue()
		{
			return value;
		}
		
		@Override
		public V setValue(V value)
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static <T> Stack<T>[] asArray(Map<T, Long> map)
	{
		return asArray(Long.class, map);
	}
	
	/**
	 * Change map as a array.
	 * 
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static <T, N extends Number> Stack<T>[] asArray(Class<N> clazz,
			Map<T, N> map)
	{
		Stack<T>[] sts = new Stack[map.size()];
		int i = 0;
		for (Entry<T, N> e : map.entrySet())
		{
			sts[i] = new Stack(e.getKey(), e.getValue().longValue());
			++i;
		}
		return sts;
	}
	
	public static <T> Map<T, Long> asMap(Stack<T>... list)
	{
		Map<T, Long> map = new HashMap<T, Long>();
		for (Stack<T> stack : list)
		{
			map.put(stack.obj, (long) stack.size);
		}
		return map;
	}
	
	public static <T, N extends Number> Map<T, N> asMap(Class<N> clazz,
			Stack<T>... list)
	{
		Map<T, N> map = new HashMap<T, N>();
		for (Stack<T> stack : list)
		{
			try
			{
				map.put(stack.obj, clazz.getConstructor(String.class)
						.newInstance(String.valueOf(stack.size)));
			}
			catch (Throwable e)
			{
			}
			;
		}
		return map;
	}
	
	public static <T> void add(Map<T, Long> map, T e)
	{
		if (e != null)
		{
			if (map.containsKey(e))
			{
				long a = map.get(e) + 1;
				map.put(e, a);
			}
			else
			{
				map.put(e, 1L);
			}
		}
	}
	
	public static <T> void add(Map<T, Long> map, Stack<T> e)
	{
		if (e.obj != null && e.size > 0)
		{
			if (map.containsKey(e.obj))
			{
				long a = map.get(e.obj) + e.size;
				map.put(e.obj, a);
			}
			else
			{
				map.put(e.obj, e.size);
			}
		}
	}
	
	public static <T> void add(Map<T, Long> map, Stack<T> e, long size)
	{
		if (map.containsKey(e.obj))
		{
			long a = map.get(e.obj) + e.size * size;
			map.put(e.obj, a);
		}
		else
		{
			map.put(e.obj, e.size * size);
		}
	}
	
	public static <T> void add(Map<T, Long> map, T... e)
	{
		for (T t : e)
			add(map, t);
	}
	
	public static <T> void add(Map<T, Long> map, long size, T... e)
	{
		for (T t : e)
			add(map, new Stack<T>(t, size));
	}
	
	public static <T> void add(Map<T, Long> map, Stack<T>... e)
	{
		for (Stack<T> ts : e)
			add(map, ts);
	}
	
	public static <T> void add(Map<T, Long> map, long size, Stack<T>... e)
	{
		for (Stack<T> ts : e)
			add(map, ts, size);
	}
	
	public static <T> void add(Map<T, Long> aMap, Map<T, Long> aValue)
	{
		for (Entry<T, Long> e : aValue.entrySet())
			add(aMap, new Stack(e.getKey(), e.getValue()));
	}
	
	public static <T> void add(Map<T, Long> aMap, long size,
			Map<T, Long> aValue)
	{
		for (Entry<T, Long> e : aValue.entrySet())
			add(aMap, new Stack(e.getKey(), e.getValue() * size));
	}
	
	public static <T> boolean remove(Map<T, Long> map, T e)
	{
		if (e != null)
		{
			if (map.containsKey(e))
			{
				long a = map.get(e) - 1;
				if (a > 0)
					map.put(e, a);
				else
					map.remove(e);
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	public static <T> long remove(Map<T, Long> map, Stack<T> e)
	{
		if (e.obj != null && e.size > 0)
		{
			if (map.containsKey(e.obj))
			{
				long a = map.get(e.obj) - e.size;
				if (a > 0)
					map.put(e.obj, a);
				else
					map.remove(e.obj);
				return a > 0 ? e.size : a + e.size;
			}
			return 0;
		}
		return 0;
	}
	
	public static <T> long remove(Map<T, Long> map, Stack<T> e, long size)
	{
		return remove(map, new Stack(e.obj, e.size * size));
	}
	
	public static <T> void remove(Map<T, Long> map, T... e)
	{
		for (T t : e)
			remove(map, t);
	}
	
	public static <T> void remove(Map<T, Long> map, long size, T... e)
	{
		for (T t : e)
			remove(map, new Stack<T>(t, size));
	}
	
	public static <T> void remove(Map<T, Long> map, Stack<T>... e)
	{
		for (Stack<T> ts : e)
			remove(map, ts);
	}
	
	public static <T> void remove(Map<T, Long> map, long size, Stack<T>... e)
	{
		for (Stack<T> ts : e)
			remove(map, ts, size);
	}
	
	public static <T> void remove(Map<T, Long> aMap, Map<T, Long> aValue)
	{
		for (Entry<T, Long> e : aValue.entrySet())
			remove(aMap, new Stack(e.getKey(), e.getValue()));
	}
	
	public static <T> void remove(Map<T, Long> aMap, int size,
			Map<T, Long> aValue)
	{
		for (Entry<T, Long> e : aValue.entrySet())
			remove(aMap, new Stack(e.getKey(), e.getValue() * size));
	}
	
	public static <T> Stack<T>[] multiply(Stack<T>[] aStacks, int size)
	{
		Stack<T>[] ret = new Stack[aStacks.length];
		int i = 0;
		for (Stack<T> stack : aStacks)
		{
			ret[i] = stack.copy();
			ret[i].size *= size;
			++i;
		}
		return ret;
	}
	
	public static <T> Set<T> asSet(T... elements)
	{
		Set<T> set = new HashSet<T>();
		for (T ele : elements)
			if (ele != null)
				set.add(ele);
		return set;
	}
	
	public static <T> Set<T> asSetWith(Object... elements)
	{
		Set<T> set = new HashSet<T>();
		for (Object ele : elements)
			if (ele != null)
				set.add((T) ele);
		return set;
	}
}