/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Register type, contain a register list with a integer id and a string id (or
 * 'name') for each element.
 * <p>
 * This target collection is implements {@link java.lang.Iterable}, for you can
 * use it in for-each loop directly.
 * 
 * @author ueyudiud
 * @param <T> the element type in register.
 * @see java.lang.Iterable
 * @see nebula.base.Register
 */
public interface IRegister<T> extends Iterable<T>
{
	/**
	 * Register a new element.
	 * 
	 * @param name the registered name.
	 * @param arg the registered argument.
	 * @return the id of registered argument.
	 */
	int register(@Nonnull String name, @Nonnull T arg);
	
	/**
	 * Register a new element with selected id.
	 * 
	 * @param id the id of registered argument.
	 * @param name the registered name.
	 * @param arg the registered argument.
	 */
	void register(int id, @Nonnull String name, @Nonnull T arg);
	
	/**
	 * Get element id, if element contain two element, get first id.
	 * 
	 * @param arg
	 * @return The id of element, <code>-1</code> means the element is not
	 *         present in register.
	 */
	int id(@Nonnull T arg);
	
	/**
	 * Get element id from name.
	 * 
	 * @param name
	 * @return The id of element with select name, <code>-1</code> means the
	 *         element is not present in register.
	 */
	int id(@Nonnull String name);
	
	/**
	 * Get name of element.
	 * 
	 * @param arg the element.
	 * @return the name.
	 */
	String name(@Nonnull T arg);
	
	/**
	 * Get name of element from id.
	 * 
	 * @param id
	 * @return
	 * @see #name(Object)
	 */
	String name(int id);
	
	/**
	 * Get element with select name, if it is not present, give bake default
	 * one.
	 * 
	 * @param name
	 * @param def the default element return.
	 * @return the element with name registered, or <tt>def</tt> when no element
	 *         with name found.
	 * @see #get(String)
	 */
	default T get(@Nonnull String name, @Nullable T def)
	{
		T target;
		return (target = get(name)) != null ? target : def;
	}
	
	/**
	 * Get element with select name.
	 * 
	 * @param name the name of element.
	 * @return the element with name registered, or <tt>null</tt> if no element
	 *         with name found.
	 */
	@Nullable
	T get(@Nonnull String name);
	
	/**
	 * Get element with select id, if it is not present, give bake default
	 * one.
	 * 
	 * @param id the element id.
	 * @param def the default element return.
	 * @return the element with id registered, or <tt>def</tt> when no element
	 *         with id found.
	 * @see #get(int)
	 */
	default T get(int id, @Nullable T def)
	{
		T target;
		return (target = get(id)) != null ? target : def;
	}
	
	/**
	 * Get element by id.
	 * @param id the id of element.
	 * @return the element with id registered, or <tt>null</tt> if no element found.
	 */
	T get(int id);
	
	/**
	 * Get all targets in register.
	 * 
	 * @return
	 */
	Set<T> targets();
	
	/**
	 * Get all registry names set.
	 * 
	 * @return
	 */
	Set<String> names();
	
	/**
	 * Return <code>true</code> if id is present in register.
	 * @param id the register id.
	 * @return
	 */
	default boolean contain(int id)
	{
		return get(id) != null;
	}
	
	/**
	 * Return <code>true</code> if name is present in register.
	 * @param name the register name.
	 * @return
	 */
	default boolean contain(@Nonnull String name)
	{
		return get(name) != null;
	}
	
	/**
	 * Return <tt>true</tt> if this register contain the argument.
	 * 
	 * @param arg
	 * @return
	 */
	default boolean contain(@Nonnull T arg)
	{
		return name(arg) != null;
	}
	
	/**
	 * Remove an element from register by name.
	 * 
	 * @param name the name of element.
	 * @return the removed element value.
	 */
	@Nullable
	T remove(String name);
	
	/**
	 * Remove an element from register.
	 * 
	 * @param arg the remove element.
	 * @return the element registered name.
	 */
	@Nullable
	String remove(T arg);
	
	/**
	 * Give the element size of register.
	 * 
	 * @return the size
	 */
	int size();
	
	/**
	 * Get (name, element) entry stream.
	 * @return
	 */
	Stream<Entry<String, T>> entryStream();
	
	/**
	 * Get the elements stream.
	 * @return
	 */
	Stream<T> stream();
	
	/**
	 * Return the hashcode of register. For all value registered in entry, the
	 * hashcode is sum of <tt>id ^ name.hashcode() ^ hashCode(value)</tt>
	 * 
	 * @return the hashcode of register.
	 * @see Object#hashCode()
	 */
	int hashCode();
	
	/**
	 * Return the register is same to another register (or just an object).
	 * 
	 * @see Object#equals(Object)
	 */
	boolean equals(Object obj);
}
