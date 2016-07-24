package farcore.lib.prop;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.properties.PropertyHelper;

public class PropertyString extends PropertyHelper<String>
{
	private List<String> allowedValues;

	public PropertyString(String name, String...strings)
	{
		super(name, String.class);
		allowedValues = ImmutableList.copyOf(strings);
	}
	
	@Override
	public Collection<String> getAllowedValues()
	{
		return allowedValues;
	}
	
	@Override
	public Optional<String> parseValue(String value)
	{
		return Optional.of(value);
	}
	
	@Override
	public String getName(String value)
	{
		return value;
	}
}