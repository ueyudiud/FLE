/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
class ImmutableSingleIntMap<E> extends ImmutableIntMap<E>
{
	private E key;
	private int value;
	
	ImmutableSingleIntMap(E key, int value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public int size()
	{
		return 1;
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return L.equals(this.key, key);
	}
	
	@Override
	public boolean contains(Object key, int value)
	{
		return L.equals(this.key, key) && this.value == value;
	}
	
	@Override
	public int getOrDefault(Object key, int def)
	{
		return containsKey(key) ? this.value : def;
	}
	
	@Override
	public OptionalInt getIf(Object key)
	{
		return containsKey(key) ? OptionalInt.of(this.value) : OptionalInt.empty();
	}
	
	@Override
	public int sum()
	{
		return this.value;
	}
	
	@Override
	public Set<E> keySet()
	{
		return ImmutableSet.of(this.key);
	}
	
	@Override
	public void forEach(Consumer<? super IntegerEntry<E>> action)
	{
		action.accept(new IntegerEntry<>(this.key, this.value));
	}
	
	@Override
	public void foreach(ObjIntConsumer<? super E> consumer)
	{
		consumer.accept(this.key, this.value);
	}
	
	@Override
	public Iterator<IntegerEntry<E>> iterator()
	{
		return Iterators.forArray(new IntegerEntry<>(this.key, this.value));
	}
}
