/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

/**
 * @author ueyudiud
 */
class ImmutableEmptyIntMap extends ImmutableIntMap<Object>
{
	public static final IntMap<?> INSTANCE = new ImmutableEmptyIntMap();
	
	@Override
	public int size()
	{
		return 0;
	}
	
	@Override
	public boolean isEmpty()
	{
		return true;
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return false;
	}
	
	@Override
	public boolean contains(Object key, int value)
	{
		return false;
	}
	
	@Override
	public OptionalInt getIf(Object key)
	{
		return OptionalInt.empty();
	}
	
	@Override
	public int sum()
	{
		return 0;
	}
	
	@Override
	public void transformAll(IntUnaryOperator operator)
	{
		
	}
	
	@Override
	public void foreach(ObjIntConsumer<? super Object> consumer)
	{
		
	}
	
	@Override
	public void forEach(Consumer<? super IntegerEntry<Object>> action)
	{
		
	}
	
	@Override
	public Set<Object> keySet()
	{
		return ImmutableSet.of();
	}
	
	@Override
	public Iterator<IntegerEntry<Object>> iterator()
	{
		return Iterators.emptyIterator();
	}
}
