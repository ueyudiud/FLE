package farcore.lib.collection;

import java.util.Random;
import java.util.function.Function;

import farcore.util.L;

@FunctionalInterface
public interface Selector<E> extends Function<Random, E>
{
	static <E> Selector<E> forChance(E def, E spe, int chance)
	{
		return random -> random.nextInt(10000) < chance ? spe : def;
	}
	
	static <E> Selector<E> forChance(E def, Selector<E> parent, int chance)
	{
		return random -> random.nextInt(10000) < chance ? parent.next(random) : def;
	}
	
	static <E> Selector<E> single(E element)
	{
		return random -> element;
	}
	
	static <E> Selector<E> list(E...elements)
	{
		return random -> L.random(random, elements);
	}
	
	default E next()
	{
		return next(L.random());
	}
	
	default E apply(Random random) { return next(random); }
	
	/**
	 * Get next element from selector,
	 * used the selected random generator.
	 * @param random The custom random generator.
	 * @return The selected element.
	 */
	E next(Random random);
}