/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import nebula.base.function.Applicable;

public interface IPropertyMap
{
	<V> V put(IProperty<V> property, @Nonnull V value);
	
	<V> V get(IProperty<V> property);
	
	<V> V remove(IProperty<V> property);
	
	<V> Optional<? extends V> getOptional(IProperty<V> property);
	
	void clear();
	
	boolean contain(IProperty<?> property);
	
	Set<Entry<IProperty<?>, ?>> entrySet();
	
	Set<IProperty<?>> keySet();
	
	@FunctionalInterface
	interface IProperty<V> extends Applicable<V>
	{
		static <V> IProperty<V> to()
		{
			return new ToNull();
		}
		
		static <V> IProperty<V> to(V value)
		{
			return () -> value;
		}
		
		class ToNull implements IProperty
		{
			@Override
			public Object get()
			{
				return null;
			}
		}
		
		static <V> IProperty<V> to(Class<? extends V> constructor)
		{
			return () ->
			{
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
	}
}
