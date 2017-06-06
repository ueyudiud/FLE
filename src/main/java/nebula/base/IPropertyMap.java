package nebula.base;

import java.util.Map.Entry;

import nebula.base.function.Appliable;

import java.util.Set;

public interface IPropertyMap
{
	<V> V put(IProperty<V> property, V value);
	
	<V> V get(IProperty<V> property);
	
	<V> V remove(IProperty<V> property);
	
	void clear();
	
	boolean contain(IProperty<?> property);
	
	Set<Entry<IProperty<?>, ?>> entrySet();
	
	Set<IProperty<?>> keySet();
	
	@FunctionalInterface
	interface IProperty<V> extends Appliable<V>
	{
		@Override
		default V apply()
		{
			return defValue();
		}
		
		V defValue();
	}
}