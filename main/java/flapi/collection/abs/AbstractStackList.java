package flapi.collection.abs;

import java.util.Random;

public abstract class AbstractStackList<S, T> implements IStackList<S, T>
{
	final Random rand = new Random();
	
	@Override
	public T randomGet()
	{
		return randomGet(rand);
	}
}