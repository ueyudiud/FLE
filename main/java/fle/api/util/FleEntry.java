package fle.api.util;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class FleEntry<K, V> implements Entry<K, V>
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