package farcore.lib.collection;

import java.util.List;
import java.util.Set;

public interface IRegister<T> extends Iterable<T>
{
	int register(String name, T arg);
	
	void register(int id, String name, T arg);
	
	int id(T arg);
	
	int id(String name);
	
	String name(T arg);
	
	String name(int id);
	
	T get(String name, T def);
	
	T get(String name);
	
	T get(int id, T def);
	
	T get(int id);
	
	List<T> targets();
	
	Set<String> names();
	
	boolean contain(String name);
	
	boolean contain(int id);
	
	boolean contain(T arg);
	
	T remove(String name);
	
	String remove(T arg);
}