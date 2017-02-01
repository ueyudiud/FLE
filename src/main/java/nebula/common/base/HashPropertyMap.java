package nebula.common.base;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class HashPropertyMap implements IPropertyMap
{
	private HashMap<IProperty, Object> map;
	
	public HashPropertyMap()
	{
		map = new HashMap();
	}
	public HashPropertyMap(int initialCapacity)
	{
		map = new HashMap(initialCapacity);
	}
	public HashPropertyMap(int initialCapacity, float loadFactor)
	{
		map = new HashMap(initialCapacity, loadFactor);
	}
	
	@Override
	public <V> V put(IProperty<V> property, V value)
	{
		return (V) map.put(property, value);
	}
	
	@Override
	public <V> V get(IProperty<V> property)
	{
		return (V) map.get(property);
	}
	
	@Override
	public <V> V remove(IProperty<V> property)
	{
		return (V) map.remove(property);
	}
	
	@Override
	public boolean contain(IProperty property)
	{
		return map.containsKey(property);
	}
	
	@Override
	public Set<Entry<IProperty, Object>> entrySet()
	{
		return map.entrySet();
	}
	
	@Override
	public Set<IProperty> keySet()
	{
		return map.keySet();
	}
}