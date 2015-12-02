package fle.api.util;

import java.util.Arrays;
import java.util.Map;

import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.util.WeightHelper.Stack;

/**
 * The new reaction handler.
 * @author ueyudiud
 *
 */
public class SizeUtil
{
	public static final SizeUtil instance = new SizeUtil();
	
	public static SizeUtil makeNewSizeUtil()
	{
		try
		{
			
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		return instance;
	}
	
	private SizeUtil()
	{
		
	}
	
	public static <T> Stack<T>[] trim(I<T>...is)
	{
		if(is.length == 0) return new Stack[0];
		if(is.length == 1)
		{
			return new Stack[]{new Stack(is[0].target, 1)};
		}
		int[] ret = new int[is.length];
		int m = getMaxDivisor(ret[0] = is[0].getSize(), ret[1] = is[1].getSize());
		for(int i = 2; i < is.length; ++i)
			m = getMaxDivisor(m, ret[i] = is[i].getSize());
		for(int i = 0; i < ret.length; ret[i++] /= m);
		Stack<T>[] ret1 = new Stack[ret.length];
		for(int i = 0; i < ret1.length; ret1[i++] = new Stack(is[i].target, ret[i]));
		return ret1;
	}	
	public static <T> int[] trim(Stack<T>...stacks)
	{
		if(stacks.length == 0) return new int[0];
		if(stacks.length == 1)
		{
			return new int[]{stacks[0].getSize()};
		}
		int[] ret = new int[stacks.length];
		int m = getMaxDivisor(ret[0] = stacks[0].getSize(), ret[1] = stacks[1].getSize());
		for(int i = 2; i < stacks.length; ++i)
			m = getMaxDivisor(m, ret[i] = stacks[i].getSize());
		for(int i = 0; i < ret.length; ret[i++] /= m);
		return ret;
	}
	public static <T> boolean match(Stack<T>[] recipe, Map<T, Integer> map)
	{
		for(Stack<T> stack : recipe)
		{
			if(!map.containsKey(stack.getObj())) return false;
			if(map.get(stack.getObj()) < stack.getSize()) return false;
		}
		return true;
	}
	public static <T> boolean match(Stack<T>[] recipe, I<T>...input)
	{
		label:
		for(Stack<T> stack : recipe)
		{
			for(int j = 0; j < input.length; ++j)
			{
				if(input[j] == null) continue;
				I<T> in = input[j];
				if(in.target == null) continue;
				if(!stack.getObj().equals(in.target)) continue;
				if(in.getSize() <= stack.getSize()) return false;
				continue label;
			}
			return false;			
		}
	    return true;
	}
	public static <T> Stack<T>[] matchInputs(Stack<T>[] recipe, I<T>...input)
	{
		Stack<T>[] ret = new Stack[input.length];
		int i = 0;
		for(Stack<T> stack : recipe)
		{
			for(int j = 0; j < input.length; ++j)
			{
				if(input[j] == null) continue;
				I<T> in = input[j];
				if(in.target == null) continue;
				if(!stack.getObj().equals(in.target)) continue;
				if(in.getSize() <= stack.getSize())
				{
					i = 0;
					break;
				}
				ret[i] = new Stack(in.target, in.getPerSize() * stack.getSize());
				i = i == 0 ? in.getSize() / stack.getSize() : Math.min(i, in.getSize() / stack.getSize());
			}
			if(i == 0)
			{
				Arrays.fill(ret, null);
				return ret;
			}
		}
		for(int j = 0; j < ret.length; ret[j++].size *= i);
		return ret;
	}
	public static <T> int matchOutputs(Stack<T>[] recipe, I<T>...input)
	{
		int i = 0;
		for(Stack<T> stack : recipe)
		{
			for(int j = 0; j < input.length; ++j)
			{
				if(input[j] == null) continue;
				I<T> in = input[j];
				if(in.target == null) continue;
				if(!stack.getObj().equals(in.target)) continue;
				if(in.getSize() <= stack.getSize()) return 0;
				i = i == 0 ? in.getSize() / stack.getSize() : Math.min(i, in.getSize() / stack.getSize());
			}
			if(i == 0)
			{
				return 0;
			}
		}
		return i;
	}
	
	public static int getMaxDivisor(int x, int y)
	{
		if(x == 0 || y == 0) return 0;
		int a = x < 0 ? -x : x;
		int b = y < 0 ? -y : y;
		if(a < b)
		{
			a ^= b;
			b ^= a;
			a ^= b;
		}
		while(a % b != 0)
		{
			a -= (a / b) * b;
			a ^= b;
			b ^= a;
			a ^= b;
		}
		return a;
	}
	
	public static int getMinMultiple(int x, int y)
	{
		int m = getMaxDivisor(x, y);
		return x * y / m;
	}

	public static <T> I<T> info(T target, int size)
	{
		return info(target, size, Size.Simple);
	}
	public static <T> I<T> info(T target, int size, Size aSize)
	{
		return info(target, size, aSize, Solute.Pure);
	}
	public static <T> I<T> info(T target, int size, Size aSize, Solute solute)
	{
		I<T> i = new I();
		i.target = target;
		i.s = size;
		i.size = aSize;
		i.solute = solute;
		return i;
	}
	
	public static class I<T>
	{
		T target;
		int s;
		Size size;
		Solute solute;
		
		private I()
		{
			
		}

		public T getTarget()
		{
			return target;
		}
		
		public int getSize()
		{
			return s * size.size / solute.getSize();
		}
		
		public int getPerSize()
		{
			return size.size / solute.getSize();
		}
		
		public I<T> copy(int scale)
		{
			return info(target, scale, size, solute);
		}
	}
	
	public static enum Size
	{
		Particle(1),
		Grain(8),
		Nugget(8 * 9),
		Small(4 * 9 * 9),
		Simple(8 * 9 * 9),
		Large(4 * 8 * 9 * 9);
		
		int size;
		
		private Size(int aSize)
		{
			size = aSize;
		}
		
		public int getSize()
		{
			return size;
		}
	}
	public static enum Solute
	{
		Pure(1),
		Tick(8),
		Dilute(8 * 9),
		Rare(8 * 9 * 9);
		
		int size;
		
		private Solute(int aPerSize)
		{
			size = aPerSize;
		}
		
		public int getSize()
		{
			return size;
		}
	}
}