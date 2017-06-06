/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base;

import java.util.Iterator;
import java.util.function.Predicate;

import com.google.common.collect.Iterators;

import nebula.common.util.L;

/**
 * The node chain.<p>
 * Used as a LinkedList.
 * @author ueyudiud
 *
 * @param <T> The target type contain in node.
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
		return new TelomereNode<>(node);
	}
	
	/**
	 * The iterator of node,
	 * the iterator will get next node util there is no node next.
	 * @return The interator.
	 */
	@Override
	default Iterator<T> iterator()
	{
		return new NodeIterator<>(this);
	}
	
	/**
	 * The value contain in node.
	 * @return
	 */
	T value();
	
	/**
	 * Is there has next node?
	 * @return
	 */
	default boolean hasNext()
	{
		return next() != null;
	}
	
	/**
	 * Return next node.
	 * @return
	 */
	INode<T> next();
	
	default boolean hasLast()
	{
		return last() != null;
	}
	
	INode<T> last();
	
	/**
	 * Get if the node chain contain a element.
	 * @param arg
	 * @return
	 */
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
	
	/**
	 * Find first matched element in node chain.
	 * @param judgable
	 * @return The matched target.
	 */
	default T find(Predicate<T> judgable)
	{
		T result;
		return judgable.test(value()) ? value() :
			(result = findBefore(judgable)) != null ? result :
				findAfter(judgable);
	}
	
	default T findAfter(Predicate<T> judgable)
	{
		if (!hasNext()) return null;
		INode<T> node = next();
		T result;
		return judgable.test(result = node.value()) ? result : node.findAfter(judgable);
	}
	
	default T findBefore(Predicate<T> judgable)
	{
		if (!hasLast()) return null;
		INode<T> node = last();
		T result;
		return judgable.test(result = node.value()) ? result : node.findBefore(judgable);
	}
	
	/**
	 * Add a new node at the start of node chain.
	 * @param target The target will contain in new node.
	 * @throws java.lang.UnsupportedOperationException If this node is immutable node chain.
	 */
	default void addLast(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Add nodes at end of node chain.
	 * @param iterator
	 * @throws java.lang.UnsupportedOperationException If this node is immutable node chain.
	 */
	default void addNext(Iterator<? extends T> iterator)
	{
		INode<T> node = this;
		while (iterator.hasNext())
		{
			node.addNext(iterator.next());
			node = node.next();
		}
	}
	
	/**
	 * Add nodes at start of node chain.
	 * @param iterator
	 * @throws java.lang.UnsupportedOperationException If this node is immutable node chain.
	 */
	default void addLast(Iterator<? extends T> iterator)
	{
		INode<T> node = this;
		while (iterator.hasNext())
		{
			node.addLast(iterator.next());
			node = node.last();
		}
	}
	
	/**
	 * Insert a new node between this node and next node.
	 * @param target
	 */
	default void insertAfter(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Insert a new node between this node and last node.
	 * @param target
	 */
	default void insertBefore(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Add a new node at the end of node chain.
	 * @param target The target will contain in new node.
	 * @throws java.lang.UnsupportedOperationException If this node is immutable node chain.
	 */
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
	default T remove()
	{
		throw new UnsupportedOperationException();
	}
	
	default T removeNext()
	{
		if (!hasNext())
			throw new IllegalStateException();
		INode<T> node = next();
		return node.remove();
	}
	
	default T removeLast()
	{
		if (!hasLast())
			throw new IllegalStateException();
		INode<T> node = last();
		return node.remove();
	}
	
	default T[] toArray(Class<T> clazz)
	{
		return Iterators.toArray(iterator(), clazz);
	}
	
	static final class NodeIterator<E> implements Iterator<E>
	{
		INode<E> currentNode;
		
		public NodeIterator(INode<E> node)
		{
			this.currentNode = node;
		}
		
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