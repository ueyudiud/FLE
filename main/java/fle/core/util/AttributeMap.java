package fle.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttributeMap
{
	private final Map<Attribute, Object> map;

	public AttributeMap()
	{
		this(16);
	}
	public AttributeMap(int cap)
	{
		this(16, 0.75F);
	}
	public AttributeMap(int cap, float speed)
	{
		map = new HashMap<Attribute, Object>(cap, speed);
	}
	
	public <T> T getAttribute(Attribute<T> a)
	{
		return map.containsKey(a) ? (T) map.get(a) : a.getDefaultValue();
	}
	
	public <T> void setAttribute(Attribute<T> a)
	{
		map.put(a, a.getDefaultValue());
	}
	
	public <T> void setAttribute(Attribute<T> a, T value)
	{
		map.put(a, value);
	}
	
	public <T> T removeAttribute(Attribute<T> a)
	{
		return (T) map.remove(a);
	}
	
	public void clear()
	{
		map.clear();
	}
	
	public Set<Attribute> getAttributes()
	{
		return map.keySet();
	}
	
	public int size()
	{
		return map.size();
	}
}