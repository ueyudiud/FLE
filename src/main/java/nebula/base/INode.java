/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import nebula.common.util.L;

/**
 * The node type in a <b>node chain</b>.
 * <p>
 * The node chain contains some nodes to store elements, and the chain can also
 * get whole nodes in chain by {@link #next()} or {@link #last()}.
 * <p>
 * The each node chain likes a {@link java.util.LinkedList}, but in a chain, you
 * need take operation on each node instead of whole node chain.
 * <p>
 * Some words meaning in this type:
 * <li>
 * next/last - the node return by <tt>node.next()</tt> / <tt>node.last()</tt>
 * <li>
 * start/end - the node be called <tt>node = node.next()</tt> /
 * <tt>node = node.last()</tt> until no node returned, and the last node is
 * starting/ending of node chain.
 * <li>
 * after/before - the node can be called by <tt>node = node.next()</tt> /
 * <tt>node = node.last()</tt> several times(at least once) to get.
 * </li>
 * 
 * @author ueyudiud
 *
 * @param <T> the target type contain in node.
 */
public interface INode<T> extends Iterable<T>
{
	/**
	 * Make a new empty node, added before this node. (Could not get this node
	 * from source node)
	 * 
	 * @param node The node need added.
	 * @return The empty telomere node.
	 */
	static <T> INode<T> telomereNode(INode<T> node)
	{
		return new TelomereNode<>(node);
	}
	
	/**
	 * The iterator of node, the iterator will get next node until the
	 * <tt>hasNext()</tt> return <code>false</code>.
	 * 
	 * @return The iterator.
	 */
	@Override
	default Iterator<T> iterator()
	{
		return new NodeIterator<>(this);
	}
	
	default Stream<T> stream()
	{
		return StreamSupport.<T>stream(new Spliterator<T>()
		{
			INode<T> node = INode.this;
			
			@Override
			public boolean tryAdvance(Consumer<? super T> action)
			{
				if (node != null)
				{
					action.accept(node.value());
					node = node.next();
					return true;
				}
				return false;
			}
			
			@Override
			public Spliterator<T> trySplit()
			{
				return null;
			}
			
			@Override
			public long estimateSize()
			{
				return Long.MAX_VALUE;
			}
			
			@Override
			public int characteristics()
			{
				return 0;
			}
		}, false);
	}
	
	/**
	 * The value contain in node.
	 * 
	 * @return the value.
	 */
	T value();
	
	/**
	 * Return <tt>true</tt> if the next node is exist.
	 * 
	 * @return <tt>true</tt> if the next node
	 * @see #next()
	 */
	default boolean hasNext()
	{
		return next() != null;
	}
	
	/**
	 * Return next node.
	 * 
	 * @return the next node, <code>null</code> if next node does not exist.
	 * @see #hasNext()
	 * @see #last()
	 */
	@Nullable
	INode<T> next();
	
	/**
	 * Return <tt>true</tt> if the last node is exist.
	 * 
	 * @return <tt>true</tt> if the last node
	 * @see #last()
	 */
	default boolean hasLast()
	{
		return last() != null;
	}
	
	/**
	 * Return last node.
	 * 
	 * @return the last node, <code>null</code> if last node does not exist.
	 * @see #hasLast()
	 * @see #next()
	 */
	@Nullable
	INode<T> last();
	
	/**
	 * Return the first node of node chain.
	 * <p>
	 * For result is non-null (return itself if this node is already node in
	 * starting) and <tt>getStart().last()</tt> will return <tt>null</tt>.
	 * 
	 * @return the first node of node chain.
	 */
	@Nonnull
	default INode<T> getStart()
	{
		INode<T> node = this;
		while (hasLast())
			node = last();
		return node;
	}
	
	/**
	 * Return the last node of node chain.
	 * <p>
	 * For result is non-null (return itself if this node is already node in
	 * ending) and <tt>getEnd().next()</tt> will return <tt>null</tt>.
	 * 
	 * @return the last node of node chain.
	 */
	@Nonnull
	default INode<T> getEnd()
	{
		INode<T> node = this;
		while (hasNext())
			node = next();
		return node;
	}
	
	/**
	 * Get if the node chain contain the element.
	 * 
	 * @param arg the checking element.
	 * @return <tt>true</tt> if this node chain contains element and
	 *         <tt>false</tt> for otherwise.
	 * @see #value()
	 */
	default boolean contain(Object arg)
	{
		return L.equals(arg, value()) || (containBefore(arg) || containAfter(arg));
	}
	
	/**
	 * Get if node before (exclude this node) current node has the element.
	 * 
	 * @param arg the checking element.
	 * @return <tt>true</tt> if nodes before this contain element and
	 *         <tt>false</tt> for otherwise.
	 * @see #contain(Object)
	 * @see #containAfter(Object)
	 */
	default boolean containBefore(Object arg)
	{
		if (!hasLast()) return false;
		INode<T> node = last();
		return L.equals(arg, node.value()) || node.containBefore(arg);
	}
	
