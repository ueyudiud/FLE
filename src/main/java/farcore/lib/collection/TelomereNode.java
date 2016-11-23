package farcore.lib.collection;

import java.util.Objects;

class TelomereNode<T> implements INode<T>
{
	final INode<T> next;

	TelomereNode(INode<T> node)
	{
		next = Objects.requireNonNull(node);
	}
	
	@Override
	public T value() { return null; }
	
	@Override
	public INode<T> next() { return next; }
	
	@Override
	public boolean hasNext() { return true; }

	@Override
	public boolean hasLast() { return false; }

	@Override
	public INode<T> last() { return null; }
}