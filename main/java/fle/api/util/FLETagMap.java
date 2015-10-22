package fle.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fle.api.util.IDataChecker.And;
import fle.api.util.IDataChecker.Or;

public class FLETagMap<T>
{
	private int c;
	private final float a;
	private Object[] list;
	
	public FLETagMap()
	{
		this(16);
	}
	public FLETagMap(int initCapcity)
	{
		this(initCapcity, 0.75F);
	}
	public FLETagMap(int initCapcity, float speed)
	{
		if(speed < 0) throw new RuntimeException("FLE API: Increase speed can't be a negitive number!");
		a = 1F + speed;
		list = new Object[initCapcity];
		c = 0;
	}

	private void addListCapcity(int lengthNeed)
	{
		if(lengthNeed + c < list.length) return;
		int k = list.length;
		while(lengthNeed + c >= k)
		{
			k *= a;
		}
		Object[] l = new Object[k];
		System.arraycopy(list, 0, l, 0, list.length);
		list = l;
	}
	
	private void putNext(T obj)
	{
		if(obj == null) return;
		while(list[c] != null)
		{
			++c;
		}
		list[c] = obj;
	}
	
	public List<T> get(IDataChecker<T> checker)
	{
		List<T> ret = new ArrayList();
		for(Object rawTarget : list)
		{
			T target = (T) rawTarget;
			if(checker.isTrue(target))
				ret.add(target);
		}
		return ret;
	}
	
	public List<T> get(IDataChecker<T>...checker)
	{
		return get(new And(checker));
	}
	
	public List<T> getWith(IDataChecker<T>...checker)
	{
		return get(new Or(checker));
	}
	
	public List<T> get()
	{
		List<T> list = new ArrayList();
		for(Object obj : this.list) list.add((T) obj);
		return list;
	}
	
	public void put(T...ts)
	{
		addListCapcity(ts.length);
		for(T t : ts)
		{
			putNext(t);
		}
	}
	
	@Override
	public String toString()
	{
		String str = "[";
		int i = 0;
		for(Object obj : list)
		{
			str += i == list.length - 1 ? obj.toString() : obj.toString() + ", ";
			++i;
		}
		str += "]";
		return str;
	}
}