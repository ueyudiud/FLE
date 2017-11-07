/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import nebula.Log;
import nebula.base.IRegister;
import nebula.common.nbt.INBTCompoundReader;
import nebula.common.nbt.INBTReader;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.nbt.INBTWriter;
import nebula.common.nbt.NBTTagCompoundEmpty;
import net.minecraft.item.ItemStack;
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
public final class NBTs
{
	private NBTs()
	{
	}
	
	public static void setString(NBTTagCompound nbt, String key, IRegisteredNameable nameable)
	{
		if (nameable != null)
		{
			nbt.setString(key, nameable.getRegisteredName());
		}
	}
	
	public static void setCharArray(NBTTagCompound nbt, String key, char[] value)
	{
		if (value != null)
		{
			nbt.setString(key, new String(value));
		}
	}
	
	public static <E> void setList(NBTTagCompound nbt, String key, E[] value, Function<E, ? extends NBTBase> writer, boolean ordered)
	{
		NBTTagList list = new NBTTagList();
		if (ordered)
		{
			for (int i = 0; i < value.length; ++i)
			{
				try
				{
					NBTBase nbt1 = writer.apply(value[i]);
					if (nbt1 == null) continue;
					NBTTagCompound compound = new NBTTagCompound();
					compound.setTag("element", nbt1);
					setNumber(compound, "idx", i);
					list.appendTag(compound);
				}
				catch (Exception exception)
				{
					Log.catchingIfDebug(exception);
				}
			}
			nbt.setTag(key, list);
		}
		else
		{
			for (E element : value)
			{
				try
				{
					list.appendTag(writer.apply(element));
				}
				catch (Exception exception)
				{
					Log.catchingIfDebug(exception);
				}
			}
			nbt.setTag(key, list);
		}
	}
	
	public static NBTTagCompound getOrCreate(NBTTagCompound nbt, String tag)
	{
		return getCompound(nbt, tag, true);
	}
	
	public static NBTTagCompound getCompound(NBTTagCompound nbt, String tag, boolean create)
	{
		if (!nbt.hasKey(tag))
		{
			if (!create) return NBTTagCompoundEmpty.INSTANCE;
			NBTTagCompound compound;
			nbt.setTag(tag, compound = new NBTTagCompound());
			return compound;
		}
		return nbt.getCompoundTag(tag);
	}
	
	public static int plusRemovableNumber(NBTTagCompound nbt, String key, int add)
	{
		int amount = nbt.getInteger(key) + add;
		setRemovableNumber(nbt, key, amount);
		return amount;
	}
	
	public static int plusRemovableNumber(NBTTagCompound nbt, String key, int add, int max)
	{
		int amount = Math.min(max, nbt.getInteger(key) + add);
		setRemovableNumber(nbt, key, amount);
		return amount;
	}
	
	public static long plusRemovableNumber(NBTTagCompound nbt, String key, long add)
	{
		long amount = nbt.getLong(key) + add;
		setRemovableNumber(nbt, key, amount);
		return amount;
	}
	
