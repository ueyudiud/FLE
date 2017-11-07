/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.NebulaLoadingPlugin;

/**
 * Reflection helper.
 * 
 * @author ueyudiud
 */
public final class R
{
	private R()
	{
	}
	
	static final Map<String, Field>	FIELD_CACHE	= new HashMap();
	private static Field			modifiersField;
	
	public static void resetFieldCache()
	{
		FIELD_CACHE.clear();
	}
	
	private static void initModifierField()
	{
		try
		{
			if (modifiersField == null)
			{
				modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
			}
		}
		catch (Throwable e)
		{
			throw new InternalError(e);
		}
	}
	
	private static Field getField(Class<?> clazz, String mcpName, String obfName, boolean isPrivate, boolean isFinal, boolean alwaysInit) throws ReflectiveOperationException
	{
		if (isFinal)
		{
			initModifierField();
		}
		String key = NebulaLoadingPlugin.runtimeDeobf ? obfName : mcpName;
		String key1 = clazz.getName() + "|" + key;
		if (!alwaysInit && FIELD_CACHE.containsKey(key1)) return FIELD_CACHE.get(key1);
		try
		{
			Field field = isPrivate ? clazz.getDeclaredField(key) : clazz.getField(key);
			if (isFinal)// Remove final modifier.
				modifiersField.setInt(field,
						field.getModifiers() & 0xFFFFFFEF/** ~Modifier.FINAL */
				);
			if (field != null)
			{
				field.setAccessible(true);
				FIELD_CACHE.put(key1, field);
				return field;
			}
		}
		catch (ReflectiveOperationException exception)
		{
			throw exception;
		}
		throw new NoSuchFieldException();
	}
	
	public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable F override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		overrideField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
	}
	
	public static <T, F> void overrideField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, @Nullable F override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		try
		{
			getField(clazz, mcpName, obfName, isPrivate, false, alwaysInit).set(target, override);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable F override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		overrideFinalField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
	}
	
	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, @Nullable F override, boolean isPrivate, boolean alwaysInit)
	{
		try
		{
			getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).set(target, override);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, int override, boolean isPrivate, boolean alwaysInit)
	{
		try
		{
			getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).setInt(target, override);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	/**
	 * Get specific field from class.
	 * 
	 * @param clazz the class type which field are at.
	 * @param mcpName the field name running in MCP environment.
	 * @param obfName the field name running in obf environment.
	 * @param target the value to get field, input <code>null</code> when
	 *            getting <tt>static</tt> field.
	 * @param alwaysInit the ReflectionHelper will cache the field to list if
	 *            input <code>true</code>, enable this option if this field is
	 *            usually be called.
	 * @param <T> the target type, for you need try to use
	 *            <code>instanceof</code> to check if <tt>target</tt> can be
	 *            cast to this type.
	 * @param <V> the return value type.
	 * @return the value of field.
	 * @throws InternalError when field is illegal or does not has access to
	 *             visit.
	 */
	public static <T, V> V getValue(@Nonnull Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, boolean alwaysInit)
	{
		try
		{
			return (V) getField(clazz, mcpName, obfName, true, false, alwaysInit).get(target);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static <T> int getInt(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, boolean alwaysInit)
	{
		try
		{
			return getField(clazz, mcpName, obfName, true, false, alwaysInit).getInt(target);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static Method getMethod(Class clazz, String name, Class...classes)
	{
		try
		{
			Method tMethod = clazz.getDeclaredMethod(name, classes);
			return tMethod;
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	/**
	 * Get method from class.
	 * 
	 * @param clazz the class of method at.
	 * @param mcpName the MCP name of method.
	 * @param obfName the obf name of method.
	 * @param classes the arguments types.
	 * @return the method.
	 * @throw InternalError if method does not exist.
	 */
	public static Method getMethod(Class clazz, String mcpName, String obfName, Class...classes)
	{
		try
		{
			Method method = clazz.getDeclaredMethod(NebulaLoadingPlugin.runtimeDeobf ? obfName : mcpName, classes);
			if (Modifier.isPrivate(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) method.setAccessible(true);
			return method;
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	/**
	 * Invoke method without checking.
	 * 
	 * @param method
	 * @param obj
	 * @param arguments
	 * @return the result of method.
	 * @throw InternalError if method is not accessible or else.
	 * @throw RuntimeException if catching an exception during invoke method.
	 */
	@Nullable
	@SuppressWarnings("hiding")
	public static <R> R invokeMethod(Method method, Object obj, Object...arguments)
	{
		try
		{
			return (R) method.invoke(obj, arguments);
		}
		catch (InvocationTargetException exception)
		{
			Throwable throwable = exception.getTargetException();
			throw (throwable instanceof RuntimeException) ? (RuntimeException) throwable : new RuntimeException("Catch an exception during creating new instance.", throwable);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
	
	public static <T> T newInstance(Constructor constructor, Object...objects)
	{
		try
		{
			return (T) constructor.newInstance(objects);
		}
		catch (InvocationTargetException exception)
		{
			Throwable throwable = exception.getTargetException();
			throw (throwable instanceof RuntimeException) ? (RuntimeException) throwable : new RuntimeException("Catch an exception during creating new instance.", throwable);
		}
		catch (ReflectiveOperationException exception)
		{
			throw new InternalError(exception);
		}
	}
}
