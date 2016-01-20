package farcore.block.properties;

import farcore.util.Direction;

public class FlePropertyDirection extends FlePropertyEnum<Direction>
{
	public FlePropertyDirection(String name)
	{
		super(name, Direction.class);
	}
	public FlePropertyDirection(String name, Direction[] values)
	{
		super(name, Direction.class, values);
	}	
}