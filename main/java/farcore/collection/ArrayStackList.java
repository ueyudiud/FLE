package farcore.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import farcore.collection.abs.AbstractStackList;
import farcore.collection.abs.IStackProvider;
import farcore.collection.abs.Stack;

/**
 * The array stack list, with <code>provider</code> to provide "stack" type.<br>
 * Save target by array with stack elements. 
 * The target type elements will add into another stack
 * when they are equals.
 * <code>
 * 	S stack = list[i];<br>
 * 	if(provider.contain(stack, t))<br>
 * 	{<br>
 * 		provider.add(stack, 1);<br>
 * 		continue label;<br>
 * 	}<br>
 * </code>
 * @author ueyudiud
 * @see farcore.collection.abs.IStackList
 * @see farcore.collection.abs.IStackProvider
 * @param <S> Stack type, contain target information can let provider identify target.
 * @param <T> Target type.
 */
public class ArrayStackList<S, T> extends AbstractStackList<S, T>
{
	/**
	 * The stack provider.
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
			if(t == null) continue;
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
	public void add(T target, long size)
	{
		if(target == null) throw new NullPointerException("Target can not be null!");
		if(size < 0) throw new IllegalArgumentException("The size of target " + size + " must be an positive number!");
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
			if(t == null) throw new NullPointerException("Stack can not be null.");
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
	public long species()
	{
		return species;
	}
	
	@Override
	public long size()
	{
		return size;
	}
	
	@Override
	public long weight(T target)
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
		long i = rand.nextLong() % size();
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
	public Map<T, Long> toMap()
	{
		Map<T, Long> ret = new HashMap();
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
		public synchronized S next()
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