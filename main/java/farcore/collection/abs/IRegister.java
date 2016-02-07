package farcore.collection.abs;

import java.util.Collection;
import java.util.Set;

/**
 * Register list, this is not a collection, but a iterator of target.<br>
 * Use to register objects with name and serial. The serial provide
 * a integer value for key of object.
 * You can get object by name of registered or serial.<br>
 * The name, serial, target can be as a key to get value each other.
 * Use<br>
 * <code>
 * T target = new Object();<br>
 * register.register(target, "yourTargetRegisterName");<br>
 * </code>
 * to register a object to register without designed a serial.
 * @author ueyudiud
 * @see farcore.collection.Register
 * @param <T> The target type.
 */
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
	
	int size();
	
	/**
	 * Get as a collection.<br>
	 * Add in collection is forbidden, but you can remove, check contain in
	 * collection type.<br>
	 * The collection type has same elements with register, 
	 * use {@code list.remove(name)} will also effect to register elements.
	 * If you need create a set but contain a different list instance from 
	 * register, use {@code register.keySet()}.
	 * @return A collection instance.
	 */
	Collection<String> asNameCollection();
	
	/**
	 * A target collection. See asNameCollection.
	 * @return
	 */
	Collection<T> asCollection();
	
	/**
	 * Get all of registered name.
	 * @return
	 */
	Set<String> keySet();
	
	/**
	 * Get all of target.
	 * @return The target list, exclude null.
	 */
	Set<T> targetSet();
}