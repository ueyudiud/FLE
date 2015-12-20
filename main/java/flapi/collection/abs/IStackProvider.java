package flapi.collection.abs;

public interface IStackProvider<S, T>
{
	S[] newArray(int size);
	
	S make(T target, int size);
	
	T getObj(S stack);
	
	int getSize(S stack);
	
	boolean equal(T arg1, T arg2);

	boolean equals(S arg1, S arg2);
	
	boolean contain(S arg, T target);
	
	boolean contains(S arg, S targets);
	
	void add(S stack, int size);
	
	S decr(S stack, int size);
	
	void setSize(S stack, int size);
	
	S copy(S stack);
}