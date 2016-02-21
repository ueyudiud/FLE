package farcore.collection.abs;

public interface IRegister<T> extends Iterable<T>
{
	int register(T target, String name);
	void register(int id, T target, String name);

	int serial(T t);
	int serial(String name);
	
	T get(int id);
	T get(String name);
	
	String name(int id);
	String name(T t);
	
	boolean contain(String name);
	boolean contain(int id);
	
	boolean remove(int id);
	
	boolean empty();
	
	String[] keySet();
}