package farcore.lib.collection;

import farcore.util.U;

public interface INode<T>
{
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

	default void addNext(T target)
	{
		throw new UnsupportedOperationException();
	}
}