package flapi.collection.abs;

public interface IStack<T>
{
	T get();
	
	int size();
	
	IStack copy();
}