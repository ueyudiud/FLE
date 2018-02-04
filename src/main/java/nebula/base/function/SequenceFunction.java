/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ueyudiud
 */
public interface SequenceFunction<T, R>
{
	R apply(int i, T t);
	
	default SequenceConsumer<T> andThen(Consumer<? super R> f)
	{
		return (i, t) -> f.accept(apply(i, t));
	}
	
	default <R1> SequenceFunction<T, R1> andThen(Function<? super R, R1> f)
	{
		return (i, t) -> f.apply(apply(i, t));
	}
}
