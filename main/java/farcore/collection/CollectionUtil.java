package farcore.collection;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import farcore.collection.abs.Stack;

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
			if(set == null) set = new HashSet();
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
			catch(Throwable e)
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
	
	public static <K, V> Map<K, V> emptyMap()
	{
		return eMap;
	}
	public static <K, V> Map<K, V> asMap(Iterator<K> keys, Function<? super K, V> entries)
	{
		return Maps.toMap(keys, entries);
	}
	public static <K, V> Map<K, V> asMap(Iterator<K> keys, Entry<K, V>...entries)
	{
	    Map<K, V> builder = new LinkedHashMap<K, V>();
	    while (keys.hasNext())
	    {
	    	K key = keys.next();
	    	for(Entry<K, V> entry : entries)
	    	{
	    		if(entry.getKey().equals(key))
	    		{
	    			builder.put(key, entry.getValue());
	    			break;
	    		}
	    	}
	    }
	    return new HashMap<K, V>(builder);
	}
	public static <K, V> Map<K, V> asMap(Iterable<K> keys, Entry<K, V>...entries)
	{
		return asMap(keys.iterator(), entries);
	}
	public static <K, V> Map<K, V> asMap(Iterable<K> keys, Function<? super K, V> entries)
	{
		return Maps.toMap(keys, entries);
	}
	public static <K, V> Map<K, V> asMap(Entry<K, V>...values)
	{
	    Map<K, V> builder = new LinkedHashMap<K, V>();
	    for(Entry<K, V> entry : values)
	    {
	    	builder.put(entry.getKey(), entry.getValue());
	    }
	    return new HashMap<K, V>(builder);
	}
	public static <K, V> Map<K, V> copy(Map<K, V> map)
	{
		return new HashMap<K, V>(map);
	}
	public static class FleEntry<K, V> implements Entry<K, V>
	{		
		K key;
		V value;
		
		public FleEntry(K aKey, V aValue)
		{
			if(aKey == null || aValue == null)
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
	
	public static <T> Stack<T>[] asArray(Map<T, Integer> aMap)
	{
		Stack<T>[] sts = new Stack[aMap.size()];
		int i = 0;
		for(Entry<T, Integer> e : aMap.entrySet())
		{
			sts[i] = new Stack(e.getKey(), e.getValue());
			++i;
		}
		return sts;
	}
	public static <T> Map<T, Integer> asMap(Stack<T>...list)
	{
		Map<T, Integer> map = new HashMap<T, Integer>();
		for(Stack<T> stack : list)
		{
			map.put(stack.obj, stack.size);
		}
		return map;
	}
	public static <T> void add(Map<T, Integer> map, T e)
	{
		if(e != null)
		{
			if(map.containsKey(e))
			{
				int a = map.get(e) + 1;
				map.put(e, a);
			}
			else
			{
				map.put(e, 1);
			}
		}
	}
	public static <T> void add(Map<T, Integer> map, Stack<T> e)
	{
		if(e.obj != null && e.size > 0)
		{
			if(map.containsKey(e.obj))
			{
				int a = map.get(e.obj) + e.size;
				map.put(e.obj, a);
			}
			else
			{
				map.put(e.obj, e.size);
			}
		}
	}
	public static <T> void add(Map<T, Integer> map, Stack<T> e, int size)
	{
		if(map.containsKey(e.obj))
		{
			int a = map.get(e.obj) + e.size * size;
			map.put(e.obj, a);
		}
		else
		{
			map.put(e.obj, e.size * size);
		}
	}
	public static <T> void add(Map<T, Integer> map, T...e)
	{
		for(T t : e) add(map, t);
	}
	public static <T> void add(Map<T, Integer> map, int size, T...e)
	{
		for(T t : e) add(map, new Stack<T>(t, size));
	}
	public static <T> void add(Map<T, Integer> map, Stack<T>...e)
	{
		for(Stack<T> ts : e) add(map, ts);
	}
	public static <T> void add(Map<T, Integer> map, int size, Stack<T>...e)
	{
		for(Stack<T> ts : e) add(map, ts, size);
	}
	public static <T> void add(Map<T, Integer> aMap,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) add(aMap, new Stack(e.getKey(), e.getValue()));
	}
	public static <T> void add(Map<T, Integer> aMap, int size,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) add(aMap, new Stack(e.getKey(), e.getValue() * size));
	}

	public static <T> boolean remove(Map<T, Integer> map, T e)
	{
		if(e != null)
		{
			if(map.containsKey(e))
			{
				int a = map.get(e) - 1;
				if(a > 0)
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
	public static <T> int remove(Map<T, Integer> map, Stack<T> e)
	{
		if(e.obj != null && e.size > 0)
		{
			if(map.containsKey(e.obj))
			{
				int a = map.get(e.obj) - e.size;
				if(a > 0)
					map.put(e.obj, a);
				else
					map.remove(e.obj);
				return a > 0 ? e.size : a + e.size;
			}
			return 0;
		}
		return 0;
	}
	public static <T> int remove(Map<T, Integer> map, Stack<T> e, int size)
	{
		return remove(map, new Stack(e.obj, e.size * size));
	}
	public static <T> void remove(Map<T, Integer> map, T...e)
	{
		for(T t : e) remove(map, t);
	}
	public static <T> void remove(Map<T, Integer> map, int size, T...e)
	{
		for(T t : e) remove(map, new Stack<T>(t, size));
	}
	public static <T> void remove(Map<T, Integer> map, Stack<T>...e)
	{
		for(Stack<T> ts : e) remove(map, ts);
	}
	public static <T> void remove(Map<T, Integer> map, int size, Stack<T>...e)
	{
		for(Stack<T> ts : e) remove(map, ts, size);
	}
	public static <T> void remove(Map<T, Integer> aMap,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) remove(aMap, new Stack(e.getKey(), e.getValue()));
	}
	public static <T> void remove(Map<T, Integer> aMap, int size,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) remove(aMap, new Stack(e.getKey(), e.getValue() * size));
	}
	public static <T> Stack<T>[] multiply(Stack<T>[] aStacks, int size)
	{
		Stack<T>[] ret = new Stack[aStacks.length];
		int i = 0;
		for(Stack<T> stack : aStacks)
		{
			ret[i] = stack.copy();
			ret[i].size *= size;
			++i;
		}
		return ret;
	}
}