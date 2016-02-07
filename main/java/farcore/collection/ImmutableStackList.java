package farcore.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import farcore.collection.abs.AbstractStackList;
import farcore.collection.abs.IStackProvider;
import farcore.collection.abs.Stack;

public class ImmutableStackList<S, T> extends AbstractStackList<S, T>
{
	public static class Builder<S, T>
	{
		boolean closed = false;
		IStackProvider<S, T> provider;
		Map<T, Long> map = new HashMap();
		
		public Builder(IStackProvider<S, T> provider)
		{
			this.provider = provider;
		}
		
		private void check()
		{
			if (closed)
				throw new RuntimeException();
		}
		
		public void add(T... targets)
		{
			check();
			for (T t : targets)
			{
				CollectionUtil.add(map, t);
			}
		}
		
		public void add(T target, long size)
		{
			check();
			CollectionUtil.add(map, size, target);
		}
		
		public void addAll(S... stacks)
		{
			check();
			for (S stack : stacks)
			{
				add(provider.getObj(stack), provider.getSize(stack));
			}
		}
		
		public ImmutableStackList build()
		{
			check();
			closed = true;
			return new ImmutableStackList(map, provider);
		}
	}
	
	private final long size;
	private final IStackProvider<S, T> provider;
	private final S[] stacks;
	
	private ImmutableStackList(Map<T, Long> map, IStackProvider<S, T> provider)
	{
		this.provider = provider;
		List<Stack<T>> list = Arrays.asList(CollectionUtil.asArray(map));
		List<S> sList = new ArrayList(list.size());
		int s = 0;
		for (Stack<T> stack : list)
		{
			sList.add(provider.make(stack.obj, stack.size));
			s += stack.size;
		}
		size = s;
		this.stacks = sList.toArray(provider.newArray(sList.size()));
	}
	
	@Override
	public void add(T... targets)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(T target, long size)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addAll(S... stacks)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(T... targets)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public S removeAll(S stack)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<S> removeAll(S... stacks)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean contain(T target)
	{
		for (S s : stacks)
		{
			if (provider.contain(s, target))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(S stack)
	{
		for (S s : stacks)
		{
			if (provider.contains(s, stack))
				return true;
		}
		return false;
	}
	
	@Override
	public long species()
	{
		return stacks.length;
	}
	
	@Override
	public long size()
	{
		return size;
	}
	
	@Override
	public long weight(T target)
	{
		for (S s : stacks)
		{
			if (provider.contain(s, target))
				return provider.getSize(s);
		}
		return 0;
	}
	
	@Override
	public double scale(T target)
	{
		return (double) weight(target) / (double) size;
	}
	
	@Override
	public T randomGet(Random rand)
	{
		return null;
	}
	
	@Override
	public Map<T, Long> toMap()
	{
		Map<T, Long> map = new HashMap();
		for (S stack : stacks)
		{
			map.put(provider.getObj(stack), provider.getSize(stack));
		}
		return map;
	}
	
	@Override
	public Stack<T>[] toArray()
	{
		Stack<T>[] array = new Stack[stacks.length];
		for (int i = 0; i < stacks.length; array[i] = new Stack(
				provider.getObj(stacks[i]), provider.getSize(stacks[i])), ++i)
			;
		return array;
	}
	
	@Override
	public Iterator<S> iterator()
	{
		return Arrays.asList(stacks).iterator();
	}
}