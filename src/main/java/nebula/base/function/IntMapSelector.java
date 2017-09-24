/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.function;

import java.util.Random;

import nebula.base.IntegerEntry;
import nebula.base.IntegerMap;

/**
 * @author ueyudiud
 */
class IntMapSelector<E> implements Selector<E>
{
	final IntegerMap<? extends E> map;
	
	public IntMapSelector(IntegerMap<? extends E> map)
	{
		this.map = map;
	}
	
	@Override
	public E next(Random random)
	{
		int i = random.nextInt(this.map.getSum());
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