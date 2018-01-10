/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.function;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.base.IntMap;
import nebula.common.util.L;

/**
 * The selector, is a functional interface.
 * <p>
 * 
 * The result of function is randomly, or use specific RNG to get the result.
 * <p>
 * 
 * Used the same random with same seed <i>should</i> return same result.
 * 
 * @author ueyudiud
 * @param <E> The return element type.
 */
@FunctionalInterface
public interface Selector<E> extends Function<Random, E>
{
	// The selector provide methods.
	
	static <E> Selector<E> forChance(@Nullable E def, @Nullable E spe, int chance)
	{
		return chance == 0 ? single(def) : random -> random.nextInt(10000) < chance ? spe : def;
	}
	
	static <E> Selector<E> forChance(@Nullable E def, @Nonnull Selector<E> parent, int chance)
	{
		Objects.requireNonNull(parent);
		return chance == 0 ? single(def) : random -> random.nextInt(10000) < chance ? parent.next(random) : def;
	}
	
	/**
	 * Return a <tt>Selector</tt> with always return a constant.
	 * 
	 * @param element the constant to return.
	 * @return the selector.
	 */
	static <E> Selector<E> single(@Nullable E element)
	{
		return new Selector<E>()
		{
			@Override
			public E next(Random random)
			{
				return element;
			}
			
			@Override
			public E next(int random)
			{
				return element;
			}
		};
	}
	
	/**
	 * Return a <tt>Selector</tt> with weight map from
	 * {@link nebula.base.IntMap}. the data will be not copied to selector,
	 * the modification of map can still affect the result of selecting.
	 * 
	 * @param map the integer map, each key in map may can be returned by
	 *            <tt>next()</tt> with weight (which is mapped value in map) to
	 *            return.
	 * @return the selector.
	 */
	static <E> Selector<E> of(IntMap<? extends E> map)
	{
		return new IntMapSelector<>(map);
	}
	
	/**
	 * Return a <tt>Selector</tt> can select result from specific elements.
	 * 
	 * @param elements the elements can be returned by <tt>next()</tt>
	 * @return the selector.
	 */
	static <E> Selector<E> list(E...elements)
	{
		return random -> L.random(random, elements);
	}
	
	// The selector provide methods end.
	
	/**
	 * Wrapped as a <tt>Applicable</tt>, which used default RNG to select
	 * result.
	 * 
	 * @return the function.
	 * @see nebula.common.util.L#random()
	 */
	default Applicable<E> anyTo()
	{
		return () -> next();
	}
	
	default E next()
	{
		return next(L.random());
	}
	
	default E apply(Random random)
	{
		return next(random);
	}
	
	/**
	 * Get next element from selector, used the selected random generator.
	 * 
	 * @param random the custom random generator.
	 * @return the selected element.
	 */
	E next(Random random);
	
	/**
	 * The seed based generate random.
	 * <p>
	 * For each seed, the result will be a constant unless which time the method
	 * is called.
	 * 
	 * @param random the random seed.
	 * @return the selected element.
	 */
	default E next(int random)
	{
		return next(new Random(random));
	}
}
