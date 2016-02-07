package farcore.collection.abs;

import com.google.gson.annotations.SerializedName;

public class Stack<T> implements IStack<T>
{
	@SerializedName("target")
	public T obj;
	public long size;

	public Stack(T target)
	{
		this(target, 1);
	}
	public Stack(T target, long size)
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
		return (int) (obj.hashCode() * 591769 + size * 17491);
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
	public long size()
	{
		return size;
	}
	
	@Override
	public Stack copy()
	{
		return clone();
	}
}