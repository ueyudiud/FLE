/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.IStringSerializable;

/**
 * @author ueyudiud
 */
public class Properties
{
	private static final Map<Class<?>, PropertyEnum<?>> PROPERTIES = new HashMap();
	
	/**
	 * Get a enumeration property.
	 * @param enumClass
	 * @return
	 */
	public static <E extends Enum<E> & IStringSerializable> PropertyEnum<E> get(Class<E> enumClass)
	{
		if (PROPERTIES.containsKey(enumClass)) return (PropertyEnum<E>) PROPERTIES.get(enumClass);
		EnumStateName name = enumClass.getAnnotation(EnumStateName.class);
		if (name == null) throw new IllegalArgumentException("The enum class does not contain a state name, check if a EnumStateName annotation is presented!");
		PropertyEnum<E> property = PropertyEnum.create(name.value(), enumClass);
		PROPERTIES.put(enumClass, property);
		return property;
	}
	
	public static PropertyInteger create(String name, int min, int max)
	{
		return PropertyInteger.create(name, min, max);
	}
	
	public static PropertyBool create(String name)
	{
		return PropertyBool.create(name);
	}
	
	/**
	 * The state annotation, mark on Enum class and provide auto property.
	 * @author ueyudiud
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public static @interface EnumStateName
	{
		/**
		 * The name of enum property key.
		 * @return
		 */
		String value();
	}
	
	/**
	 * The property injection will be given if it is on a enum property.<p>
	 * It dose not take effect now!<p>
	 * XXX
	 * @author ueyudiud
	 */
	@Deprecated
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public static @interface PropertyInject
	{
	}
}