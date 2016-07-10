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
	
	default T get(String name, T def)
	{
		T target;
		return (target = get(name)) != null ? target : def;
	}
	
	T get(String name);
	
	default T get(int id, T def)
	{
		T target;
		return (target = get(id)) != null ? target : def;
	}
	
	T get(int id);
	
	List<T> targets();
	
	Set<String> names();
	
	default boolean contain(String name)
	{
		return get(name) != null;
	}
	
	default boolean contain(int id)
	{
		return get(id) != null;
	}
	
	default boolean contain(T arg)
	{
		return name(arg) != null;
	}
	
	T remove(String name);
	
	/**
	 * Remove an element from register.
	 * @param arg The remove element.
	 * @return The element registered name.
	 */
	String remove(T arg);

	/**
	 * Give the element size of register.
	 * @return
	 */
	int size();
	
	/**
	 * Arrange and remove empty slot,
	 * give all element new id by name.
	 */
	void arrange();
	
	/**
	 * Arrange elements, the first is
	 * strings name contains, and
	 * move other not contain in this
	 * element to other id.
	 * @param list The name serialized list.
	 */
	void arrange(String...list);
}