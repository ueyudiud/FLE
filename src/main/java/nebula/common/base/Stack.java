/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.base;

/**
 * The stack instance.<p>
 * With a element*size type.
 * @author ueyudiud
 *
 * @param <E>
 */
public class Stack<E>
{
	public E element;
	public long size;
	
	public Stack(E element)
	{
		this(element, 1L);
	}
	public Stack(E element, long size)
	{
		this.element = element;
		this.size = size;
	}
	
	public boolean same(Stack stack)
	{
		return stack.element.equals(this.element);
	}
	
	public Stack<E> split(long size)
	{
		Stack<E> stack = new Stack(this.element, size);
		this.size -= size;
		return stack;
	}
	
	@Override
	public Stack<E> clone()
	{
		return new Stack<>(this.element, this.size);
	}
	
	@Override
	public String toString()
	{
		return "(" + this.element + ")x" + this.size;
	}
	
	@Override
	public int hashCode()
	{
		return this.element.hashCode() * 31 + Long.hashCode(this.size);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == this ? true :
			!(obj instanceof Stack) ? false :
				((Stack) obj).element.equals(this.element) && ((Stack) obj).size == this.size;
	}
}