package farcore.lib.collection;

import java.util.Random;

import farcore.util.L;

public interface Selector<E>
{
	static <E> Selector<E> single(E element)
	{
		return random -> element;
	}
	
	default E next()
	{
		return next(L.random());
	}
	
	/**
	 * Get next element from selector,
	 * used the selected random generator.
	 * @param random The custom random generator.
	 * @return The selected element.
	 */
	E next(Random random);
}