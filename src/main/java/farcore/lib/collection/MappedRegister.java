/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.collection;

import static farcore.util.L.indexOf;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import farcore.util.L;

/**
 * @author ueyudiud
 */
public class MappedRegister<T> implements IRegister<T>
{
	Collection<T> registeredElements;
	Set<String> registeredNameSet;
	
	Integer[] pointToID;
	Object[] pointToTargets;
	String[] pointToNames;
	int size = 0;
	int point = 0;
	int idFree = 0;
	
	public MappedRegister() { this(16); }
	public MappedRegister(int initialCapacity)
	{
		this.pointToID = new Integer[initialCapacity];
		this.pointToNames = new String[initialCapacity];
		this.pointToTargets = new Object[initialCapacity];
	}
	
	int nextFreeID()
	{
		while (L.contain(this.pointToID, this.idFree++));
		return this.idFree;
	}
	
	int ensureCapacity()
	{
		do
		{
			if(this.pointToID.length <= this.point)
			{
				int len = this.point + (this.point >> 1);
				this.pointToID = L.copyToLength(this.pointToID, len);
				this.pointToNames = L.copyToLength(this.pointToNames, len);
				this.pointToTargets = L.copyToLength(this.pointToTargets, len);
			}
		}
		while (this.pointToID[this.point ++] != null);
		return this.point - 1;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return targets().iterator();
	}
	
	@Override
	public int register(String name, T arg)
	{
		int id = nextFreeID();
		register(id, name, arg);
		return id;
	}
	
	@Override
	public void register(int id, String name, T arg)
	{
		if(contain(name))
			throw new IllegalArgumentException("The name is already registered.");
		++this.size;
		int index = ensureCapacity();
		this.pointToID[index] = id;
		this.pointToNames[index] = name;
		this.pointToTargets[index] = Objects.requireNonNull(arg);
	}
	
	@Override
	public boolean contain(int id)
	{
		return indexOf(this.pointToID, id) != -1;
	}
	
	@Override
	public boolean contain(String name)
	{
		return indexOf(this.pointToNames, name) != -1;
	}
	
	@Override
	public boolean contain(T arg)
	{
		return indexOf(this.pointToTargets, arg) != -1;
	}
	
	@Override
	public int id(T arg)
	{
		int id = indexOf(this.pointToTargets, arg);
		return id == -1 ? -1 : this.pointToID[id];
	}
	
	@Override
	public int id(String name)
	{
		int id = indexOf(this.pointToNames, name);
		return id == -1 ? -1 : this.pointToID[id];
	}
	
	@Override
	public String name(T arg)
	{
		int id = indexOf(this.pointToTargets, arg);
		return id == -1 ? null : this.pointToNames[id];
	}
	
	@Override
	public String name(int id0)
	{
		int id = indexOf(this.pointToID, id0);
		return id == -1 ? null : this.pointToNames[id];
	}
	
	@Override
	public T get(String name)
	{
		int id = indexOf(this.pointToNames, name);
		return (T) (id == -1 ? null : this.pointToTargets[id]);
	}
	
	@Override
	public T get(int id0)
	{
		int id = indexOf(this.pointToID, id0);
		return (T) (id == -1 ? null : this.pointToTargets[id]);
	}
	
	@Override
	public Collection<T> targets()
	{
		if(this.registeredElements == null)
		{
			this.registeredElements = new MappedRegisterTargetCollection();
		}
		return this.registeredElements;
	}
	
	@Override
	public Set<String> names()
	{
		if (this.registeredNameSet == null)
		{
			this.registeredNameSet = new MappedRegisterNameSet();
		}
		return this.registeredNameSet;
	}
	
	void remove(int id)
	{
		this.pointToID[id] = null;
		this.pointToNames[id] = null;
		this.pointToTargets[id] = null;
		this.point = id;
		this.size--;
	}
	
	@Override
	public T remove(String name)
	{
		int id = indexOf(this.pointToNames, name);
		if(id == -1) return null;
		Object obj = this.pointToTargets[id];
		remove(id);
		return (T) obj;
	}
	
	@Override
	public String remove(T arg)
	{
		int id = indexOf(this.pointToTargets, arg);
		if(id == -1) return null;
		String name = this.pointToNames[id];
		remove(id);
		return name;
	}
	
	@Override
	public int size()
	{
		return this.size;
	}
	
	class MappedRegisterNameSet extends AbstractSet<String>
	{
		@Override
		public boolean remove(Object o)
		{
			return o instanceof String ? MappedRegister.this.remove((String) o) != null : false;
		}
		
		@Override
		public Iterator<String> iterator()
		{
			return new Iterator<String>()
			{
				int point;
				
				@Override
				public boolean hasNext()
				{
					int point = this.point;
					while(point < MappedRegister.this.pointToNames.length && MappedRegister.this.pointToNames[point] == null)
					{
						++point;
					}
					return point < MappedRegister.this.pointToNames.length;
				}
				
				@Override
				public String next()
				{
					while(this.point < MappedRegister.this.pointToNames.length && MappedRegister.this.pointToNames[this.point] == null)
					{
						++this.point;
					}
					return MappedRegister.this.pointToNames[this.point];
				}
				
				@Override
				public void remove()
				{
					MappedRegister.this.remove(this.point);
				}
			};
		}
		
		@Override
		public int size()
		{
			return MappedRegister.this.size;
		}
	}
	
	class MappedRegisterTargetCollection extends AbstractCollection<T>
	{
		@Override
		public boolean remove(Object o)
		{
			try
			{
				return MappedRegister.this.remove((T) o) != null;
			}
			catch (ClassCastException exception)
			{
				return false;
			}
		}
		
		@Override
		public Iterator<T> iterator()
		{
			return new Iterator<T>()
			{
				int point;
				
				@Override
				public boolean hasNext()
				{
					int point = this.point;
					while(point < MappedRegister.this.pointToTargets.length && MappedRegister.this.pointToTargets[point] == null)
					{
						++point;
					}
					return point < MappedRegister.this.pointToTargets.length;
				}
				
				@Override
				public T next()
				{
					while(this.point < MappedRegister.this.pointToTargets.length && MappedRegister.this.pointToTargets[this.point] == null)
					{
						++this.point;
					}
					return (T) MappedRegister.this.pointToTargets[this.point];
				}
				
				@Override
				public void remove()
				{
					MappedRegister.this.remove(this.point);
				}
			};
		}
		
		@Override
		public int size()
		{
			return MappedRegister.this.size;
		}
	}
}