/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import nebula.common.util.A;

/**
 * A register instance of IRegister.
 * This type is not thread safe.
 * @author ueyudiud
 * @param <T> the register object type.
 */
public class Register<T> implements IRegister<T>
{
	private int point = 0;
	private int size = 0;
	private float factor;
	
	private Object[] targets;
	private String[] names;
	
	private Collection<T> targetCol;
	private Set<String> nameSet;
	
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
		if(size < this.names.length) return;
		this.names = A.copyToLength(this.names, size);
		this.targets = A.copyToLength(this.targets, size);
	}
	
	private int freePoint()
	{
		do
		{
			if(this.point >= this.names.length)
			{
				extraList((int) (this.names.length * (1 + this.factor)));
				continue;
			}
			else if(this.names[this.point] == null)
			{
				break;
			}
			this.point++;
		}
		while (true);
		return this.point;
	}
	
	private void freePoint(int id)
	{
		if (contain(id))
			throw new IllegalArgumentException("The id " + id + " has already registed with " + this.targets[id] + "!");
		if (id >= this.names.length)
		{
			extraList((int) (id * (1 + this.factor) + 1));
		}
	}
	
	//	private int[] freePoints(int length)
	//	{
	//		int l = 0;
	//		int[] ids = new int[length];
	//		do
	//		{
	//			if(this.point >= this.names.length)
	//			{
	//				extraList((int) (length + this.names.length * (1 + this.factor)));
	//				continue;
	//			}
	//			else if(this.names[this.point] == null)
	//			{
	//				ids[l] = this.point;
	//				l++;
	//			}
	//			this.point++;
	//		}
	//		while (l < length);
	//		return ids;
	//	}
	
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
		if (contain(name))
			throw new IllegalArgumentException("The name " + name + " has already registed!");
		else
		{
			this.size++;
			this.names[id] = name;
			this.targets[id] = arg;
		}
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
	
	@Override
	public int id(T arg)
	{
		if(arg == null) return -1;
		int hash = arg.hashCode();
		Object object = null;
		for(int i = 0; i < this.targets.length; i++)
		{
			object = this.targets[i];
			if(object != null && hash == object.hashCode() && arg.equals(object)) return i;
		}
		return -1;
	}
	
	@Override
	public int id(String name)
	{
		if(name == null) return -1;
		String name1 = null;
		for(int i = 0; i < this.names.length; i++)
		{
			name1 = this.names[i];
			if(name1 != null && name1.equals(name)) return i;
		}
		return -1;
	}
	
	@Override
	public String name(T arg)
	{
		int id = id(arg);
		return id == -1 ? null : this.names[id];
	}
	
	@Override
	public String name(int id)
	{
		return id >= this.names.length || id < 0 ? null : this.names[id];
	}
	
	@Override
	public T get(String name)
	{
		int id = id(name);
		return id == -1 ? null : (T) this.targets[id];
	}
	
	@Override
	public T get(int id)
	{
		return id >= this.targets.length || id < 0 ? null : (T) this.targets[id];
	}
	
	@Override
	public Collection<T> targets()
	{
		return this.targetCol == null ? (this.targetCol = new RegisterTargetCollection()) : this.targetCol;
	}
	
	@Override
	public Set<String> names()
	{
		return this.nameSet == null ? (this.nameSet = new RegisterNameSet()) : this.nameSet;
	}
	
	@Override
	public boolean contain(String name)
	{
		return id(name) != -1;
	}
	
	@Override
	public boolean contain(int id)
	{
		if(id < 0)
			throw new IllegalArgumentException("The id " + id + " must be an non-negtive number!");
		return id >= this.names.length ? false : this.names[id] != null;
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
			this.point = Math.min(this.point, id);
			this.names[id] = null;
			T ret = (T) this.targets[id];
			this.targets[id] = null;
			this.size--;
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
			this.point = Math.min(this.point, id);
			this.targets[id] = null;
			String ret = this.names[id];
			this.names[id] = null;
			this.size--;
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
			int i = this.p;
			while(i < Register.this.targets.length)
			{
				if(Register.this.targets[i] != null)
					return true;
				++i;
			}
			return false;
		}
		
		@Override
		public T next()
		{
			while(this.p < Register.this.targets.length)
			{
				if(Register.this.targets[this.p] != null)
					return (T) Register.this.targets[this.p++];
				++this.p;
			}
			return null;
		}
	}
	
	private class RegisterNameItr implements Iterator<String>
	{
		int p = 0;
		
		@Override
		public boolean hasNext()
		{
			int i = this.p;
			while(i < Register.this.names.length)
			{
				if(Register.this.names[i] != null)
					return true;
				++i;
			}
			return false;
		}
		
		@Override
		public String next()
		{
			while(this.p < Register.this.names.length)
			{
				if(Register.this.names[this.p] != null)
					return Register.this.names[this.p++];
				++this.p;
			}
			return null;
		}
	}
	
	private class RegisterNameSet implements Set<String>
	{
		@Override
		public int size()
		{
			return Register.this.size;
		}
		
		@Override
		public boolean isEmpty()
		{
			return Register.this.size == 0;
		}
		
		@Override
		public boolean contains(Object arg)
		{
			return arg instanceof String && Register.this.contain((String) arg);
		}
		
		@Override
		public Iterator<String> iterator()
		{
			return new RegisterNameItr();
		}
		
		@Override
		public Object[] toArray()
		{
			Object[] result = new Object[Register.this.size];
			int p = 0;
			int id = 0;
			while(p == Register.this.size)
			{
				if(Register.this.names[id] != null)
				{
					result[p++] = Register.this.names[id];
				}
				++id;
			}
			return result;
		}
		
		@Override
		public <E> E[] toArray(E[] array)
		{
			if(array.length < Register.this.size)
			{
				array = (E[]) Array.newInstance(array.getClass().getComponentType(), Register.this.size);
			}
			int p = 0;
			int id = 0;
			while(p == Register.this.size)
			{
				if(Register.this.names[id] != null)
				{
					array[p++] = (E) Register.this.names[id];
				}
				++id;
			}
			return array;
		}
		
		@Override
		public boolean add(String e)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean remove(Object object)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsAll(Collection<?> collection)
		{
			for(Object object : collection)
			{
				if(!(object instanceof String) || !contain((String) object)) return false;
			}
			return true;
		}
		
		@Override
		public boolean addAll(Collection<? extends String> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean removeAll(Collection<?> collection)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void clear()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	private class RegisterTargetCollection implements Collection<T>
	{
		@Override
		public int size()
		{
			return Register.this.size;
		}
		
		@Override
		public boolean isEmpty()
		{
			return Register.this.size == 0;
		}
		
		@Override
		public boolean contains(Object o)
		{
			try
			{
				return contain((T) o);
			}
			catch (ClassCastException exception)
			{
				return false;
			}
		}
		
		@Override
		public Iterator<T> iterator()
		{
			return new RegisterItr();
		}
		
		@Override
		public Object[] toArray()
		{
			List<T> list = new ArrayList(Register.this.size);
			for (Object target : Register.this.targets)
			{
				if(target != null)
				{
					list.add((T) target);
				}
			}
			return list.toArray();
		}
		
		@Override
		public <E> E[] toArray(E[] a)
		{
			List<E> list = new ArrayList(Register.this.size);
			for (Object target : Register.this.targets)
			{
				if(target != null)
				{
					list.add((E) target);
				}
			}
			return list.toArray(a);
		}
		
		@Override
		public boolean add(T e)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsAll(Collection<?> collection)
		{
			for(Object object : collection)
			{
				if(!contains(object))
					return false;
			}
			return true;
		}
		
		@Override
		public boolean addAll(Collection<? extends T> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void clear()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public int hashCode()
	{
		int hash = 0;
		for (int i = 0; i < this.names.length; ++i)
		{
			if (this.names[i] != null)
				hash += i ^ this.names[i].hashCode() ^ Objects.hashCode(this.targets[i]);
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (!(obj instanceof IRegister<?>)) return false;
		
		IRegister<?> register = (IRegister<?>) obj;
		if (register.size() != this.size) return false;
		
		try
		{
			for (int i = 0; i < this.names.length; ++i)
			{
				if (this.names[i] != null)
				{
					if (!this.names[i].equals(register.name(i)) || !Objects.equals(this.targets[i], register.get(i)))
						return false;
				}
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
		StringBuilder key = new StringBuilder();
		for(String string : this.names)
		{
			if(string != null)
			{
				if(key.length() != 0)
				{
					key.append(",");
				}
				key.append(string);
			}
		}
		return "{" + key + "}";
	}
}