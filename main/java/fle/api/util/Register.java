package fle.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Register<T> implements Iterable<T>
{
	private int i = 0;
	private final List<Integer> idList = new ArrayList();
	private final List<String> names = new ArrayList();
	private final List<T> objs = new ArrayList();
	private final Map<Integer, String> nameMap = new HashMap();
	private final Map<String, T> objMap = new HashMap();
	private final Map<T, Integer> idMap = new HashMap();

	public void register(int i, T t, String name)
	{
		if(idMap.keySet().contains(t)) 
		{
			System.out.print("Fla API : Registry has already register " + t.toString() + ".");
			return;
		}
		if(names.contains(t)) 
		{
			System.out.print("Fla API : Registry has tag name " + name + ".");
			return;
		}
		names.add(name);
		objs.add(t);
		idList.add(i);
		nameMap.put(i, name);
		objMap.put(name, t);
		idMap.put(t, i);
	}
	
	public void register(T t, String name)
	{
		if(idMap.keySet().contains(t)) 
		{
			System.out.print("Fla API : Registry has already register " + t.toString() + ".");
			return;
		}
		if(names.contains(t)) 
		{
			System.out.print("Fla API : Registry has tag name " + name + ".");
			return;
		}
		while(idList.contains(i))
		{
			++i;
		}
		names.add(name);
		objs.add(t);
		idList.add(i);
		nameMap.put(i, name);
		objMap.put(name, t);
		idMap.put(t, i);
	}

	public int serial(T t)
	{
		return idMap.get(t);
	}
	public int serial(String name)
	{
		return serial(objMap.get(name));
	}
	
	public String name(T t)
	{
		return nameMap.get(serial(t));
	}
	public String name(int i)
	{
		return nameMap.get(i);
	}
	
	public T get(int i)
	{
		return get(nameMap.get(i));
	}
	public T get(String tag)
	{
		return objMap.get(tag);
	}
	
	@Override
	public Iterator<T> iterator() 
	{
		return new ArrayList<T>(objs).iterator();
	}

	public int size() 
	{
		return objs.size();
	}

	public T iterator(int i)
	{
		return objs.get(i);
	}

	public boolean remove(int i)
	{
		T target = get(i);
		String str = name(i);
		if(target == null || str == null) return false;
		names.remove(str);
		idList.remove(i);
		nameMap.remove(i);
		objMap.remove(str);
		idMap.remove(target);
		return objs.remove(target);
	}

	public boolean isEmpty() 
	{
		return objMap.size() == 0;
	}

	public boolean contain(String aName) 
	{
		return names.contains(aName);
	}
}