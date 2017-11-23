/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

import nebula.common.util.L;

/**
 * The sorted register, used binary searching to get name and id.
 * 
 * @author ueyudiud
 * @param <T> the register object type.
 */
public class SortedRegister<T> extends AbstractRegister<T>
{
	protected static class Delegate<T>
	{
		int		id;
		String	name;
		T		ref;
		
		Delegate(int id, String name, T ref)
		{
			this.id = id;
			this.name = name;
			this.ref = ref;
		}
		
		public T get()
		{
			return this.ref;
		}
		
		public int compareID(Delegate<?> delegate)
		{
			return Integer.compare(this.id, delegate.id);
		}
		
		public int compareName(Delegate<?> delegate)
		{
			return this.name.compareTo(delegate.name);
		}
	}
	
	protected final Collection<Delegate<T>>	resource;
	protected final ToIntFunction<T>		idFunc;
	
	protected boolean sorted = false;
	
	protected Map<T, Integer>	map;
	protected Delegate<T>[]		sortedIDs;
	protected Delegate<T>[]		sortedNames;
	
	protected Collection<T>		targets;
	protected Set<String>		names;
	
	public SortedRegister()
	{
		this(16, null);
	}
	
	public SortedRegister(int initialCapacity, @Nullable ToIntFunction<T> function)
	{
		this.resource = new ArrayList<>(initialCapacity);
		this.idFunc = function;
	}
	
	public SortedRegister(Collection<?> collection, @Nullable ToIntFunction<T> function)
	{
		if (!collection.isEmpty()) throw new IllegalArgumentException("The initalized collection should be empty!");
		this.resource = (Collection<Delegate<T>>) collection;
		this.idFunc = function;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		build();
		return new Iterator<T>()
		{
			int id = 0;
			
			@Override
			public boolean hasNext()
			{
				return this.id < SortedRegister.this.sortedIDs.length;
			}
			
			@Override
			public T next()
			{
				return SortedRegister.this.sortedIDs[this.id++].ref;
			}
		};
	}
	
	private int free;
	
	protected int freeID()
	{
		while (of(this.free++) != null);
		return this.free - 1;
	}
	
	protected void build()
	{
		if (!this.sorted)
		{
			if (this.resource instanceof ArrayList)
			{
				((ArrayList<?>) this.resource).trimToSize();
			}
			Arrays.sort(this.sortedIDs = this.resource.toArray(new Delegate[this.resource.size()]), Delegate::compareID);
			Arrays.sort(this.sortedNames = this.resource.toArray(new Delegate[this.resource.size()]), Delegate::compareName);
			if (this.idFunc == null)
			{
				this.map = new HashMap<>();
				for (Delegate<T> delegate : this.resource)
				{
					this.map.put(delegate.ref, delegate.id);
				}
			}
			this.sorted = true;
		}
	}
	
	private void markChaned()
	{
		if (this.sorted)
		{
			this.sorted = false;
			this.sortedIDs = null;
			this.sortedNames = null;
		}
	}
	
	private int checkIDNotExist(int id)
	{
		if (contain(id)) throw new IllegalArgumentException("The id " + id + " has already registered!");
		return id;
	}
	
	@Override
	public int register(String name, T arg)
	{
		int id = this.idFunc != null ? checkIDNotExist(this.idFunc.applyAsInt(arg)) : freeID();
		reg(id, name, arg);
		return id;
	}
	
	@Override
	public void register(int id, String name, T arg)
	{
		reg(checkIDNotExist(id), name, arg);
	}
	
	protected void reg(int id, String name, T arg)
	{
		if (this.sorted ? of(name) != null : L.contain(this.resource, d->name.equals(d.name)))
			throw new IllegalArgumentException("The name " + name + " has already registered!");
		this.resource.add(new Delegate<>(id, name, Objects.requireNonNull(arg)));
		markChaned();
	}
	
	private Delegate<T> of(int id)
	{
		build();
		int low = 0, high = this.sortedIDs.length - 1;
		while (low <= high)
		{
			int mid = (low + high >>> 1);
			switch (Integer.compare(id, this.sortedIDs[mid].id))
			{
			case -1:
				low = mid + 1;
				continue;
			case 1:
				high = mid - 1;
				continue;
			default:
				return this.sortedIDs[mid];
			}
		}
		return null;
	}
	
