package fle.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import fle.api.material.IAtoms;
import fle.api.util.WeightHelper.Stack;

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
	public static <T> Map<T, Integer> asMap(Stack<T>...list)
	{
		Map<T, Integer> map = new HashMap<T, Integer>();
		for(Stack<T> stack : list)
		{
			map.put(stack.target, stack.size);
		}
		return map;
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
			map.put(e.target, e.size * size);
		}
	}
	public static <T> void add(Map<T, Integer> map, T...e)
	{
		for(T t : e) add(map, t);
	}
	public static <T> void add(Map<T, Integer> map, int size, T...e)
	{
		for(T t : e) add(map, new Stack<T>(t, size));
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

	public static <T> boolean remove(Map<T, Integer> map, T e)
	{
		if(e != null)
		{
			if(map.containsKey(e))
			{
				int a = map.get(e) - 1;
				if(a > 0)
					map.put(e, a);
				else
					map.remove(e);
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	public static <T> int remove(Map<T, Integer> map, Stack<T> e)
	{
		if(e.target != null && e.size > 0)
		{
			if(map.containsKey(e.target))
			{
				int a = map.get(e.target) - e.size;
				if(a > 0)
					map.put(e.target, a);
				else
					map.remove(e.target);
				return a > 0 ? e.size : a + e.size;
			}
			return 0;
		}
		return 0;
	}
	public static <T> int remove(Map<T, Integer> map, Stack<T> e, int size)
	{
		return remove(map, new Stack(e.target, e.size * size));
	}
	public static <T> void remove(Map<T, Integer> map, T...e)
	{
		for(T t : e) remove(map, t);
	}
	public static <T> void remove(Map<T, Integer> map, int size, T...e)
	{
		for(T t : e) remove(map, new Stack<T>(t, size));
	}
	public static <T> void remove(Map<T, Integer> map, Stack<T>...e)
	{
		for(Stack<T> ts : e) remove(map, ts);
	}
	public static <T> void remove(Map<T, Integer> map, int size, Stack<T>...e)
	{
		for(Stack<T> ts : e) remove(map, ts, size);
	}
	public static <T> void remove(Map<T, Integer> aMap,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) remove(aMap, new Stack(e.getKey(), e.getValue()));
	}
	public static <T> void remove(Map<T, Integer> aMap, int size,
			Map<T, Integer> aValue)
	{
		for(Entry<T, Integer> e : aValue.entrySet()) remove(aMap, new Stack(e.getKey(), e.getValue() * size));
	}
	public static <T> Stack<T>[] multiply(Stack<T>[] aStacks, int size)
	{
		Stack<T>[] ret = new Stack[aStacks.length];
		int i = 0;
		for(Stack<T> stack : aStacks)
		{
			ret[i] = stack.copy();
			ret[i].size *= size;
			++i;
		}
		return ret;
	}
	
	private static final Random rand = new Random();
	private static Stack EMPTY_STACK = new Stack(null);
	private int length;
	private Stack<T>[] sts;

	public WeightHelper()
	{
		length = 0;
		sts = new Stack[0];
	}
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

	private boolean arrange()
	{
		for(int i = 0; i < sts.length - 1; ++i)
		{
			if(sts[i].getObj() == null)
			{
				sts[i] = sts[sts.length - 1];
				sts[sts.length - 1] = EMPTY_STACK.copy();
				return true;
			}
			for(int j = i + 1; j < sts.length; ++j)
			{
				if(sts[j].getObj() != null)
					if(sts[j].getObj().equals(sts[i].getObj()))
					{
						sts[Math.min(i, j)].addStackIn(sts[Math.max(i, j)].size);
						sts[Math.max(i, j)] = sts[sts.length - 1];
						if(Math.max(i, j) != sts.length - 1)
						{
							sts[sts.length - 1] = EMPTY_STACK.copy();
							return true;
						}
					}
			}
		}
		return true;
	}
	private void addListSize()
	{
		Stack<T>[] instead = new Stack[sts.length + 1];
		System.arraycopy(sts, 0, instead, 0, sts.length);
		sts = instead;
	}
	
	public void add(Stack<T>...target)
	{
		arrange();
		label:
		for(Stack<T> s : target)
		{
			if(s == null) continue;
			if(s.getObj() == null) continue;
			for(int i = 0; i < sts.length; ++i)
			{
				Stack<T> stack = sts[i];
				if(stack.target == null)
				{
					sts[i] = s.copy();
					length += s.size;
					continue label;
				}
				if(stack.target.equals(s.target))
				{
					stack.addStackIn(s.size);
					length += s.size;
					continue label;
				}
			}
			addListSize();
			length += s.size;
			sts[sts.length - 1] = s.copy();
		}
	}
	public void add(T...target)
	{
		arrange();
		label :
		for(T s : target)
		{
			if(s == null) continue;
			for(int i = 0; i < sts.length; ++i)
			{
				Stack<T> stack = sts[i];
				if(stack.target == null)
				{
					sts[i] = new Stack<T>(s);
					length++;
					continue label;
				}
				if(stack.target.equals(s))
				{
					stack.addStackIn(1);
					length++;
					continue label;
				}
			}
			addListSize();
			sts[sts.length - 1] = new Stack(s);
			length++;
		}
	}
	
	public int remove(T target)
	{
		if(target == null) return 0;
		arrange();
		for(int i = 0; i < sts.length; ++i)
		{
			Stack<T> stack = sts[i];
			if(stack.getObj().equals(target))
			{
				int size = stack.size;
				sts[i] = EMPTY_STACK;
				length -= size;
				return size;
			}
		}
		return 0;
	}
	public Stack<T> remove(T target, int aSize)
	{
		if(target == null || aSize <= 0) return null;
		for(int i = 0; i < sts.length; ++i)
		{
			Stack<T> stack = sts[i];
			if(stack.getObj() == null) continue;
			if(stack.getObj().equals(target))
			{
				int size = Math.min(aSize, stack.size);
				sts[i].minusStackOut(size);
				length -= size;
				return new Stack(target, size);
			}
		}
		return null;
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
		arrange();
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
		if(length <= 0) return null;
		int i = rand.nextInt(length);
		int cache = 0;
		for(Stack<T> stack : sts)
		{
			if(stack.getObj() == null) continue;
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
		int size;
		T target;
		
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
		
		public Stack<T> minusStackOut(int aS)
		{
			if(target == null) return EMPTY_STACK.copy();
			int ret = Math.min(size, aS);
			size -= aS;
			if(size <= 0)
			{
				size = 0;
				target = null;
			}
			return new Stack<T>(target, ret);
		}
		
		@Override
		public String toString()
		{
			return target == null ? "empty" : target.toString() + "x" + size;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof Stack)) return false;
			return target == null ? ((Stack) obj).getObj() == null : target.equals(((Stack) obj).getObj());
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

	public void toMap(Map<T, Integer> map)
	{
		for(Stack<T> t : sts)
		{
			if(t == null) continue;
			if(t.size <= 0 || t.target == null) continue;
			map.put(t.target, t.size);
		}
	}
	
	public boolean contain(T target)
	{
		for(Stack<T> s : sts)
			if(s.target != null && s.target.equals(target)) return true;
		return false;
	}
	
	public boolean contain(Stack<T> stack)
	{
		return getSize(stack.getObj()) >= stack.size;
	}
	
	public int getSize(T target)
	{
		for(Stack<T> s : sts)
			if(s.target != null && s.target.equals(target)) return s.size;
		return 0;
	}
}