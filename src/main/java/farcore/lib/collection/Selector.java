package farcore.lib.collection;

import java.util.Random;

import farcore.util.U;

public interface Selector<E>
{
	public static <E> Selector<E> single(E element)
	{
		return (Random random) -> element;
	}

	default E next()
	{
		return next(U.L.random());
	}

	/**
	 * Get next element from selector,
	 * used the selected random generator.
	 * @param random The custom random generator.
	 * @return The selected element.
	 */
	E next(Random random);
}