	private Delegate<T> of(String name)
	{
		build();
		int low = 0, high = this.sortedNames.length - 1;
		while (low <= high)
		{
			int mid = (low + high >>> 1);
			int cmp = name.compareTo(this.sortedNames[mid].name);
			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return this.sortedIDs[mid];
		}
		return null;
	}
	
	@Override
	public int id(T arg)
	{
		if (this.idFunc != null)
			return this.idFunc.applyAsInt(arg);
		else
		{
			build();
			return this.map.getOrDefault(arg, -1);
		}
	}
	
	@Override
	public int id(String name)
	{
		Delegate<T> delegate;
		return (delegate = of(name)) != null ? delegate.id : -1;
	}
	
	@Override
	public String name(T arg)
	{
		int id = id(arg);
		return id < 0 ? null : name(id);
	}
	
	@Override
	public String name(int id)
	{
		Delegate<T> delegate;
		return (delegate = of(id)) != null ? delegate.name : null;
	}
	
	@Override
	public T get(String name)
	{
		return get(name, null);
	}
	
	@Override
	public T get(int id)
	{
		return get(id, null);
	}
	
	@Override
	public T get(String name, T def)
	{
		Delegate<T> delegate = of(name);
		return delegate != null ? delegate.ref : def;
	}
	
	@Override
	public T get(int id, T def)
	{
		Delegate<T> delegate = of(id);
		return delegate != null ? delegate.ref : def;
	}
	
	@Override
	public Collection<T> targets()
	{
		if (this.targets == null)
		{
			this.targets = new SortedRegisterCollection();
		}
		return this.targets;
	}
	
	private class SortedRegisterCollection extends AbstractCollection<T>
	{
		@Override
		public Iterator<T> iterator()
		{
			return SortedRegister.this.iterator();
		}
		
		@Override
		public boolean contains(Object o)
		{
			try
			{
				return SortedRegister.this.contain((T) o);
			}
			catch (ClassCastException | NullPointerException exception)
			{
				return false;
			}
		}
		
		@Override
		public int size()
		{
			return SortedRegister.this.size();
		}
	}
	
	@Override
	public Set<String> names()
	{
		if (this.names == null)
		{
			this.names = new SortedRegisterNamedSet();
		}
		return this.names;
	}
	
	private class SortedRegisterNamedSet extends AbstractSet<String>
	{
		@Override
		public Iterator<String> iterator()
		{
			build();
			return new Iterator<String>()
			{
				int id = 0;
				
				@Override
				public boolean hasNext()
				{
					return this.id < SortedRegister.this.sortedIDs.length;
				}
				
				@Override
				public String next()
				{
					return SortedRegister.this.sortedIDs[this.id++].name;
				}
			};
		}
		
		@Override
		public boolean contains(Object o)
		{
			try
			{
				return SortedRegister.this.contain((String) o);
			}
			catch (ClassCastException | NullPointerException exception)
			{
				return false;
			}
		}
		
		@Override
		public int size()
		{
			return SortedRegister.this.size();
		}
	}
	
	@Override
	public T remove(String name)
	{
		if (this.sorted)
		{
			Delegate<T> delegate = of(name);
			if (delegate != null)
			{
				this.resource.remove(delegate);
				this.free = delegate.id;
				markChaned();
				return delegate.ref;
			}
		}
		else
		{
			Iterator<Delegate<T>> itr = this.resource.iterator();
			while (itr.hasNext())
			{
				Delegate<T> delegate = itr.next();
				if (name.equals(delegate.name))
				{
					itr.remove();
					this.free = delegate.id;
					return delegate.ref;
				}
			}
		}
		return null;
	}
	
	@Override
	public String remove(T arg)
	{
		Iterator<Delegate<T>> itr = this.resource.iterator();
		while (itr.hasNext())
		{
			Delegate<T> delegate = itr.next();
			if (arg.equals(delegate.ref))
			{
				itr.remove();
				this.free = delegate.id;
				markChaned();
				return delegate.name;
			}
		}
		return null;
	}
	
	@Override
	public boolean contain(int id)
	{
		return of(id) != null;
	}
	
	@Override
	public boolean contain(String name)
	{
		return of(name) != null;
	}
	
	@Override
	public int size()
	{
		return this.resource.size();
	}
}
