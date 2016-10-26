package farcore.lib.collection;

import java.util.Iterator;

import farcore.util.U;

public interface INode<T>
{
	/**
	 * Make a new empty node, added before this node.
	 * (Could not get this node from source node)
	 * @param node The node need added.
	 * @return The empty telomere node.
	 */
	public static <T> INode<T> telomereNode(INode<T> node)
	{
		return new TelomereNode(node);
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
		return U.L.equal(arg, value()) || (containBefore(arg) || containAfter(arg));
	}
	
	default boolean containBefore(Object arg)
	{
		if(!hasLast()) return false;
		INode<T> node = last();
		return U.L.equal(arg, node.value()) || node.containBefore(arg);
	}

	default boolean containAfter(Object arg)
	{
		if(!hasNext()) return false;
		INode<T> node = next();
		return U.L.equal(arg, node.value()) || node.containAfter(arg);
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
}