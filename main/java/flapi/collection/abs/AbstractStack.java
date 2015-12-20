package flapi.collection.abs;


public interface AbstractStack<T>
{
	boolean equal(T arg);
	
	boolean equal(AbstractStack<T> arg);
	
	boolean contain(AbstractStack<T> arg);
	
	T[] toList();
}