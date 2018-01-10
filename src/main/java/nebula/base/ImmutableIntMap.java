/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.OptionalInt;

/**
 * @author ueyudiud
 */
public abstract class ImmutableIntMap<E> implements IntMap<E>
{
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
	public void rescale() throws IllegalStateException
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
