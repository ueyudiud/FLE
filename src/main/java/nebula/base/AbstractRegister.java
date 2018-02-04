/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;

import nebula.base.register.IRegister;

/**
 * @author ueyudiud
 */
public abstract class AbstractRegister<T> implements IRegister<T>
{
	private static final int characteristics = Spliterator.NONNULL | Spliterator.DISTINCT;
	
	@Override
	public Stream<T> stream()
	{
		return StreamSupport.stream(Spliterators.spliterator(iterator(), size(), characteristics), false);
	}
	
	@Override
	public Stream<Entry<String, T>> entryStream()
	{
		return StreamSupport.stream(Spliterators.spliterator(Iterators.transform(iterator(), t->new Ety<>(name(t), t)), size(), characteristics), false);
	}
	
	@Override
	public int hashCode()
	{
		int hash = 0;
		for (String name : names())
		{
			int i = id(name);
			T t = get(name);
			hash += i ^ name.hashCode() ^ Objects.hashCode(t);
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (!(obj instanceof IRegister<?>)) return false;
		
		IRegister<?> register = (IRegister<?>) obj;
		if (register.size() != size()) return false;
		
		try
		{
			for (String name : names())
			{
				int i = id(name);
				if (!name.equals(register.name(i)) ||
						!Objects.equals(get(i), register.get(i)))
					return false;
			}
			return true;
		}
		catch (ClassCastException exception)
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		if (size() == 0)
			return "{}";
		else
		{
			StringBuilder values = new StringBuilder("{");
			Iterator<String> itr = names().iterator();
			String key = itr.next();
			values.append(key).append('=').append(get(key));
			while (itr.hasNext())
			{
				values.append(',').append(' ');
				key = itr.next();
				values.append(key).append('=').append(get(key));
			}
			return values.append('}').toString();
		}
	}
}
