package farcore.lib.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class Register<T> implements IRegister<T>
{
	private int point = 0;
	private float factor;
	
	private Object[] targets;
	private String[] names;

	public Register()
	{
		this(16);
	}
	public Register(int length)
	{
		this(length, 0.75F);
	}
	public Register(int length, float factor)
	{
		this.factor = factor;
		this.targets = new Object[length];
		this.names = new String[length];
	}

	private void extraList(int size)
	{
		if(size < names.length) return;
		String[] names1 = new String[size];
		Object[] targets1 = new Object[size];
		System.arraycopy(names, 0, names1, 0, names.length);
		System.arraycopy(targets, 0, targets1, 0, targets.length);
		names = names1;
		targets = targets1;
	}
	
	private int freePoint()
	{
		do 
		{
			if(point >= names.length)
			{
				extraList((int) (names.length * (1 + factor)));
				continue;
			}
			else if(names[point] == null)
			{
				break;
			}
			point++;
		} 
		while (true);
		return point;
	}
	
	private void freePoint(int id)
	{
		if(contain(id))
		{
			throw new IllegalArgumentException("The id " + id + " has already registed!");
		}
		if(id >= names.length)
		{
			extraList((int) (id * (1 + factor)));
		}
	}
	
	private int[] freePoints(int length)
	{
		int l = 0;
		int[] ids = new int[length];
		do 
		{
			if(point >= names.length)
			{
				extraList((int) (length + names.length * (1 + factor)));
				continue;
			}
			else if(names[point] == null)
			{
				ids[l] = point;
				l++;
			}
			point++;
		} 
		while (l < length);
		return ids;
	}
	
	@Override
	public int register(String name, T arg)
	{
		int point = freePoint();
		reg(point, name, arg);
		return point;
	}

	@Override
	public void register(int id, String name, T arg)
	{
		freePoint(id);
		reg(id, name, arg);
	}
	
	private void reg(int id, String name, T arg)
	{
		if(contain(name))
		{
			throw new IllegalArgumentException("The name " + name + " has already registed!");
		}
		names[id] = name;
		targets[id] = arg;
	}

	@Override
	public int id(T arg)
	{
		if(arg == null) return -1;
		int hash = arg.hashCode();
		Object object = null;
		for(int i = 0;
				i < targets.length; 
				object = targets[i++])
		{
			if(object == null) continue;
			if(hash == object.hashCode() &&
					arg.equals(object))
			{
				return i - 1;
			}
		}
		return -1;
	}

	@Override
	public int id(String name)
	{
		if(name == null) return -1;
		String name1 = null;
		for(int i = 0;
				i < names.length; 
				name1 = names[i++])
		{
			if(name == null) continue;
			if(name.equals(name1))
			{
				return i - 1;
			}
		}
		return -1;
	}

	@Override
	public String name(T arg)
	{
		int id = id(arg);
		return id == -1 ? null : names[id];
	}

	@Override
	public String name(int id)
	{
		return id >= names.length || id < 0 ? null : names[id];
	}

	@Override
	public T get(String name)
	{
		int id = id(name);
		return id == -1 ? null : (T) targets[id];
	}
	
	@Override
	public T get(String name, T def)
	{
		T ret = get(name);
		return ret == null ? def : ret;
	}

	@Override
	public T get(int id)
	{
		return id >= targets.length || id < 0 ? null : (T) targets[id];
	}

	@Override
	public T get(int id, T def)
	{
		T ret = get(id);
		return ret == null ? def : ret;
	}
	
	@Override
	public List<T> targets()
	{
		return (List<T>) Arrays.asList(targets);
	}

	@Override
	public Set<String> names()
	{
		return ImmutableSet.copyOf(names);
	}

	@Override
	public boolean contain(String name)
	{
		return id(name) != -1;
	}

	@Override
	public boolean contain(int id)
	{
		if(id < 0) throw new IllegalArgumentException("The id " + id + " must be an non-negtive number!");
		return id >= names.length ? false : names[id] != null;
	}

	@Override
	public boolean contain(T arg)
	{
		return id(arg) != -1;
	}

	@Override
	public T remove(String name)
	{
		int id = id(name);
		if(id != -1)
		{
			this.point = Math.min(point, id);
			names[id] = null;
			T ret = (T) targets[id];
			targets[id] = null;
			return ret;
		}
		return null;
	}

	@Override
	public String remove(T arg)
	{
		int id = id(arg);
		if(id != -1)
		{
			this.point = Math.min(point, id);
			targets[id] = null;
			String ret = names[id];
			names[id] = null;
			return ret;
		}
		return null;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new RegisterItr();
	}
	
	private class RegisterItr implements Iterator<T>
	{
		int p = 0;
		
		@Override
		public boolean hasNext()
		{
			int i = p;
			while(i < targets.length)
			{
				if(targets[i] != null)
				{
					return true;
				}
				++i;
			}
			return false;
		}

		@Override
		public T next()
		{
			while(p < targets.length)
			{
				if(targets[p] != null)
				{
					return (T) targets[p++];
				}
				++p;
			}
			return null;
		}
	}
}