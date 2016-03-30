package farcore.lib.collection;

public interface IProperty<T, O>
{
	/**
	 * Get property name.
	 * @return
	 */
	String name();
	
	/**
	 * Get target from object.
	 * @param object
	 * @return
	 */
	T get(O object);
	
	/**
	 * Set target to object.
	 * @param target
	 * @return
	 */
	O set(T target);
	
	/**
	 * Return default value.
	 * @return
	 */
	T def();
}