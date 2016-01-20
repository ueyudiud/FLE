package farcore.collection.abs;

import java.util.Random;

/**
 * The abstract stack list.
 * Only override random get method.
 * Provide an default random.
 * @author ueyudiud
 *
 * @param <S>
 * @param <T>
 */
public abstract class AbstractStackList<S, T> implements IStackList<S, T>
{
	protected final Random rand = new Random();
	
	@Override
	public T randomGet()
	{
		return randomGet(rand);
	}
}