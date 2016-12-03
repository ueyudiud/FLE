package farcore.lib.collection;

import java.util.Iterator;

import farcore.util.L;

/**
 * The node chain.
 * @author ueyudiud
 *
 * @param <T>
 */
public interface INode<T> extends Iterable<T>
{
	/**
	 * Make a new empty node, added before this node.
	 * (Could not get this node from source node)
	 * @param node The node need added.
	 * @return The empty telomere node.
	 */
	static <T> INode<T> telomereNode(INode<T> node)
	{
		return new TelomereNode(node);
	}
	
	@Override
	default Iterator<T> iterator()
	{
		return new NodeIterator<T>();
	}
	
	T value();
	
	default boolean hasNext()
	{
		return next() != null;
	}
	
	INode<T> next();
	
	default boolean hasLast()
	{
		return last() != null;
	}
	
	INode<T> last();
	
	default boolean contain(Object arg)
	{
		return L.equal(arg, value()) || (containBefore(arg) || containAfter(arg));
	}
	
	default boolean containBefore(Object arg)
	{
		if(!hasLast()) return false;
		INode<T> node = last();
		return L.equal(arg, node.value()) || node.containBefore(arg);
	}
	
	default boolean containAfter(Object arg)
	{
		if(!hasNext()) return false;
		INode<T> node = next();
		return L.equal(arg, node.value()) || node.containAfter(arg);
	}
	
	default void addLast(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	default void addNext(Iterator<? extends T> iterator)
	{
		INode<T> node = this;
		while (iterator.hasNext())
		{
			node.addNext(iterator.next());
			node = node.next();
		}
	}
	
	default void addLast(Iterator<? extends T> iterator)
	{
		INode<T> node = this;
		while (iterator.hasNext())
		{
			node.addLast(iterator.next());
			node = node.last();
		}
	}
	
	default void insertAfter(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	default void insertBefore(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	default void addNext(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Remove this node from node chain.<br>
	 * The removed node will be free.
	 * Before:<br>
	 * N1->N2->N3<br>
	 * After:<br>
	 * N1->N3, N2<br>
	 */
	default void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	static final class NodeIterator<E> implements Iterator<E>
	{
		INode<E> currentNode;
		
		@Override
		public boolean hasNext()
		{
			return currentNode != null;
		}
		
		@Override
		public E next()
		{
			E element = currentNode.value();
			if(currentNode.hasNext())
			{
				currentNode = currentNode.next();
			}
			else
			{
				currentNode = null;
			}
			return element;
		}
		
		@Override
		public void remove()
		{
			INode<E> node = currentNode;
			if(currentNode.hasLast())
			{
				currentNode = currentNode.last();
			}
			else
			{
				currentNode = null;
			}
			node.remove();
		}
	}
}