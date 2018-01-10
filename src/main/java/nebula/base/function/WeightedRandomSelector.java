/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.function;

import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.Iterators;

import nebula.base.INode;
import nebula.base.IntMap;
import nebula.base.IntegerEntry;
import nebula.base.Node;
import nebula.base.Stack;
import nebula.common.util.Maths;

public class WeightedRandomSelector<T> implements Iterable<IntegerEntry<T>>, Selector<T>
{
	int						allWeight;
	INode<IntegerEntry<T>>	first;
	INode<IntegerEntry<T>>	last;
	
	public WeightedRandomSelector()
	{
	}
	
	public WeightedRandomSelector(Stack<? extends T>...stacks)
	{
		for (Stack<? extends T> stack : stacks)
		{
			add(stack.element, (int) stack.size);
		}
	}
	
	public WeightedRandomSelector(IntMap<? extends T> map)
	{
		for (IntegerEntry<? extends T> entry : map)
		{
			add(entry.getKey(), entry.getValue());
		}
	}
	
	public int weight()
	{
		return this.allWeight;
	}
	
	public void add(T value, int weight)
	{
		if (weight < 0) throw new IllegalArgumentException("Weight can not be negetive value. got: " + weight);
		if (weight == 0) return;
		if (this.first == null)
		{
			this.first = this.last = Node.first(new IntegerEntry(value, weight));
			this.allWeight = weight;
		}
		else
		{
			this.last.addNext(new IntegerEntry(value, weight));
			this.allWeight += weight;
			this.last = this.last.next();
		}
	}
	
	public boolean isEmpty()
	{
		return this.allWeight == 0;
	}
	
	/**
	 * Clean selector target list.
	 */
	public void clear()
	{
		this.allWeight = 0;
		this.first = this.last = null;
	}
	
	@Override
	public T next(Random random)
	{
		if (this.first == null || this.allWeight == 0) return null;
		int i = random.nextInt(this.allWeight);
		INode<IntegerEntry<T>> node;
		for (node = this.first; (i -= node.value().getValue()) >= 0 && node.hasNext();
				// The weight is still more than random number, or it has no node any more.
				node = node.next());
		return node.value().getKey();
	}
	
	/**
	 * The use specific seed for generate.
	 * 
	 * @param random the random seed.
	 * @return
	 */
	public T next(int random)
	{
		if (this.first == null || this.allWeight == 0) return null;
		int i = Maths.mod(random, this.allWeight);
		INode<IntegerEntry<T>> node;
		for (node = this.first;
				(i -= node.value().getValue()) >= 0 && node.hasNext();
				// The weight is still more than random number, or it has no node any more.
				node = node.next());
		return node.value().getKey();
	}
	
	@Override
	public Iterator<IntegerEntry<T>> iterator()
	{
		return this.first == null ? Iterators.emptyIterator() : new WRSIterator();
	}
	
	private class WRSIterator implements Iterator<IntegerEntry<T>>
	{
		INode<IntegerEntry<T>> now;
		
		WRSIterator()
		{
			this.now = INode.telomereNode(WeightedRandomSelector.this.first);
		}
		
		@Override
		public boolean hasNext()
		{
			return this.now.hasNext();
		}
		
		@Override
		public IntegerEntry<T> next()
		{
			this.now = this.now.next();
			return this.now.value();
		}
	}
}
