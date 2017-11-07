/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block.property;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import nebula.base.range.RangeInt;
import nebula.common.util.L;

public class PropertyInt implements IStandardProperty<Integer>
{
	private final String		name;
	private final int			min;
	private final int			max;
	private Integer				instance;
	private final List<Integer>	list;
	
	public PropertyInt(String name, int min, int max)
	{
		this.name = name;
		this.min = min;
		this.max = max;
		this.instance = min;
		this.list = RangeInt.range(min, max);
	}
	
	public PropertyInt setInstance(int instance)
	{
		this.instance = instance;
		return this;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public Collection<Integer> getAllowedValues()
	{
		return this.list;
	}
	
	@Override
	public Class<Integer> getValueClass()
	{
		return Integer.class;
	}
	
	@Override
	public Optional<Integer> parseValue(String value)
	{
		try
		{
			int integer = Integer.parseInt(value);
			return !L.inRange(this.max, this.min, integer) ? Optional.absent() : Optional.of(integer);
		}
		catch (NumberFormatException exception)
		{
			return Optional.absent();
		}
	}
	
	@Override
	public String getName(Integer value)
	{
		return value.toString();
	}
	
	@Override
	public Integer instance()
	{
		return this.instance;
	}
	
	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("name", this.name).add("clazz", int.class).add("values", getAllowedValues()).toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this == obj || ((obj instanceof PropertyInt) && ((PropertyInt) obj).name == this.name && ((PropertyInt) obj).max == this.max && ((PropertyInt) obj).min == this.min);
	}
	
	@Override
	public int hashCode()
	{
		return 31 * int.class.hashCode() + this.name.hashCode();
	}
}
