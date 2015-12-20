package flapi.collection;

import java.util.Map;

import flapi.collection.abs.IStackProvider;
import flapi.collection.abs.Stack;

public class ArrayStandardStackList<T> extends ArrayStackList<Stack<T>, T>
{
	public ArrayStandardStackList()
	{
		this(16);
	}
	public ArrayStandardStackList(int size)
	{
		this(size, 0.25F);
	}
	public ArrayStandardStackList(int size, float scale)
	{
		super(new StackProvider<T>(), size, scale);
	}
	public ArrayStandardStackList(Stack<T>...stacks)
	{
		this(stacks.length);
		list = stacks;
		species = stacks.length;
		for(Stack<T> stack : stacks)
			size += stack.size;
	}
	public ArrayStandardStackList(Map<T, Integer> map)
	{
		this(map.size());
		list = CollectionUtil.asArray(map);
		species = map.size();
		for(Integer i : map.values())
			size += i;
	}
	
	private static class StackProvider<T> implements IStackProvider<Stack<T>, T>
	{
		@Override
		public Stack<T>[] newArray(int size)
		{
			return new Stack[size];
		}
		
		@Override
		public Stack<T> make(T target, int size)
		{
			return new Stack(target, size);
		}

		@Override
		public T getObj(Stack<T> stack)
		{
			return stack == null ? null : stack.obj;
		}

		@Override
		public int getSize(Stack<T> stack)
		{
			return stack == null ? 0 : stack.size;
		}

		@Override
		public boolean equal(T arg1, T arg2)
		{
			return arg1 == null || arg2 == null ? arg1 == arg2 : arg1.hashCode() == arg2.hashCode() && arg1.equals(arg2);
		}

		@Override
		public boolean equals(Stack<T> arg1, Stack<T> arg2)
		{
			return arg1 == null || arg2 == null ? arg1 == arg2 : arg1.isEqual(arg2);
		}

		@Override
		public boolean contain(Stack<T> arg, T target)
		{
			return arg == null ? target == null : equal(arg.obj, target);
		}

		@Override
		public boolean contains(Stack<T> arg, Stack<T> targets)
		{
			return arg == null ? targets == null : arg.isContain(targets);
		}

		@Override
		public void add(Stack<T> stack, int size)
		{
			stack.size += size;
		}
		
		@Override
		public Stack<T> decr(Stack<T> stack, int size)
		{
			if(stack == null) return null;
			int s = Math.min(size, stack.size);
			stack.size -= s;
			return new Stack(stack.obj, s);
		}

		@Override
		public void setSize(Stack<T> stack, int size)
		{
			stack.size = size;
		}

		@Override
		public Stack<T> copy(Stack<T> stack)
		{
			return stack != null ? stack.copy() : null;
		}
	}
}