package flapi.collection.abs;

public class Stack<T> implements IStack<T>
{
	public T obj;
	public int size;

	public Stack(T target)
	{
		this(target, 1);
	}
	public Stack(T target, int size)
	{
		this.obj = target;
		this.size = size;
	}
	
	public boolean isEqual(Stack stack)
	{
		return obj == null || stack.obj == null ? obj == stack.obj : obj.equals(stack.obj);
	}
	
	public boolean isSame(Stack stack)
	{
		return isEqual(stack) ? stack.size == size : false;
	}
	
	public boolean isContain(Stack stack)
	{
		return isEqual(stack) ? stack.size <= size : false;
	}
	
	@Override
	public String toString()
	{
		return obj.toString() + "x" + size;
	}
	
	@Override
	public int hashCode()
	{
		return obj.hashCode() * 591769 + size * 17491;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Stack ? isEqual((Stack) obj) : false;
	}
	
	@Override
	public Stack clone()
	{
		return new Stack(obj, size);
	}
	
	public Stack copy(int size)
	{
		return new Stack(obj, size);
	}
	
	//=======================================IStack Part==================================
	
	@Override
	public T get()
	{
		return obj;
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public Stack copy()
	{
		return clone();
	}
}