	/**
	 * Get if node after (exclude this node) current node has the element.
	 * 
	 * @param arg the checking element.
	 * @return <tt>true</tt> if nodes after this contain element and
	 *         <tt>false</tt> for otherwise.
	 * @see #contain(Object)
	 * @see #containBefore(Object)
	 */
	default boolean containAfter(Object arg)
	{
		if (!hasNext()) return false;
		INode<T> node = next();
		return L.equals(arg, node.value()) || node.containAfter(arg);
	}
	
	/**
	 * Take <tt>cut</tt> action.
	 * <p>
	 * Split the node chain to two chain, which one is started from next node at
	 * the position called <tt>cutNext</tt> method.
	 * 
	 * @return the second node chain which contain the <tt>next</tt> node
	 *         before, it will start with <tt>next</tt> node first.
	 */
	INode<T> cutNext();
	
	/**
	 * Take <tt>cut</tt> action.
	 * <p>
	 * Split the node chain to two chain, which one is ended from last node at
	 * the position called <tt>cutLast</tt> method.
	 * 
	 * @return the second node chain which contain the <tt>last</tt> node
	 *         before, it will at the ending with <tt>last</tt> node.
	 */
	INode<T> cutLast();
	
	/**
	 * Return first matched element by
	 * {@link java.util.function.Predicate#test(Object)} in this node chain.
	 * 
	 * @param p the matching function.
	 * @return the matched target.
	 */
	default INode<T> find(Predicate<T> p)
	{
		INode<T> result;
		return p.test(value()) ? this : (result = findBefore(p)) != null ? result : findAfter(p);
	}
	
	/**
	 * Return first matched element after this node.
	 * 
	 * @param p the matching function.
	 * @return the matched target.
	 * @see #find(Predicate)
	 */
	default INode<T> findAfter(Predicate<T> p)
	{
		if (!hasNext()) return null;
		INode<T> node = next();
		return p.test(node.value()) ? node : node.findAfter(p);
	}
	
	/**
	 * Return first matched element before this node.
	 * 
	 * @param p the matching function.
	 * @return the matched target.
	 * @see #find(Predicate)
	 */
	default INode<T> findBefore(Predicate<T> judgable)
	{
		if (!hasLast()) return null;
		INode<T> node = last();
		return judgable.test(node.value()) ? node : node.findBefore(judgable);
	}
	
	/**
	 * Add a new node at the start of node chain.
	 * 
	 * @param target The target will contain in new node.
	 * @throws nsupportedOperationException If this node is immutable node
	 *             chain.
	 */
	default void addLast(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Add nodes at end of node chain.
	 * 
	 * @param iterator
	 * @throws UnsupportedOperationException If this node is immutable node
	 *             chain.
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
	 * 
	 * @param iterator
	 * @throws java.lang.UnsupportedOperationException If this node is immutable
	 *             node chain.
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
	 * 
	 * @param target
	 */
	default void insertAfter(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Insert a new node between this node and last node.
	 * 
	 * @param target
	 */
	default void insertBefore(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Add a new node at the end of node chain.
	 * 
	 * @param target The target will contain in new node.
	 * @throws UnsupportedOperationException If this node chain is immutable
	 *             node chain.
	 */
	default void addNext(T target)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Remove this node from node chain.
	 * <p>
	 * The removed node will not be present in node chain, the {@link #next()}
	 * and {@link #last()} will return <tt>null</tt> after method is called.
	 * <p>
	 * To get removed node chain, you need use a field to cached node chain
	 * before node has been removed or use {@link #removeNext()} or
	 * {@link #removeLast()} instead.
	 * 
	 * @see #removeLast()
	 * @see #removeNext()
	 * @throws UnsupportedOperationException if this node chain is immutable
	 *             node chain.
	 */
	default T remove()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Remove next node from node chain.
	 * 
	 * @return the value at removed node.
	 * @see #remove()
	 * @throws IllegalStateException if next node isn't present.
	 */
	default T removeNext()
	{
		if (!hasNext()) throw new IllegalStateException();
		INode<T> node = next();
		return node.remove();
	}
	
	/**
	 * Remove last node from node chain.
	 * 
	 * @return the value at removed nod.
	 * @see #remove()
	 * @throws IllegalStateException if last node isn't present.
	 */
	default T removeLast()
	{
		if (!hasLast()) throw new IllegalStateException();
		INode<T> node = last();
		return node.remove();
	}
	
	/**
	 * Return array copied all elements after this node.
	 * 
	 * @param clazz the array type.
	 * @return the array.
	 */
	default T[] toArray(Class<T> clazz)
	{
		return Iterators.toArray(iterator(), clazz);
	}
	
	/**
	 * Return the hashcode of <b>node chain</b>.
	 * <p>
	 * Consider the hashcode of node chain and position, use for calculation:
	 * <code>
	 * 
	 * </code>
	 * 
	 * @return the hashcode of node.
	 * @see Object#hashCode()
	 */
	int hashCode();
	
	/**
	 * Check is two <b>node chains</b> are same.
	 * <p>
	 * Return <tt>true</tt> if two node chains contains same elements and the
	 * position on node chain are equal.
	 * 
	 * @param obj the matching object.
	 * @return <tt>true</tt> if two node chain and node position are equal.
	 * @see Object#equals(Object)
	 */
	boolean equals(Object obj);
	
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
			if (currentNode.hasNext())
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
			if (currentNode.hasLast())
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
