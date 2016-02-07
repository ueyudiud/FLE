package farcore.collection.abs;

public interface IStackProvider<S, T>
{
	S[] newArray(int size);
	
	S make(T target, long size);
	
	T getObj(S stack);
	
	long getSize(S stack);
	
	boolean equal(T arg1, T arg2);

	boolean equals(S arg1, S arg2);
	
	boolean contain(S arg, T target);
	
	boolean contains(S arg, S targets);
	
	void add(S stack, long size);
	
	S decr(S stack, long size);
	
	void setSize(S stack, long size);
	
	S copy(S stack);
}