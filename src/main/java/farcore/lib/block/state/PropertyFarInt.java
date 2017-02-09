package farcore.lib.block.state;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class PropertyFarInt implements IFarProperty<Integer>
{
	private final String name;
	private final int min;
	private final int max;
	private Integer instance;
	private final ImmutableList<Integer> list;
	
	public PropertyFarInt(String name, int min, int max)
	{
		this.name = name;
		this.min = min;
		this.max = max;
		this.instance = min;
		ImmutableList.Builder<Integer> builder = ImmutableList.builder();
		for (int i = min; i <= max; ++i)
		{
			builder.add(i);
		}
		this.list = builder.build();
	}
	
	public PropertyFarInt setInstance(int instance)
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
		Integer integer = Integer.getInteger(value);
		return integer == null || (integer > this.max || integer < this.min) ? Optional.absent() : Optional.of(integer);
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
}