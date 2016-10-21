package farcore.lib.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ObjectArrays;

/**
 * A register instanceof of register.
 * This type is not thread safe.
 * @author ueyudiud
 *
 * @param <T> The register object type.
 */
public class Register<T> implements IRegister<T>
{
	private static final Comparator<Object[]> COMPARATOR = (Object[] e1, Object[] e2) ->
	{
		return ((String) e1[0]).compareTo((String) e2[0]);
	};
	
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
			throw new IllegalArgumentException("The id " + id + " has already registed with " + targets[id] + "!");
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
			throw new IllegalArgumentException("The name " + name + " has already registed!");
		else
		{
			size++;
			names[id] = name;
			targets[id] = arg;
		}
	}
	
	@Override
	public int size()
	{
		return size;
	}

	@Override
	public int id(T arg)
	{
		if(arg == null) return -1;
		int hash = arg.hashCode();
		Object object = null;
		for(int i = 0; i < targets.length; i++)
		{
			object = targets[i];
			if(object != null && hash == object.hashCode() && arg.equals(object)) return i;
		}
		return -1;
	}

	@Override
	public int id(String name)
	{
		if(name == null) return -1;
		String name1 = null;
		for(int i = 0; i < names.length; i++)
		{
			name1 = names[i];
			if(name1 != null && name1.equals(name)) return i;
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
	public T get(int id)
	{
		return id >= targets.length || id < 0 ? null : (T) targets[id];
	}
	
	@Override
	public Collection<T> targets()
	{
		return targetCol == null ? (targetCol = new RegisterTargetCollection()) : targetCol;
	}

	@Override
	public Set<String> names()
	{
		return nameSet == null ? (nameSet = new RegisterNameSet()) : nameSet;
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
			size--;
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
			size--;
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
					return true;
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
					return (T) targets[p++];
				++p;
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
			int i = p;
			while(i < names.length)
			{
				if(names[i] != null)
					return true;
				++i;
			}
			return false;
		}

		@Override
		public String next()
		{
			while(p < names.length)
			{
				if(names[p] != null)
					return names[p++];
				++p;
			}
			return null;
		}
	}
	
	private class RegisterNameSet implements Set<String>
	{
		@Override
		public int size()
		{
			return size;
		}

		@Override
		public boolean isEmpty()
		{
			return size == 0;
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
			Object[] result = new Object[size];
			int p = 0;
			int id = 0;
			while(p == size)
			{
				if(names[id] != null)
				{
					result[p++] = names[id];
				}
				++id;
			}
			return result;
		}

		@Override
		public <E> E[] toArray(E[] array)
		{
			if(array.length < size)
			{
				array = (E[]) Array.newInstance(array.getClass().getComponentType(), size);
			}
			int p = 0;
			int id = 0;
			while(p == size)
			{
				if(names[id] != null)
				{
					array[p++] = (E) names[id];
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
			return size;
		}
		
		@Override
		public boolean isEmpty()
		{
			return size == 0;
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
			List<T> list = new ArrayList(size);
			for (Object target : targets)
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
			List<E> list = new ArrayList(size);
			for (Object target : targets)
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
	@Deprecated
	public void arrange()
	{
		if(size == 0) return;
		int i = 0;
		List<Object[]> list = new ArrayList();
		for(; i < names.length; ++i)
		{
			if(names[i] != null)
			{
				list.add(new Object[]{names[i], targets[i]});
			}
		}
		Object[][] objects = list.toArray(new Object[0][]);
		Arrays.sort(objects, COMPARATOR);
		size = objects.length;
		names = new String[size];
		targets = new Object[size];
		for(i = 0; i < size; ++i)
		{
			names[i] = (String) objects[i][0];
			targets[i] = objects[i][1];
		}
	}
	
	@Override
	@Deprecated
	public void arrange(String... strings)
	{
		if(size == 0) return;
		List<String> l = Arrays.asList(strings);
		int id, i = 0;
		List<Object[]> list = new ArrayList();
		Object[][] objList2, objList = new Object[strings.length][];
		for(; i < names.length; ++i)
		{
			if(names[i] != null)
			{
				id = list.indexOf(names[i]);
				if(id == -1)
				{
					list.add(new Object[]{names[i], targets[i]});
				}
				else
				{
					objList[id] = new Object[]{names[i], targets[i]};
				}
			}
		}
		objList2 = list.toArray(new Object[0][]);
		Arrays.sort(objList2, COMPARATOR);
		objList = ObjectArrays.concat(objList, objList2, Object[].class);

		size = objList.length;
		names = new String[size];
		targets = new Object[size];
		for(i = 0; i < size; ++i)
		{
			if(objList[i] != null)
			{
				names[i] = (String) objList[i][0];
				targets[i] = objList[i][1];
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder key = new StringBuilder();
		for(String string : names)
		{
			if(string != null)
			{
				if(key.length() == 0)
				{
					key.append(",");
				}
				key.append(string);
			}
		}
		return "reg{" + key + "}";
	}
}