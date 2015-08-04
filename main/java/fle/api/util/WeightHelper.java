package fle.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeightHelper<T>
{
	private static final Random rand = new Random();
	private Stack<T>[] sts;
	private Object[] ts;
	
	public WeightHelper(T[]...aTs) 
	{
		int i;
		int size = 0;
		for(i = 0; i < aTs.length; ++i)
			size += aTs[i].length;
		ts = new Object[size];
		int r = 0;
		Map<T, Integer> map = new HashMap();
		for(i = 0; i < aTs.length; ++i)
			for(int j = 0; j < aTs[i].length; ++j)
			{
				add(map, aTs[i][j]);
				ts[r] = aTs[i][j];
				++r;
			}
		sts = new Stack[map.size()];
		i = 0;
		for(T e : map.keySet())
		{
			sts[i] = new Stack(e, map.get(e));
			++i;
		}
	}
	public WeightHelper(T...aTs)
	{
		ts = aTs;
		int i;
		Map<T, Integer> map = new HashMap();
		for(i = 0; i < aTs.length; ++i)
			add(map, aTs[i]);
		sts = new Stack[map.size()];
		i = 0;
		for(T e : map.keySet())
		{
			sts[i] = new Stack(e, map.get(e));
			++i;
		}
	}
	public WeightHelper(Map<T, Integer> aMap)
	{
		sts = new Stack[aMap.size()];
		int i = 0;
		int size = 0;
		for(T e : aMap.keySet())
		{
			sts[i] = new Stack(e, aMap.get(e));
			size += aMap.get(e);
			++i;
		}
		ts = new Object[size];
		int k = 0;
		for(i = 0; i < sts.length; ++i)
			for(int j = 0; j < sts[i].size; ++j)
			{
				ts[k] = sts[i].target;
				++k;
			}
	}
	
	private static <T> void add(Map<T, Integer> map, T e)
	{
		if(map.containsKey(e))
		{
			int a = map.get(e) + 1;
			map.put(e, a);
		}
		else
		{
			map.put(e, 1);
		}
	}
	
	public double getContain(T e)
	{
		for(int i = 0; i < sts.length; ++i)
		{
			if(sts[i].isEqul(e))
			{
				return (double) sts[i].size / (double) ts.length;
			}
		}
		return 0D;
	}
	
	public T[] getArray(T[] ts)
	{
		List<T> list = new ArrayList();
		for(int i = 0; i < sts.length; ++i)
		{
			int a = (int) (ts.length * getContain(sts[i].target));
			for(int j = 0; j < a; ++j)
				list.add(sts[i].target);
		}
		if(ts.length != list.size())
		{
			while(list.size() != ts.length)
			{
				list.add(randomGet());
			}
		}
		return list.toArray(ts);
	}
	
	public T randomGet(Random rand)
	{
		return (T) ts[rand.nextInt(ts.length)];
	}
	
	public T randomGet()
	{
		return (T) ts[rand.nextInt(ts.length)];
	}
	
	public int allWeight()
	{
		return ts.length;
	}
	
	public Object[] getList() 
	{
		return ts.clone();
	}
	
	@Override
	public String toString()
	{
		return sts.toString();
	}
	
	public static class Stack<T>
	{
		private int size;
		private T target;
		
		public Stack(T t){this(t, 1);}
		public Stack(T t, int s) 
		{
			target = t;
			size = s;
		}
		
		public boolean isEqul(Object obj)
		{
			return target == null && obj == null ? true : target == null || obj == null ? false : target.equals(obj);
		}
		
		public void addStackIn(int aS)
		{
			size += aS;
		}
		
		@Override
		public String toString()
		{
			return target.toString() + "x" + size;
		}
	}
}