/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import nebula.base.SortedRegister.Delegate;

/**
 * TODO
 * @author ueyudiud
 */
public class HashedIdAllocatedRegister<T> implements IRegister<T>
{
	private int size;
	private Delegate<T>[] nameSorted;
	private Delegate<T>[] idSorted;
	
	@Override
	public Iterator<T> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int register(String name, T arg)
	{
		register(-1, name, arg);
		return -1;
	}
	
	@Override
	@Deprecated
	public void register(int id, String name, T arg)
	{
		
	}
	
	@Override
	public int id(T arg)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int id(String name)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String name(T arg)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String name(int id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public T get(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public T get(int id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<T> targets()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<String> names()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public T remove(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String remove(T arg)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