	public static void setRemovableNumber(NBTTagCompound nbt, String key, long number)
	{
		if (number == 0)
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
		if ((float) number == number)
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
		if (number <= Byte.MAX_VALUE)
		{
			nbt.setByte(key, (byte) number);
		}
		else if (number <= Short.MAX_VALUE)
		{
			nbt.setShort(key, (short) number);
		}
		else if (number <= Integer.MAX_VALUE)
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
	
	public static <E extends Enum<E>> void setEnum(NBTTagCompound nbt, String key, E value)
	{
		if (value == null)
		{
			nbt.removeTag(key);
		}
		else
		{
			nbt.setInteger(key, value.ordinal());
		}
	}
	
	public static void setItemStack(NBTTagCompound nbt, String key, ItemStack stack, boolean markEmpty)
	{
		if (stack != null)
		{
			nbt.setTag(key, stack.writeToNBT(new NBTTagCompound()));
		}
		else if (markEmpty)
		{
			nbt.setTag(key, new NBTTagCompound());// Mark for empty stack.
		}
	}
	
	public static void setFluidStack(NBTTagCompound nbt, String key, FluidStack stack, boolean markEmpty)
	{
		if (stack != null)
		{
			nbt.setTag(key, stack.writeToNBT(new NBTTagCompound()));
		}
		else if (markEmpty)
		{
			nbt.setTag(key, new NBTTagCompound());// Mark for empty stack.
		}
	}
	
	public static <O> void setObj(NBTTagCompound compound, String key, O obj, INBTWriter<O, ?> writer)
	{
		writer.writeToNBT(obj, compound, key);
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
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_LONG);
			if (def != null && def.length != list.tagCount()) return def;
			long[] result = new long[list.tagCount()];
			for (int i = 0; i < result.length; ++i)
			{
				result[i] = ((NBTTagLong) list.get(i)).getLong();
			}
			return result;
		}
		return def;
	}
	
	public static char[] getCharArrayOrDefault(NBTTagCompound nbt, String key, char[] def)
	{
		if (nbt.hasKey(key, NBT.TAG_STRING))
		{
			return nbt.getString(key).toCharArray();
		}
		return def;
	}
	
	public static String[] getStringArrayOrDefault(NBTTagCompound nbt, String key, String[] def)
	{
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_STRING);
			if (def != null && def.length != list.tagCount()) return def;
			String[] result = new String[list.tagCount()];
			for (int i = 0; i < result.length; ++i)
			{
				result[i] = list.getStringTagAt(i);
			}
			return result;
		}
		return def;
	}
	
	public static ItemStack getItemStackOrDefault(NBTTagCompound nbt, String key, ItemStack def)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(key)) : def;
	}
	
	public static FluidStack getFluidStackOrDefault(NBTTagCompound nbt, String key, FluidStack def)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(key)) : def;
	}
	
	public static <E extends Enum<? extends E>> E getEnumOrDefault(NBTTagCompound nbt, String key, @Nonnull E def)
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
	
	public static <E> E getValueByByteOrDefault(NBTTagCompound nbt, String key, E[] values, @Nonnull E def)
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
	
	public static <E> E[] getArrayOrDefault(NBTTagCompound nbt, String key, E[] container, INBTCompoundReader<E> reader)
	{
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = nbt.getTagList(key, NBT.TAG_COMPOUND);
			if (list.tagCount() > container.length) throw new IndexOutOfBoundsException();
			Arrays.fill(container, null);
			for (int i = 0; i < list.tagCount(); ++i)
			{
				NBTTagCompound nbt1 = list.getCompoundTagAt(i);
				short id = nbt1.getShort("idx");
				if (id >= container.length) throw new IndexOutOfBoundsException();
				try
				{
					container[id] = reader.readFromNBT(nbt1);
				}
				catch (Exception exception)
				{
					Log.catchingIfDebug(exception);
					container[id] = null;
				}
			}
		}
		return container;
	}
	
	public static <O> O getObj(NBTTagCompound nbt, String key, INBTReader<O, ?> reader)
	{
		return reader.readFromNBT(nbt, key);
	}
	
	public static <E, N extends NBTBase> List<E> getUnorderedListFromOrdered(NBTTagCompound nbt, String key, Function<N, E> reader)
	{
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = (NBTTagList) nbt.getTag(key);
			ImmutableList.Builder<E> builder = ImmutableList.builder();
			for (int i = 0; i < list.tagCount(); ++i)
			{
				NBTTagCompound nbt1 = list.getCompoundTagAt(i);
				try
				{
					E e = reader.apply((N) nbt1.getTag("element"));
					if (e != null) builder.add(e);
				}
				catch (Exception exception)
				{
					Log.catchingIfDebug(exception);
				}
			}
			return builder.build();
		}
		return ImmutableList.of();
	}
	
	public static <E, N extends NBTBase> E[] getListOrDefault(NBTTagCompound nbt, String key, Class<E> elementClass, @Nullable E[] def, Function<N, E> reader, boolean ordered)
	{
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = (NBTTagList) nbt.getTag(key);
			E[] array;
			if (ordered)
			{
				array = (E[]) Array.newInstance(elementClass, def.length);
				for (int i = 0; i < list.tagCount(); ++i)
				{
					NBTTagCompound nbt1 = list.getCompoundTagAt(i);
					try
					{
						array[nbt1.getInteger("idx")] = reader.apply((N) nbt1.getTag("element"));
					}
					catch (Exception exception)
					{
						Log.catchingIfDebug(exception);
					}
				}
			}
			else
			{
				array = (E[]) Array.newInstance(elementClass, list.tagCount());
				for (int i = 0; i < list.tagCount(); ++i)
				{
					NBTBase nbt1 = list.get(i);
					try
					{
						array[i] = reader.apply((N) nbt1);
					}
					catch (Exception exception)
					{
						Log.catchingIfDebug(exception);
					}
				}
			}
			return array;
		}
		return def;
	}
	
	public static <N extends NBTBase, E> void insertToList(NBTTagCompound nbt, String key, @Nullable E[] def, Function<N, E> reader, boolean ordered)
	{
		if (nbt.hasKey(key, NBT.TAG_LIST))
		{
			NBTTagList list = (NBTTagList) nbt.getTag(key);
			if (ordered)
			{
				for (int i = 0; i < list.tagCount(); ++i)
				{
					NBTTagCompound nbt1 = list.getCompoundTagAt(i);
					try
					{
						def[nbt1.getInteger("idx")] = reader.apply((N) nbt1.getTag("element"));
					}
					catch (Exception exception)
					{
						Log.catchingIfDebug(exception);
					}
				}
			}
			else
			{
				for (int i = 0; i < list.tagCount(); ++i)
				{
					NBTBase nbt1 = list.get(i);
					try
					{
						def[i] = reader.apply((N) nbt1);
					}
					catch (Exception exception)
					{
						Log.catchingIfDebug(exception);
					}
				}
			}
		}
	}
	
	public static <E, N extends NBTBase> INBTReaderAndWritter<E[], NBTTagList> wrapAsUnorderedArrayWriterAndReader(INBTReaderAndWritter<E, N> rw)
	{
		final Class<E> clazz = (Class<E>) rw.getTargetType();
		return new INBTReaderAndWritter<E[], NBTTagList>()
		{
			@Override
			public E[] readFromNBT(NBTTagList nbt)
			{
				E[] result = (E[]) Array.newInstance(clazz, nbt.tagCount());
				for (int i = 0; i < nbt.tagCount(); ++i)
				{
					result[i] = rw.readFromNBT((N) nbt.get(i));
				}
				return result;
			}
			
			@Override
			public NBTTagList writeToNBT(E[] target)
			{
				NBTTagList list = new NBTTagList();
				for (E element : target)
				{
					list.appendTag(rw.writeToNBT(element));
				}
				return list;
			}
		};
	}
}
