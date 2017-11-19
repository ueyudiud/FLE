/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class HashPropertyMap implements IPropertyMap
{
	private HashMap<IProperty<?>, Object> map;
	
	public HashPropertyMap()
	{
		this.map = new HashMap<>();
	}
	
	public HashPropertyMap(int initialCapacity)
	{
		this.map = new HashMap<>(initialCapacity);
	}
	
	public HashPropertyMap(int initialCapacity, float loadFactor)
	{
		this.map = new HashMap<>(initialCapacity, loadFactor);
	}
	
	@Override
	public <V> V put(IProperty<V> property, V value)
	{
		return (V) this.map.put(property, value);
	}
	
	@Override
	public <V> V get(IProperty<V> property)
	{
		return (V) this.map.get(property);
	}
	
	@Override
	public <V> Optional<? extends V> getOptional(IProperty<V> property)
	{
		return (Optional<? extends V>) Optional.ofNullable(this.map.get(property));
	}
	
	@Override
	public <V> V remove(IProperty<V> property)
	{
		return (V) this.map.remove(property);
	}
	
	public void clear()
	{
		this.map.clear();
	}
	
	@Override
	public boolean contain(IProperty<?> property)
	{
		return this.map.containsKey(property);
	}
	
	@Override
	public Set<Entry<IProperty<?>, ?>> entrySet()
	{
		return (Set) this.map.entrySet();
	}
	
	@Override
	public Set<IProperty<?>> keySet()
	{
		return this.map.keySet();
	}
}
