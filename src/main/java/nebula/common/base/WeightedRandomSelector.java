package nebula.common.base;

import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.Iterators;

public class WeightedRandomSelector<T> implements Iterable<IntegerEntry<T>>, Selector<T>
{
	int allWeight;
	INode<IntegerEntry<T>> first;
	INode<IntegerEntry<T>> last;
	
	public WeightedRandomSelector()
	{
	}
	public WeightedRandomSelector(Stack<T>[] stacks)
	{
		for(Stack<T> stack : stacks)
		{
			add(stack.element, (int) stack.size);
		}
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
	
	@Override
	public T next(Random random)
	{
		if(this.first == null || this.allWeight == 0) return null;
		int i = random.nextInt(this.allWeight);
		INode<IntegerEntry<T>> node = this.first;
		while(i > 0 && //The weight is still more than random number
				node.hasNext())//Or it has no node any more.
		{
			i -= node.value().value;
			node = node.next();
		}
		return node.value().key;
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