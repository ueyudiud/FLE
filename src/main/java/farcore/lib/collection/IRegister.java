package farcore.lib.collection;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRegister<T> extends Iterable<T>
{
	/**
	 * Register a new element.
	 * @param name
	 * @param arg
	 * @return
	 */
	int register(@Nonnull String name, @Nonnull T arg);
	
	/**
	 * Register a new element with selected id.
	 * @param id
	 * @param name
	 * @param arg
	 */
	void register(int id, @Nonnull String name, @Nonnull T arg);
	
	/**
	 * Get element id, if element contain two element, get first id.
	 * @param arg
	 * @return The id of element, -1 means the element is not present in register.
	 */
	int id(T arg);
	
	int id(String name);
	
	String name(T arg);
	
	String name(int id);
	
	default T get(String name, @Nullable T def)
	{
		T target;
		return (target = get(name)) != null ? target : def;
	}
	
	T get(String name);
	
	default T get(int id, @Nullable T def)
	{
		T target;
		return (target = get(id)) != null ? target : def;
	}
	
	T get(int id);
	
	Collection<T> targets();
	
	Set<String> names();
	
	default boolean contain(int id)
	{
		return get(id) != null;
	}
	
	default boolean contain(@Nonnull String name)
	{
		return get(name) != null;
	}
	
	default boolean contain(@Nonnull T arg)
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
	
	//	/**
	//	 * Arrange and remove empty slot,
	//	 * give all element new id by name.
	//	 */
	//	@Deprecated
	//	void arrange();
	//
	//	/**
	//	 * Arrange elements, the first is
	//	 * strings name contains, and
	//	 * move other not contain in this
	//	 * element to other id.
	//	 * @param list The name serialized list.
	//	 */
	//	@Deprecated
	//	void arrange(String...list);
}