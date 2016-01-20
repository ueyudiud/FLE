package farcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class Util
{
	private static final FleRandom rand = new FleRandom();
	
	private static Field modifiersField;
	
	public static <T extends Number> byte[] toBytes(T...ts)
	{
		byte[] ret = new byte[ts.length];
		for(int i = 0; i < ts.length; ret[i] = ts[i].byteValue(), i++);
		return ret;
	}
	public static <T extends Number> short[] toShorts(T...ts)
	{
		short[] ret = new short[ts.length];
		for(int i = 0; i < ts.length; ret[i] = ts[i].shortValue(), i++);
		return ret;
	}
	public static <T extends Number> int[] toInts(T...ts)
	{
		int[] ret = new int[ts.length];
		for(int i = 0; i < ts.length; ret[i] = ts[i].intValue(), i++);
		return ret;
	}
	public static <T extends Number> long[] toLongs(T...ts)
	{
		long[] ret = new long[ts.length];
		for(int i = 0; i < ts.length; ret[i] = ts[i].longValue(), i++);
		return ret;
	}
	
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
	
	public static Method getMethod(Class clazz, List<String> field, Class...classes)
	{
		for(String str : field)
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
	
	public static <T> Collection<T> copy(Collection<T> drops)
	{
		List<T> ret = new ArrayList<T>();
		ret.addAll(drops);
		return ret;
	}
	
	public static void setStacksSize(ItemStack[][] arrays, int i)
	{
		for(ItemStack[] array : arrays)
		{
			if(array == null) continue;
			for(ItemStack stack : array)
			{
				if(stack == null) continue;
				stack.stackSize = i;
			}
		}
	}
	
	public static <T> T randomGet(Random rand, T[] array)
	{
		return array[rand.nextInt(array.length)];
	}
	
	public static <T> T randomGet(T[] array)
	{
		return array[rand.nextInt(array.length)];
	}
	
	public static String firstToUppercase(String str)
	{
		if(str == null) return null;
		if(str.length() == 0) return "";
		char c = Character.toUpperCase(str.charAt(0));//To upper case.
		String s = str.substring(1);
		return new StringBuilder().append(c).append(s).toString();
	}
	
	public static int valueOfInt(short[] array, int size)
	{
		if(array == null) return 0;
		if(array.length < size) return 0x1 << (8 * size) - 1;
		int ret = 0;
		for(int i = 0; i < size; ++i)
		{
			ret ^= array[i] << (8 * i);
		}
		return ret;
	}
}