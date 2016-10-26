package farcore.lib.collection;

class TelomereNode<T> implements INode<T>
{
	final INode<T> next;
	
	TelomereNode(INode<T> node)
	{
		if(node == null) throw new IllegalArgumentException("The node can not be null!");
		next = node;
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