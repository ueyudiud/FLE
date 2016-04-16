package farcore.lib.collection;

import java.util.Map;
import java.util.Set;

/**
 * The property map, a collection current
 * property.
 * @author ueyudiud
 * 
 */
public interface IPropertyMap<P extends IProperty>
{
	/**
	 * Get allowed properties.
	 * @return
	 */
	Set<P> propertySet();
	
	<T, O> T get(IProperty<T, O> property);
	
	<T, O> T put(IProperty<T, O> property, T target);
	
	<T, O> T remove(IProperty<T, O> property);
	
	Map<P, Object> cast();
}