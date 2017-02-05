/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

import java.util.ArrayList;
import java.util.Collection;

import nebula.common.util.IDataChecker;

/**
 * @author ueyudiud
 */
public class ArrayListAddWithCheck<E> extends ArrayList<E>
{
	public static <E> ArrayList<E> requireNonnull()
	{
		return new ArrayListAddWithCheck<>(IDataChecker.NOT_NULL);
	}
	
	IDataChecker<E> checker;
	
	public ArrayListAddWithCheck(IDataChecker<E> checker)
	{
		this.checker = checker;
	}
	public ArrayListAddWithCheck(IDataChecker<E> checker, int initicalCapacity)
	{
		super(initicalCapacity);
		this.checker = checker;
	}
	
	@Override
	public boolean add(E e)
	{
		if (this.checker.isTrue(e))
		{
			return super.add(e);
		}
		else return false;
	}
	
	@Override
	public void add(int index, E element)
	{
		if (this.checker.isTrue(element))
		{
			super.add(index, element);
		}
		else throw new IllegalArgumentException("The element can not match from checker, Checkeer: " + this.checker);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException();
	}
}