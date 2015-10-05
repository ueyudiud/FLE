package fle.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class WeightHelper<T>
{
	public static <T> Stack<T>[] asArray(Map<T, Integer> aMap)
	{
		Stack<T>[] sts = new Stack[aMap.size()];
		int i = 0;
		for(Entry<T, Integer> e : aMap.entrySet())
		{
			sts[i] = new Stack(e.getKey(), e.getValue());
			++i;
		}
		return sts;
	}
	public static <T> void add(Map<T, Integer> map, T e)
	{
		if(e != null)
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
	}
	public static <T> void add(Map<T, Integer> map, Stack<T> e)
	{
		if(e.target != null && e.size > 0)
		{
			if(map.containsKey(e.target))
			{
				int a = map.get(e.target) + e.size;
				map.put(e.target, a);
			}
			else
			{
				map.put(e.target, e.size);
			}
		}
	}
	public static <T> void add(Map<T, Integer> map, Stack<T> e, int size)
	{
		if(map.containsKey(e.target))
		{
			int a = map.get(e.target) + e.size * size;
			map.put(e.target, a);
		}
		else
		{
			map.put(e.target, e.size);
		}
	}
	public static <T> void add(Map<T, Integer> map, T...e)
	{
		for(T t : e) add(map, t);
	}
	public static <T> void add(Map<T, Integer> map, Stack<T>...e)
	{
		for(Stack<T> ts : e) add(map, ts);
	}
	public static <T> void add(Map<T, Integer> map, int size, Stack<T>...e)
	{
		for(Stack<T> ts : e) add(map, ts, size);
	}
	public static <T> void add(Map<T, Integer> aMap,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) add(aMap, new Stack(e.getKey(), e.getValue()));
	}
	public static <T> void add(Map<T, Integer> aMap, int size,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) add(aMap, new Stack(e.getKey(), e.getValue() * size));
	}
	
	private static final Random rand = new Random();
	private int length;
	private Stack<T>[] sts;
	
	public WeightHelper(T[]...aTs) 
	{
		int i;
		int size = 0;
		for(i = 0; i < aTs.length; ++i)
			size += aTs[i].length;
		int r = 0;
		Map<T, Integer> map = new HashMap();
		for(i = 0; i < aTs.length; ++i)
			for(int j = 0; j < aTs[i].length; ++j)
			{
				add(map, aTs[i][j]);
				++r;
			}
		sts = new Stack[map.size()];
		i = 0;
		for(T e : map.keySet())
		{
			sts[i] = new Stack(e, map.get(e));
			++i;
		}
		length = r;
	}
	public WeightHelper(T...aTs)
	{
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
		length = i;
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
		length = size;
	}
	public WeightHelper(Stack<T>...aStacks)
	{
		sts = aStacks.clone();
		for(Stack e : aStacks)
		{
			length += e.size;
		}
	}

	public double getContain(T e)
	{
		for(int i = 0; i < sts.length; ++i)
		{
			if(sts[i].isEqul(e))
			{
				return (double) sts[i].size / (double) length;
			}
		}
		return 0D;
	}

	public Map<T, Double> getContains()
	{
		Map<T, Double> ret = new HashMap();
		for(Stack<T> t : sts)
		{
			ret.put(t.target, (double) t.size / (double) length);
		}
		return ret;
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
		int i = rand.nextInt(length);
		int cache = 0;
		for(Stack<T> stack : sts)
		{
			if(cache + stack.size > i) return stack.target;
			cache += stack.size;
		}
		return (T) null;
	}
	
	public T randomGet()
	{
		return randomGet(rand);
	}
	
	public Stack<T>[] getList()
	{
		return sts;
	}
	
	public int allWeight()
	{
		return length;
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
			return target == null && obj == null ? true : (target == null || obj == null ? false : target.equals(obj));
		}
		
		public void addStackIn(int aS)
		{
			size += aS;
		}
		
		public void minusStackOut(int aS)
		{
			size -= aS;
		}
		
		@Override
		public String toString()
		{
			return target.toString() + "x" + size;
		}
		
		public T getObj()
		{
			return target;
		}
		
		public int getSize()
		{
			return size;
		}
		
		public void setSize(int aSize)
		{
			size = aSize;
		}
		
		public Stack<T> copy()
		{
			return new Stack(target, size);
		}
	}
}