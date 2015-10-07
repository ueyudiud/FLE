package fle.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;

public class Util
{
	public static <T, F> void overrideStaticField(Class<? extends T> clazz, List<String> field, F override, boolean isPrivate) throws Exception
	{
		overrideField(clazz, field, null, override, isPrivate);
	}
	public static <T, F> void overrideField(Class<? extends T> clazz, List<String> field, T target, F override, boolean isPrivate) throws Exception
	{
		boolean flag = false;
		List<Throwable> list = new ArrayList();
		for(String str : field)
		{
			try
			{
				Field tField;
				if(isPrivate) tField = clazz.getDeclaredField(str);
				else tField = clazz.getField(str);
				if(tField != null)
				{
					tField.setAccessible(true);
					tField.set(target, override);
					flag = true;
					break;
				}
			}
			catch(Throwable e)
			{
				list.add(e);
				continue;
			}
		}
		if(!flag)
		{
			for(Throwable e : list) e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private static Field modifiersField;
	
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
	
	public static <T, F> void overrideStaticFinalField(Class<? extends T> clazz, List<String> field, F override, boolean isPrivate) throws Exception
	{
		overrideFinalField(clazz, field, null, override, isPrivate);
	}
	public static <T, F> void overrideFinalField(Class<? extends T> clazz, List<String> field, T target, F override, boolean isPrivate) throws Exception
	{
		boolean flag = false;
		List<Throwable> list = new ArrayList();
		for(String str : field)
		{
			try
			{
				initModifierField();
				Field tField;
				if(isPrivate) tField = clazz.getDeclaredField(str);
				else tField = clazz.getField(str);
				modifiersField.setInt(tField, tField.getModifiers() & 0xFFFFFFEF);
				if(tField != null)
				{
					tField.setAccessible(true);
					tField.set(target, override);
					flag = true;
					break;
				}
			}
			catch(Throwable e)
			{
				list.add(e);
				continue;
			}
		}
		if(!flag)
		{
			for(Throwable e : list) e.printStackTrace();
			throw new RuntimeException("FLE: fail to find and override field " + field.get(0));
		}
	}
	
	public static <T> Object getValue(Class<? extends T> clazz, List<String> field, T target)
	{
		for(String str : field)
		{
			try
			{
				Field tField = clazz.getDeclaredField(str);
				tField.setAccessible(true);
				return tField.get(target);
			}
			catch(Throwable e)
			{
				continue;
			}
		}
		return null;
	}
	
	public static <T> Collection<T> copy(Collection<T> drops)
	{
		List<T> ret = new ArrayList<T>();
		ret.addAll(drops);
		return ret;
	}
}