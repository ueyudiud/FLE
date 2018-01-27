/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import static nebula.base.Judgable.NOT_NULL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public class ArrayListConditional<E> extends ArrayList<E>
{
	private static final long serialVersionUID = -5656965564696895076L;
	
	public static <E> ArrayList<E> requireNonnull()
	{
		return new ArrayListConditional<>(NOT_NULL);
	}
	
	transient Predicate<? super E> checker;
	
	public ArrayListConditional(Predicate<? super E> checker)
	{
		this.checker = checker;
	}
	
	public ArrayListConditional(Predicate<? super E> checker, int initicalCapacity)
	{
		super(initicalCapacity);
		this.checker = checker;
	}
	
	@Override
	public boolean add(@Nullable E e)
	{
		if (this.checker.test(e))
		{
			return super.add(e);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void add(int index, @Nullable E element)
	{
		testElement(element);
		super.add(index, element);
	}
	
	@Override
	public E set(int index, E element)
	{
		testElement(element);
		return super.set(index, element);
	}
	
	@Override
	public boolean remove(Object o)
	{
		try
		{
			return this.checker.test((E) o) && super.remove(o);
		}
		catch (ClassCastException exception)
		{
			return false;
		}
	}
	
	void testElement(E element)
	{
		if (!this.checker.test(element))
			throw new IllegalArgumentException("The element can not match from checker, Checkeer: " + this.checker);
	}
	
	@Override
	public boolean addAll(@Nullable Collection<? extends E> c)
	{
		return c == null ? false : super.addAll(A.argument(c.stream().filter(this.checker).toArray()));
	}
	
	@Override
	public boolean addAll(int index, @Nullable Collection<? extends E> c)
	{
		return c == null ? false : super.addAll(index, new ArrayListArgument<E>(c.stream().filter(this.checker).toArray()));
	}
}