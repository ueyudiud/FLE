/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author ueyudiud
 */
public abstract class AbstractRegister<T> implements IRegister<T>
{
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
