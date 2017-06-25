/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base;

import static nebula.base.Judgable.NOT_NULL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public class ArrayListAddWithCheck<E> extends ArrayList<E>
{
	private static final long serialVersionUID = -5656965564696895076L;
	
	public static <E> List<E> argument(Object[] array)
	{
		return new ArrayListArgument(array);
	}
	
	public static <E> ArrayList<E> requireNonnull()
	{
		return new ArrayListAddWithCheck<>(NOT_NULL);
	}
	
	transient Predicate<? super E> checker;
	
	public ArrayListAddWithCheck(Predicate<? super E> checker)
	{
		this.checker = checker;
	}
	public ArrayListAddWithCheck(Predicate<? super E> checker, int initicalCapacity)
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
		else return false;
	}
	
	@Override
	public void add(int index, @Nullable E element)
	{
		if (this.checker.test(element))
		{
			super.add(index, element);
		}
		else throw new IllegalArgumentException("The element can not match from checker, Checkeer: " + this.checker);
	}
	
	@Override
	public boolean addAll(@Nullable Collection<? extends E> c)
	{
		return c == null ? false : super.addAll(argument(c.stream().filter(this.checker).toArray()));
	}
	
	@Override
	public boolean addAll(int index, @Nullable Collection<? extends E> c)
	{
		return c == null ? false : super.addAll(index, new ArrayListArgument<E>(c.stream().filter(this.checker).toArray()));
	}
}