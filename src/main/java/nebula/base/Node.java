package nebula.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import nebula.common.util.L;

public class Node<T> implements INode<T>
{
	/**
	 * Build the first node of nodes.
	 * 
	 * @param target
	 * @return
	 */
	public static <T> Node<T> first(@Nullable T target)
	{
		return new Node<>(target);
	}
	
	public static <T> Node<T> chain(T...ts)
	{
		switch (ts.length)
		{
		case 0:
			return null;
		case 1:
			return new Node<>(ts[0]);
		default:
			Node<T> result = new Node<>(ts[0]);
			Node<T> n1 = result;
			for (int i = 1; i < ts.length; ++i)
			{
				n1.addNext(ts[i]);
				n1 = n1.next;
			}
			return result;
		}
	}
	
	public static <T> Node<T> chain(List<T> list)
	{
		switch (list.size())
		{
		case 0:
			return null;
		case 1:
			return new Node<>(list.get(0));
		default:
			Node<T> result = new Node<>(list.get(0));
			Node<T> n1 = result;
			for (int i = 1; i < list.size(); ++i)
			{
				n1.addNext(list.get(i));
				n1 = n1.next;
			}
			return result;
		}
	}
	
	/**
	 * Create a new node chain.
	 * <p>
	 * Return the first node of node chain.
	 * 
	 * @param iterable the object to provide iterator.
	 * @return the first node.
	 * @see #chain(Iterator)
	 */
	public static <T> Node<T> chain(Iterable<T> iterable)
	{
		if (iterable instanceof List && !(iterable instanceof LinkedList))// The
																			// iterator
																			// for
																			// linked
																			// list
																			// is
																			// faster
																			// than
																			// index
																			// positioned.
		{
			return chain((List<T>) iterable);
		}
		else
		{
			return chain(iterable.iterator());
		}
	}
	
	/**
	 * Create a new node chain.
	 * <p>
	 * Return the first node of node chain.
	 * 
	 * @param iterator the iterator to provider node chain.
	 * @return the first node.
	 */
	public static <T> Node<T> chain(Iterator<T> iterator)
	{
		if (!iterator.hasNext()) return null;
		Node<T> node = new Node<>(iterator.next()), result = node;
		while (iterator.hasNext())
		{
			node.addNext(iterator.next());
			node = node.next;
		}
		return result;
	}
	
	private T		target;
	private Node<T>	next;
	private Node<T>	last;
	
	Node(T target)
	{
		this.target = target;
	}
	
	@Override
	public T value()
	{
		return this.target;
	}
	
	@Override
	public Node<T> next()
	{
		return this.next;
	}
	
	@Override
	public Node<T> last()
	{
		return this.last;
	}
	
	@Override
	public void addLast(T target)
	{
		if (this.last != null)
		{
			this.last.addLast(target);
		}
		else
		{
			this.last = new Node<>(target);
			this.last.next = this;
		}
	}
	
	@Override
	public void addNext(T target)
	{
		if (this.next != null)
		{
			this.next.addNext(target);
		}
		else
		{
			this.next = new Node<>(target);
			this.next.last = this;
		}
	}
	
	@Override
	public void insertAfter(T target)
	{
		Node<T> node = this.next;
		this.next = new Node<>(target);
		this.next.last = this;
		if (node != null)
		{
			node.last = this.next;
			this.next.next = node;
		}
	}
	
	@Override
	public void insertBefore(T target)
	{
		Node<T> node = this.last;
		this.last = new Node<>(target);
		this.last.next = this;
		if (node != null)
		{
			node.next = this.last;
			this.last.last = node;
		}
		;
	}
	
	@Override
	public T remove()
	{
		if (this.last != null)
		{
			this.last.next = this.next;
		}
		if (this.next != null)
		{
			this.next.last = this.last;
		}
		this.next = this.last = null;
		return this.target;
	}
	
	@Override
	public INode<T> cutNext()
	{
		if (this.next == null) return null;
		this.next.last = null;
		INode<T> result = this.next;
		this.next = null;
		return result;
	}
	
	@Override
	public INode<T> cutLast()
	{
		if (this.last == null) return null;
		this.last.next = null;
		INode<T> result = this.last;
		this.last = null;
		return result;
	}
	
	@Override
	public int hashCode()
	{
		int hash = Objects.hashCode(this.target);
		
		Node<T>
		
		node = this;
		while (node.hasLast())
		{
			node = node.last();
			hash *= 31;
			hash += Objects.hashCode(node.target);
		}
		
		hash *= 31;
		node = this;
		while (node.hasNext())
		{
			node = node.next();
			hash *= 31;
			hash += Objects.hashCode(node.target);
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (!(obj instanceof INode<?>)) return false;
		
		INode<?> node = (INode<?>) obj;
		if (!L.equal(value(), node.value())) return false;
		
		INode<?>
		
		node1 = this, node2 = node;
		while (node1.hasLast() || node2.hasLast())
		{
			if (node1.hasLast() != node2.hasLast() || !L.equal((node1 = node1.last()).value(), (node2 = node2.last()).value())) return false;
		}
		
		node1 = this;
		node2 = node;
		while (node1.hasNext() || node2.hasNext())
		{
			if (node1.hasNext() != node2.hasNext() || !L.equal((node1 = node1.next()).value(), (node2 = node2.next()).value())) return false;
		}
		
		return true;
	}
}
