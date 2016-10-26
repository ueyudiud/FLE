package farcore.lib.collection;

import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.Iterators;

public class WeightedRandomSelector<T> implements Iterable<IntegerEntry<T>>, Selector<T>
{
	int allWeight;
	INode<IntegerEntry<T>> first;
	INode<IntegerEntry<T>> last;

	public void add(T value, int weight)
	{
		if(first == null)
		{
			first = last = Node.first(new IntegerEntry(value, weight));
			allWeight = weight;
		}
		else
		{
			last.addNext(new IntegerEntry(value, weight));
			allWeight += weight;
			last = last.next();
		}
	}

	@Override
	public T next(Random random)
	{
		if(first == null || allWeight == 0) return null;
		int i = random.nextInt(allWeight);
		INode<IntegerEntry<T>> node = first;
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
		return first == null ? Iterators.emptyIterator() : new WRSIterator();
	}

	private class WRSIterator implements Iterator<IntegerEntry<T>>
	{
		INode<IntegerEntry<T>> now;

		WRSIterator()
		{
			now = INode.telomereNode(first);
		}

		@Override
		public boolean hasNext()
		{
			return now.hasNext();
		}
		
		@Override
		public IntegerEntry<T> next()
		{
			now = now.next();
			return now.value();
		}
	}
}