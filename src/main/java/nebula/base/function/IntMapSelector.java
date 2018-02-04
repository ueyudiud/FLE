/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.function;

import java.util.Random;

import nebula.base.IntMap;
import nebula.base.IntegerEntry;

/**
 * @author ueyudiud
 */
class IntMapSelector<E> implements Selector<E>
{
	final IntMap<? extends E> map;
	
	public IntMapSelector(IntMap<? extends E> map)
	{
		this.map = map;
	}
	
	@Override
	public E next(Random random)
	{
		int i = random.nextInt(this.map.sum());
		for (IntegerEntry<? extends E> entry : this.map)
		{
			if ((i -= entry.getValue()) < 0)
			{
				return entry.getKey();
			}
		}
		throw new InternalError();
	}
}
