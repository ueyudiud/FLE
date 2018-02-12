/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;

/**
 * @author ueyudiud
 */
abstract class ImmutableIntMap<E> implements IntMap<E>
{
	ImmutableIntMap()
	{
	}
	
	@Override
	public int put(E key, int value)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void putOrAdd(E key, int amount)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void rescale()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void transformAll(IntUnaryOperator operator)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public OptionalInt removeIf(Object key)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
}
