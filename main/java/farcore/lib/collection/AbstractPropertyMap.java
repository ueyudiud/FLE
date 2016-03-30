package farcore.lib.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class AbstractPropertyMap<P extends IProperty> implements IPropertyMap<P>
{
	private final ImmutableSet<P> set;
	protected Map<P, Object> map = new HashMap();
	
	public AbstractPropertyMap(P...target)
	{
		set = ImmutableSet.copyOf(target);
	}
	
	@Override
	public Set<P> propertySet()
	{
		return set;
	}

	@Override
	public <T, O> T get(IProperty<T, O> property) 
	{
		return set.contains(property) ? property.get((O) map.get(property)) : null;
	}

	@Override
	public <T, O> T put(IProperty<T, O> property, T target)
	{
		if(set.contains(property))
		{
			O o = (O) map.put((P) property, property.set(target));
			return o != null ? property.get(o) : null;
		}
		return null;
	}
	
	@Override
	public <T, O> T remove(IProperty<T, O> property)
	{
		if(set.contains(property))
		{
			O o = (O) map.remove(property);
			return o != null ? property.get(o) : null;
		}
		return null;
	}

	@Override
	public Map<P, Object> cast()
	{
		return map;
	}
}