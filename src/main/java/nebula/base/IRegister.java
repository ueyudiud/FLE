package nebula.base;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Register type, contain a register list with a integer id
 * and a string id for each element.<p>
 * This target collection is implements {@link java.lang.Iterable},
 * for you can use it in for-each loop directly.
 * @author ueyudiud
 * @param <T> the element type in regiser.
 * @see java.lang.Iterable
 * @see nebula.base.Register
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
	
	/**
	 * Get name of element.
	 * @param arg the element.
	 * @return the name.
	 */
	String name(T arg);
	
	/**
	 * Get name of element from id.
	 * @param id
	 * @return
	 * @see #name(Object)
	 */
	String name(int id);
	
	/**
	 * Get element with select name, if it is not present, give bake default one.
	 * @param name
	 * @param def the default element return.
	 * @return the element with name registered, or <tt>def</tt> when no element with name found.
	 * @see #get(String)
	 */
	default T get(String name, @Nullable T def)
	{
		T target;
		return (target = get(name)) != null ? target : def;
	}
	
	/**
	 * Get element with select name.
	 * @param name the name of element.
	 * @return the element with name registered, or <tt>null</tt> if no element with name found.
	 */
	@Nullable
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
	 * @return the size
	 */
	int size();
}