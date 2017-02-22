/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import java.util.Random;
import java.util.function.Function;

import nebula.common.util.L;

/**
 * The selector, is a functional interface.<p>
 * 
 * The result of function is randomly, or use selected Random number generator to
 * has the specific result.
 * @author ueyudiud
 *
 * @param <E> The return element type.
 */
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
	
	default <T> Function<T, E> anyTo()
	{
		return t -> next();
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