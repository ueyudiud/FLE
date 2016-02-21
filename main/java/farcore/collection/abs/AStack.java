package farcore.collection.abs;

public interface AStack<T>
{
	boolean equal(T arg);
	
	boolean equal(AStack<T> arg);
	
	boolean contain(AStack<T> arg);
	
	T[] toList();
}