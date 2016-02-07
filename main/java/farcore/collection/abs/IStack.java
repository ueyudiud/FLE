package farcore.collection.abs;

public interface IStack<T>
{
	T get();
	
	long size();
	
	IStack copy();
}