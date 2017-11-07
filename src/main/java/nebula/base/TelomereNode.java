package nebula.base;

import java.util.Objects;

class TelomereNode<T> implements INode<T>
{
	final INode<T> next;
	
	TelomereNode(INode<T> node)
	{
		this.next = Objects.requireNonNull(node);
	}
	
	@Override
	public INode<T> cutLast()
	{
		return null;
	}
	
	@Override
	public INode<T> cutNext()
	{
		return this.next;
	}
	
	@Override
	public T value()
	{
		return null;
	}
	
	@Override
	public INode<T> next()
	{
		return this.next;
	}
	
	@Override
	public boolean hasNext()
	{
		return true;
	}
	
	@Override
	public boolean hasLast()
	{
		return false;
	}
	
	@Override
	public INode<T> last()
	{
		return null;
	}
}
