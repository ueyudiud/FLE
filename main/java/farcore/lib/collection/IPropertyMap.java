package farcore.lib.collection;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author ueyudiud
 *
 */
public interface IPropertyMap<P extends IProperty>
{
	Set<P> propertySet();
	
	<T, O> T get(IProperty<T, O> property);
	
	<T, O> T put(IProperty<T, O> property, T target);
	
	<T, O> T remove(IProperty<T, O> property);
	
	Map<P, Object> cast();
}