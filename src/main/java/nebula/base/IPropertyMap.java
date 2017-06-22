package nebula.base;

import java.util.Map.Entry;
import java.util.Set;

import nebula.base.function.Appliable;

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
		static <V> IProperty<V> to()
		{
			return new ToNull();
		}
		
		class ToNull implements IProperty
		{
			@Override
			public Object defValue()
			{
				return null;
			}
			
		}
		
		static <V> IProperty<V> to(Class<? extends V> constructor)
		{
			return ()-> {
				try
				{
					return constructor.newInstance();
				}
				catch (Exception exception)
				{
					throw new InternalError(exception);
				}
			};
		}
		
		@Override
		default V apply()
		{
			return defValue();
		}
		
		V defValue();
	}
}