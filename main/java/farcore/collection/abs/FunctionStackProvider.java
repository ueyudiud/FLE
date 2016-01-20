package farcore.collection.abs;

import com.google.common.base.Function;

public abstract class FunctionStackProvider<S, T>
		extends ReflectStackProvider<S, T>
{
	Function<Stack<T>, S> function;
	
	public FunctionStackProvider(Function<Stack<T>, S> function)
	{
		this.function = function;
		
	}
	
	@Override
	public S make(T target, long size)
	{
		return function.apply(new Stack<T>(target, size));
	}
	
	@Override
	public boolean equal(T arg1, T arg2)
	{
		return arg1 == null || arg2 == null ? arg1 == arg2 : arg1.equals(arg2);
	}
	
	@Override
	public boolean equals(S arg1, S arg2)
	{
		return arg1 == null || arg2 == null ? arg1 == arg2 : arg1.equals(arg2);
	}
	
	@Override
	public S copy(S stack)
	{
		return function.apply(new Stack(getObj(stack), getSize(stack)));
	}
}