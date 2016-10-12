package farcore.lib.collection;

public class Node<T> implements INode<T>
{
	public static <T> Node<T> first(T target)
	{
		return new Node<T>(target);
	}

	private T target;
	private Node<T> next;
	private Node<T> last;

	Node(T target)
	{
		this.target = target;
	}

	@Override
	public T value()
	{
		return target;
	}
	
	@Override
	public Node<T> next()
	{
		return next;
	}
	
	@Override
	public Node<T> last()
	{
		return last;
	}

	@Override
	public void addLast(T target)
	{
		if(last != null)
		{
			last.addLast(target);
		}
		else
		{
			last = new Node<T>(target);
		}
	}

	@Override
	public void addNext(T target)
	{
		if(next != null)
		{
			next.addNext(target);
		}
		else
		{
			next = new Node<T>(target);
		}
	}
}