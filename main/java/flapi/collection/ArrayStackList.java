package flapi.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import flapi.collection.abs.AbstractStackList;
import flapi.collection.abs.IStackProvider;
import flapi.collection.abs.Stack;

/**
 * 
 * @author ueyudiud
 *
 * @param <S>
 * @param <T>
 */
public class ArrayStackList<S, T> extends AbstractStackList<S, T>
{
	/**
	 * The stack provider.<br>
	 */
	private final IStackProvider<S, T> provider;
	private final float scale;
	protected S[] list;
	protected int species;
	protected int size;

	public ArrayStackList(IStackProvider<S, T> provider)
	{
		this(provider, 16);
	}
	public ArrayStackList(IStackProvider<S, T> provider, int size)
	{
		this(provider, size, 0.25F);
	}
	public ArrayStackList(IStackProvider<S, T> provider, int size, float scale)
	{
		if(size <= 0) throw new IndexOutOfBoundsException("Size can't be an non-positive number.");
		if(scale <= 0) throw new IndexOutOfBoundsException("Scale can't be an non-positive number.");
		if(provider == null) throw new RuntimeException("Provider can't be null.");
		this.provider = provider;
		list = provider.newArray(size);
		this.scale = scale;
	}

	private boolean matchEqual(int id, T target)
	{
		if(list.length <= id || id < 0) return false;
		return provider.equal(provider.getObj(list[id]), target);
	}
	private boolean canRecover(int id)
	{
		if(list.length <= id || id < 0) return false;
		return list[id] == null || provider.getSize(list[id]) == 0;
	}
	private void addSize(int size)
	{
		if(size < list.length) return;
		S[] n = provider.newArray(size);
		System.arraycopy(list, 0, n, 0, list.length);
	}
	
	@Override
	public void add(T... targets)
	{
		List<T> l = new ArrayList(targets.length);
		label:
		for(T t : targets)
		{
			int firstEmpty = -1;
			for(int i = 0; i < list.length; ++i)
			{
				S stack = list[i];
				if(canRecover(i) && firstEmpty == -1)
				{
					firstEmpty = i;
				}
				else if(provider.contain(stack, t))
				{
					provider.add(stack, 1);
					continue label;
				}
			}
			if(firstEmpty != -1)
			{
				list[firstEmpty] = provider.make(t, 1);
				++species;
				continue label;
			}
			l.add(t);
		}
		if(!l.isEmpty())
		{
			int length = list.length;
			addSize((int) (length * (1 + scale) + l.size()));
			label:
			for(T t : l)
			{
				for(int i = length; i < list.length; ++i)
				{
					if(canRecover(i))
					{
						list[i] = provider.make(t, 1);
						++species;
						continue label;
					}
					S stack = list[i];
					if(provider.contain(stack, t))
					{
						provider.add(stack, 1);
						continue label;
					}
				}
			}
		}
		size += targets.length;
	}
	
	@Override
	public void add(T target, int size)
	{
		int firstEmpty = -1;
		for(int i = 0; i < list.length; ++i)
		{
			S stack = list[i];
			if(canRecover(i) && firstEmpty == -1)
			{
				firstEmpty = i;
			}
			else if(provider.contain(stack, target))
			{
				provider.add(stack, size);
				return;
			}
		}
		if(firstEmpty != -1)
		{
			list[firstEmpty] = provider.make(target, size);
			++species;
			return;
		}
		int length = list.length;
		addSize((int) (length * (1 + scale)));
		list[length] = provider.make(target, size);
		++species;
		this.size += size;
	}
	
	@Override
	public void addAll(S... stacks)
	{
		List<S> l = new ArrayList(stacks.length);
		label:
		for(S t : stacks)
		{
			int firstEmpty = -1;
			for(int i = 0; i < list.length; ++i)
			{
				S stack = list[i];
				if(canRecover(i) && firstEmpty == -1)
				{
					firstEmpty = i;
				}
				else if(provider.contain(stack, provider.getObj(t)))
				{
					provider.add(stack, provider.getSize(t));
					continue label;
				}
			}
			if(firstEmpty != -1)
			{
				list[firstEmpty] = provider.copy(t);
				++species;
				continue label;
			}
			l.add(t);
		}
		if(!l.isEmpty())
		{
			int length = list.length;
			addSize((int) (length * (1 + scale) + l.size()));
			label:
			for(S t : l)
			{
				for(int i = length; i < list.length; ++i)
				{
					if(canRecover(i))
					{
						list[i] = provider.copy(t);
						++species;
						continue label;
					}
					S stack = list[i];
					if(provider.contain(stack, provider.getObj(t)))
					{
						provider.add(stack, 1);
						continue label;
					}
				}
			}
		}
		for(S s : stacks)
			size += provider.getSize(s);
	}
	
