package nebula.base;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Register type, contain a register list with a integer id
 * and a string id for each element.<p>
 * @author ueyudiud
 *
 * @param <T> The element type in regiser.
 */
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
	
	/**
	 * Get element id from name.
	 * @param name
	 * @return The id of element with select name, -1 means the element is not present in register.
	 */
	int id(String name);
	
	String name(T arg);
	
	String name(int id);
	
	/**
	 * Get element with select name, if it is not present, give bake default one.
	 * @param name
	 * @param def
	 * @return
	 */
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
	
	/**
	 * Get all targets in register.
	 * @return
	 */
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
	
	/**
	 * May not use any more.
	 */
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