package farcore.block.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.minecraft.block.properties.IProperty;

import com.google.common.base.Objects;

/**
 * The block properties. (FLE version)
 * @author ueyudiud
 * 
 * @param <T> The property type of properties. (example : meta for integer)
 */
public abstract class FleProperty<T extends Comparable<T>> implements IProperty, Iterable<T>
{
	/**
	 * The property name.
	 */
	protected final String name;
	protected final Class<? extends T> clazz;
	protected Collection<T> collection;
	protected String toString;
	
	public FleProperty(String name, Class<? extends T> clazz, Collection<? extends T> allowValues)
	{
		this.name = name;
		this.clazz = clazz;
		this.collection = new ArrayList<T>(allowValues);
		toString = Objects.toStringHelper(this).add("name", name).add("valueType", clazz).toString();
	}
	
	FleProperty(String name, Class<? extends T> clazz)
	{
		this.name = name;
		this.clazz = clazz;
		toString = Objects.toStringHelper(this).add("name", name).add("valueType", clazz).toString();
	}

	/**
	 * Get property name.
	 */
	@Override
	public final String getName()
	{
		return name;
	}

	/**
	 * Get all values are allow.
	 */
	@Override
	public Collection<T> getAllowedValues()
	{
		if(collection == null)
		{
			collection = getCollection();
		}
		return collection;
	}
	
	protected Collection<T> getCollection()
	{
		return null;
	}

	@Override
	public final Iterator<T> iterator()
	{
		return getAllowedValues().iterator();
	}

	/**
	 * Get values type, which should implements {@link java.lang.Comparable}
	 * @return The class type.
	 */
	@Override
	public final Class<? extends Comparable<T>> getValueClass()
	{
		return clazz;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode() * 31 + clazz.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj instanceof FleProperty)
		{
			return clazz.equals(((FleProperty) obj).clazz) &&
					name.equals(((FleProperty) obj).name);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return toString;
	}
	
	protected abstract String name(T value);
	
	/**
	 * Get value name.
	 * @param value
	 * @return name of value.
	 */
	@Override
	public String getName(Comparable value)
	{
		try
		{
			return name((T) value);
		}
		catch(ClassCastException exception)
		{
			return "unknown";
		}
	}
}