	@Override
	public boolean remove(T target)
	{
		if(target == null) return true;
		for(int i = 0; i < list.length; ++i)
		{
			if(matchEqual(i, target))
			{
				size -= provider.getSize(list[i]);
				list[i] = null;
				--species;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void remove(T... targets)
	{
		for(T t : targets)
			remove(t);
	}
	
	@Override
	public S removeAll(S stack)
	{
		if(stack == null || provider.getSize(stack) == 0) return null;
		T target = provider.getObj(stack);
		for(int i = 0; i < list.length; ++i)
		{
			if(matchEqual(i, target))
			{
				S ret = provider.decr(list[i], provider.getSize(stack));
				size -= provider.getSize(ret);
				if(provider.getSize(list[i]) == 0)
				{
					list[i] = null;
					--species;
				}
				return ret;
			}
		}
		return null;
	}
	
	@Override
	public List<S> removeAll(S... stacks)
	{
		List<S> ret = new ArrayList();
		for(S stack : stacks)
		{
			S a = removeAll(stack);
			if(a != null)
				ret.add(a);
		}
		return ret;
	}
	
	@Override
	public boolean contain(T target)
	{
		if(target == null) return false;
		for(int i = 0; i < list.length; ++i)
			if(matchEqual(i, target))
				return true;
		return false;
	}
	
	@Override
	public boolean contains(S stack)
	{
		if(stack == null) return false;
		if(provider.getSize(stack) == 0) return true;
		T target = provider.getObj(stack);
		for(int i = 0; i < list.length; ++i)
			if(matchEqual(i, target))
				if(provider.contains(list[i], stack))
				{
					return true;
				}
				else
				{
					return false;
				}
		return false;
	}
	
	@Override
	public int species()
	{
		return species;
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public int weight(T target)
	{
		if(target == null) return 0;
		for(int i = 0; i < list.length; ++i)
			if(matchEqual(i, target))
				return provider.getSize(list[i]);
		return 0;
	}
	
	@Override
	public double scale(T target)
	{
		return (double) weight(target) / (double) size();
	}
	
	@Override
	public T randomGet(Random rand)
	{
		if(size() == 0) return null;
		int i = rand.nextInt(size());
		T ret = null;
		Iterator<S> itr = iterator();
		while(itr.hasNext())
		{
			S s = itr.next();
			i -= provider.getSize(s);
			if(i < 0)
			{
				ret = provider.getObj(s);
				break;
			}
		}
		return ret;
	}
	
	@Override
	public Map<T, Integer> toMap()
	{
		Map<T, Integer> ret = new HashMap();
		for(S stack : list)
		{
			ret.put(provider.getObj(stack), provider.getSize(stack));
		}
		return ret;
	}
	
	@Override
	public Stack<T>[] toArray()
	{
		Stack<T>[] ret = new Stack[species];
		int j = 0;
		for(S s : list)
		{
			if(s == null) continue;
			ret[j] = new Stack(provider.getObj(s), provider.getSize(s));
			++j;
		}
		return ret;
	}
	
	@Override
	public Iterator<S> iterator()
	{
		return new StackIterator();
	}
	
	public class StackIterator implements Iterator<S>
	{
		int cache;

		@Override
		public boolean hasNext()
		{
			int c = cache;
			while(list.length > c)
			{
				if(list[c++] != null) return true;
			}
			return false;
		}

		@Override
		public S next()
		{
			int c = cache;
			while(list.length > c)
			{
				if(list[c] != null)
				{
					return list[(cache = c + 1) - 1];
				}
				++c;
			}
			return null;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}