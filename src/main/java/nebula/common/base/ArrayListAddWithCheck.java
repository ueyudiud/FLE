/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import static nebula.common.base.Judgable.NOT_NULL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author ueyudiud
 */
public class ArrayListAddWithCheck<E> extends ArrayList<E>
{
	public static <E> ArrayList<E> requireNonnull()
	{
		return new ArrayListAddWithCheck<>(NOT_NULL);
	}
	
	Predicate<E> checker;
	
	public ArrayListAddWithCheck(Predicate<E> checker)
	{
		this.checker = checker;
	}
	public ArrayListAddWithCheck(Predicate<E> checker, int initicalCapacity)
	{
		super(initicalCapacity);
		this.checker = checker;
	}
	
	@Override
	public boolean add(E e)
	{
		if (this.checker.test(e))
		{
			return super.add(e);
		}
		else return false;
	}
	
	@Override
	public void add(int index, E element)
	{
		if (this.checker.test(element))
		{
			super.add(index, element);
		}
		else throw new IllegalArgumentException("The element can not match from checker, Checkeer: " + this.checker);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return super.addAll(new ArrayListArgument<E>(c.stream().filter(this.checker).toArray()));
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return super.addAll(index, new ArrayListArgument<E>(c.stream().filter(this.checker).toArray()));
	}
}