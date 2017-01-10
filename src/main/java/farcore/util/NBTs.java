/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

import javax.annotation.Nullable;

import farcore.FarCore;
import farcore.lib.collection.IRegister;
import farcore.lib.material.Mat;
import farcore.lib.nbt.INBTReader;
import farcore.lib.nbt.NBTTagCompoundEmpty;
import farcore.lib.util.Direction;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class NBTs
{
	public static void setString(NBTTagCompound nbt, String key, IRegisteredNameable nameable)
	{
		if(nameable != null)
		{
			nbt.setString(key, nameable.getRegisteredName());
		}
	}
	
	public static void setCharArray(NBTTagCompound nbt, String key, char[] value)
	{
		if(value != null)
		{
			nbt.setString(key, new String(value));
		}
	}
	
	public static <E> void setList(NBTTagCompound nbt, String key, E[] value, Function<E, NBTBase> writer)
	{
		NBTTagList list = new NBTTagList();
		for(E element : value)
		{
			try
			{
				list.appendTag(writer.apply(element));
			}
			catch(Exception exception)
			{
				FarCore.catching(exception);
			}
		}
		nbt.setTag(key, list);
	}
	
	public static NBTTagCompound getOrCreate(NBTTagCompound nbt, String tag)
	{
		return getCompound(nbt, tag, true);
	}
	public static NBTTagCompound getCompound(NBTTagCompound nbt, String tag, boolean create)
	{
		if(!nbt.hasKey(tag))
		{
			if(!create) return NBTTagCompoundEmpty.INSTANCE;
			NBTTagCompound compound;
			nbt.setTag(tag, compound = new NBTTagCompound());
			return compound;
		}
		return nbt.getCompoundTag(tag);
	}
	
	public static void plusRemovableNumber(NBTTagCompound nbt, String key, int add)
	{
		setRemovableNumber(nbt, key, nbt.getInteger(key) + add);
	}
	
	public static void setRemovableNumber(NBTTagCompound nbt, String key, long number)
	{
		if(number == 0)
		{
			nbt.removeTag(key);
		}
		else
		{
			setNumber(nbt, key, number);
		}
	}
	
	public static void setNumber(NBTTagCompound nbt, String key, double number)
	{
		if((float) number == number)
		{
			nbt.setFloat(key, (float) number);
		}
		else
		{
			nbt.setDouble(key, number);
		}
	}
	
	public static void setNumber(NBTTagCompound nbt, String key, long number)
	{
		if((byte) number == number)
		{
			nbt.setByte(key, (byte) number);
		}
		else if((short) number == number)
		{
			nbt.setShort(key, (short) number);
		}
		else if((int) number == number)
		{
			nbt.setInteger(key, (int) number);
		}
		else
		{
			nbt.setLong(key, number);
		}
	}
	
	public static void setStringArray(NBTTagCompound nbt, String key, String[] array)
	{
		NBTTagList list = new NBTTagList();
		for (String element : array)
		{
			list.appendTag(new NBTTagString(element));
		}
		nbt.setTag(key, list);
	}
	
	public static void setLongArray(NBTTagCompound nbt, String key, long[] array)
	{
		NBTTagList list = new NBTTagList();
		for (long element : array)
		{
			list.appendTag(new NBTTagLong(element));
		}
		nbt.setTag(key, list);
	}
	
	public static void setFluidStack(NBTTagCompound nbt, String key, FluidStack stack, boolean markEmpty)
	{
		if(stack != null)
		{
			nbt.setTag(key, stack.writeToNBT(new NBTTagCompound()));
		}
		else if(markEmpty)
		{
			nbt.setTag(key, new NBTTagCompound());//Mark for empty stack.
		}
	}
	
	public static byte getByteOrDefault(NBTTagCompound nbt, String key, int def)
	{
		return nbt.hasKey(key) ? nbt.getByte(key) : (byte) def;
	}
	
	public static short getShortOrDefault(NBTTagCompound nbt, String key, int def)
	{
		return nbt.hasKey(key) ? nbt.getShort(key) : (short) def;
	}
	
	public static int getIntOrDefault(NBTTagCompound nbt, String key, int def)
	{
		return nbt.hasKey(key) ? nbt.getInteger(key) : def;
	}
	
	public static long getLongOrDefault(NBTTagCompound nbt, String key, long def)
	{
		return nbt.hasKey(key) ? nbt.getLong(key) : def;
	}
	
	public static float getFloatOrDefault(NBTTagCompound nbt, String key, float def)
	{
		return nbt.hasKey(key) ? nbt.getLong(key) : def;
	}
	
	public static double getDoubleOrDefault(NBTTagCompound nbt, String key, double def)
	{
		return nbt.hasKey(key) ? nbt.getLong(key) : def;
	}
	
	public static int[] getIntArrayOrDefault(NBTTagCompound nbt, String key, int[] def)
	{
		return nbt.hasKey(key, NBT.TAG_INT_ARRAY) ? nbt.getIntArray(key) : def;
	}
	
	public static long[] getLongArrayOrDefault(NBTTagCompound nbt, String key, long[] def)
	{
		if(nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_LONG);
			if(def.length != list.tagCount())
				return def;
			long[] result = new long[list.tagCount()];
			for(int i = 0; i < result.length; ++i)
			{
				result[i] = ((NBTTagLong) list.get(i)).getLong();
			}
			return result;
		}
		return def;
	}
	
	public static char[] getCharArrayOrDefault(NBTTagCompound nbt, String key, char[] def)
	{
		if(nbt.hasKey(key, NBT.TAG_STRING))
		{
			return nbt.getString(key).toCharArray();
		}
		return def;
	}
	
	public static String[] getStringArrayOrDefault(NBTTagCompound nbt, String key, String[] def)
	{
		if(nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_STRING);
			if(def.length != list.tagCount())
				return def;
			String[] result = new String[list.tagCount()];
			for(int i = 0; i < result.length; ++i)
			{
				result[i] = list.getStringTagAt(i);
			}
			return result;
		}
		return def;
	}
	
	public static FluidStack getFluidStackOrDefault(NBTTagCompound nbt, String key, FluidStack def)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(key)) : def;
	}
	
	public static <E extends Enum<? extends E>> E getEnumOrDefault(NBTTagCompound nbt, String key, E def)
	{
		try
		{
			return nbt.hasKey(key) ? (E) def.getClass().getEnumConstants()[nbt.getByte(key)] : def;
		}
		catch (ArrayIndexOutOfBoundsException exception)
		{
			return def;
		}
	}
	
	public static <E> E getValueByByteOrDefault(NBTTagCompound nbt, String key, E[] values, E def)
	{
		try
		{
			return nbt.hasKey(key) ? (E) values[nbt.getByte(key)] : def;
		}
		catch (ArrayIndexOutOfBoundsException exception)
		{
			return def;
		}
	}
	
	public static Direction getDirectionOrDefault(NBTTagCompound nbt, String key, byte type, Direction def)
	{
		return Direction.readFromNBT(nbt, key, type, def);
	}
	
	public static <T> T getValueByIDOrDefault(NBTTagCompound nbt, String key, IRegister<T> register, T def)
	{
		return nbt.hasKey(key) ? register.get(nbt.getInteger(key), def) : def;
	}
	
	public static <T> T getValueByNameOrDefault(NBTTagCompound nbt, String key, IRegister<T> register, T def)
	{
		return nbt.hasKey(key) ? register.get(nbt.getString(key), def) : def;
	}
	
	public static Mat getMaterialByIDOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? Mat.material(nbt.getShort(key), def) : def;
	}
	
	public static Mat getMaterialByNameOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? Mat.material(nbt.getString(key), def) : def;
	}
	
	public static <E> E[] getArrayOrDefault(NBTTagCompound nbt, String key, E[] container, INBTReader<E> reader)
	{
		if(nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_COMPOUND);
			if(list.tagCount() >= container.length)
				throw new IndexOutOfBoundsException();
			Arrays.fill(container, null);
			for(int i = 0; i < list.tagCount(); ++i)
			{
				NBTTagCompound nbt1 = list.getCompoundTagAt(i);
				short id = nbt1.getShort("num");
				if(id >= container.length)
					throw new IndexOutOfBoundsException();
				try
				{
					container[id] = reader.readFromNBT(nbt1);
				}
				catch (Exception exception)
				{
					FarCore.catching(exception);
					container[id] = null;
				}
			}
		}
		return container;
	}
	
	public static <E> E[] getListOrDefault(NBTTagCompound nbt, String key, Class<E> elementClass, @Nullable E[] def, Function<NBTBase, E> reader)
	{
		if(nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = (NBTTagList) nbt.getTag(key);
			E[] array = (E[]) Array.newInstance(elementClass, list.tagCount());
			for(int i = 0; i < list.tagCount(); ++i)
			{
				NBTBase nbt1 = list.get(i);
				try
				{
					array[i] = reader.apply(nbt1);
				}
				catch (Exception exception)
				{
					FarCore.catching(exception);
				}
			}
			return array;
		}
		return def;
	}
}