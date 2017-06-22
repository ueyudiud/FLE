package nebula.base.function;

import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.Iterators;

import nebula.base.INode;
import nebula.base.IntegerEntry;
import nebula.base.Node;
import nebula.base.Stack;

public class WeightedRandomSelector<T> implements Iterable<IntegerEntry<T>>, Selector<T>
{
	int allWeight;
	INode<IntegerEntry<T>> first;
	INode<IntegerEntry<T>> last;
	
	public WeightedRandomSelector()
	{
	}
	public WeightedRandomSelector(Stack<T>...stacks)
	{
		for(Stack<T> stack : stacks)
		{
			add(stack.element, (int) stack.size);
		}
	}
	
	public int weight()
	{
		return this.allWeight;
	}
	
	public void add(T value, int weight)
	{
		if(this.first == null)
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
		if(this.first == null || this.allWeight == 0) return null;
		int i = random.nextInt(this.allWeight) - this.first.value().getValue();
		INode<IntegerEntry<T>> node = this.first;
		while(i > 0 && //The weight is still more than random number
				node.hasNext())//Or it has no node any more.
		{
			i -= node.value().getValue();
			node = node.next();
		}
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