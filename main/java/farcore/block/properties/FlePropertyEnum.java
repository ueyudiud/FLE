package farcore.block.properties;

import java.util.Arrays;

public class FlePropertyEnum<E extends Enum<E>> extends FleProperty<E>
{
	public FlePropertyEnum(String name, Class<? extends E> clazz,
			E...allowValues)
	{
		super(name, clazz, Arrays.asList(allowValues));
	}
	public FlePropertyEnum(String name, Class<? extends E> clazz)
	{
		super(name, clazz, Arrays.asList(clazz.getEnumConstants()));
	}

	@Override
	protected String name(E value)
	{
		return value.name();
	}
}