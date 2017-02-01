/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public class R
{
	static final Map<String, Field> FIELD_CACHE = new HashMap();
	private static Field modifiersField;

	public static void resetFieldCache()
	{
		FIELD_CACHE.clear();
	}

	private static void initModifierField()
	{
		try
		{
			if(modifiersField == null)
			{
				modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	private static Field getField(Class<?> clazz, String mcpName, String obfName, boolean isPrivate, boolean isFinal, boolean alwaysInit) throws Exception
	{
		if(isFinal)
		{
			initModifierField();
		}
		for(String str : new String[]{mcpName, obfName})
		{
			try
			{
				if(!alwaysInit && FIELD_CACHE.containsKey(clazz.getName() + "|" + str))
					return FIELD_CACHE.get(clazz.getName() + "|" + str);
				Field tField;
				if(isPrivate)
				{
					tField = clazz.getDeclaredField(str);
				}
				else
				{
					tField = clazz.getField(str);
				}
				if(isFinal)
				{
					modifiersField.setInt(tField, tField.getModifiers() & 0xFFFFFFEF);
				}
				if(tField != null)
				{
					tField.setAccessible(true);
					FIELD_CACHE.put(clazz.getName() + "|" + str, tField);
					return tField;
				}
			}
			catch(Exception exception)
			{
				throw exception;
			}
		}
		throw new FileNotFoundException();
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
		catch(Throwable e)
		{
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable F override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		overrideFinalField(clazz, mcpName, obfName, null, override, isPrivate, alwaysInit);
	}

	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, @Nullable F override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		try
		{
			getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).set(target, override);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			throw new RuntimeException("FLE: fail to find and override field " + mcpName);
		}
	}

	public static <T, F> void overrideFinalField(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, int override, boolean isPrivate, boolean alwaysInit) throws Exception
	{
		try
		{
			getField(clazz, mcpName, obfName, isPrivate, true, alwaysInit).setInt(target, override);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			throw new RuntimeException("FLE: fail to find and override field " + mcpName);
		}
	}

	public static <T> Object getValue(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, boolean alwaysInit)
	{
		try
		{
			return getField(clazz, mcpName, obfName, true, false, alwaysInit).get(target);
		}
		catch(Exception exception)
		{
			return null;
		}
	}

	public static <T> int getInt(Class<? extends T> clazz, String mcpName, String obfName, @Nullable T target, boolean alwaysInit)
	{
		try
		{
			return getField(clazz, mcpName, obfName, true, false, alwaysInit).getInt(target);
		}
		catch(Exception exception)
		{
			return 0;
		}
	}

	public static Method getMethod(Class clazz, String mcpName, String obfName, Class...classes)
	{
		for(String str : new String[]{mcpName, obfName})
		{
			try
			{
				Method tMethod = clazz.getDeclaredMethod(str, classes);
				return tMethod;
			}
			catch(Throwable e)
			{
				continue;
			}
		}
		return null;
	}
}