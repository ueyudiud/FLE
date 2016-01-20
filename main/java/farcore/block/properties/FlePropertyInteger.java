package farcore.block.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farcore.util.FleLog;

public class FlePropertyInteger extends FleProperty<Integer>
{
	private final int min;
	private final int max;
	
	public FlePropertyInteger(String name, int max)
	{
		this(name, 0, max);
	}
	public FlePropertyInteger(String name, int min, int max)
	{
		super(name, Integer.class, createNewMap(min, max));
		this.max = max;
		this.min = min;
	}

	private static Collection<? extends Integer> createNewMap(int min, int max)
	{
		if(min == max)
		{
			FleLog.warn(
					"Check property min value isn't less than max value, " +
					"plase check your mod. Auto change min value to max value.");
		}
		else if(min > max)
		{
			FleLog.warn(
				"Check property min value is more than max value, " +
				"plase check your mod. Auto change min value to max value.");
			max ^= min;
			min ^= max;
			max ^= min;
		}
		List<Integer> list = new ArrayList();
		for(int i = min; i <= max; ++i)
			list.add(new Integer(i));
		return list;
	}

	@Override
	protected String name(Integer value)
	{
		return value.toString();
	}	
}