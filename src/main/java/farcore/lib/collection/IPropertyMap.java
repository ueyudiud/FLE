package farcore.lib.collection;

import java.util.Map.Entry;
import java.util.Set;

public interface IPropertyMap
{
	<V> V put(IProperty<V> property, V value);
	
	<V> V get(IProperty<V> property);
	
	<V> V remove(IProperty<V> property);
	
	boolean contain(IProperty property);
	
	Set<Entry<IProperty, Object>> entrySet();
	
	Set<IProperty> keySet();
	
	@FunctionalInterface
	interface IProperty<V>
	{
		V defValue();
	}
}