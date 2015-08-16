package fle.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class FleEntry<K, V> implements Entry<K, V>
{
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