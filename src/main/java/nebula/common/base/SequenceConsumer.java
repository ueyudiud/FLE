/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * @author ueyudiud
 */
@FunctionalInterface
public interface SequenceConsumer<T> extends ObjIntConsumer<T>
{
	default void accept(Iterable<? extends T> iterable)
	{
		if (iterable instanceof List)
		{
			List<? extends T> list = (List<? extends T>) iterable;
			for (int i = 0; i < list.size(); ++i)
			{
				accept(i, list.get(i));
			}
		}
		else
		{
			Iterator<? extends T> iterator = iterable.iterator();
			int i = 0;
			while (iterator.hasNext())
			{
				accept(i++, iterator.next());
			}
		}
	}
	
	@Override
	default void accept(T t, int value)
	{
		accept(value, t);
	}
	
	void accept(int index, T target);
	
	default Consumer<T> toNormal()
	{
		return target -> accept(0, target);
	}
}