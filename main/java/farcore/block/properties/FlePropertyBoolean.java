package farcore.block.properties;

import java.util.Arrays;
import java.util.List;

public class FlePropertyBoolean extends FleProperty<Boolean>
{
	private static final List<Boolean> list = Arrays.asList(Boolean.FALSE, Boolean.TRUE);
	
	public FlePropertyBoolean(String name)
	{
		super(name, Boolean.class, list);
	}

	@Override
	protected String name(Boolean value)
	{
		return value.toString();
	}
}