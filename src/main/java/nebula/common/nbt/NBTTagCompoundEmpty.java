/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.nbt;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import nebula.common.data.Misc;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagCompoundEmpty extends NBTTagCompound
{
	/** The empty NBT tag. */
	public static final NBTTagCompoundEmpty INSTANCE = new NBTTagCompoundEmpty();
	
	/** The empty tag compound hash is 10. */
	private static final int hashcode = 10;
	
	private NBTTagCompoundEmpty() {	}
	
	@Override public boolean	getBoolean	(String tag) { return false;}
	@Override public byte		getByte		(String tag) { return 0; }
	@Override public short		getShort	(String tag) { return 0; }
	@Override public int		getInteger	(String tag) { return 0; }
	@Override public long		getLong		(String tag) { return 0L;}
	@Override public float		getFloat	(String tag) { return 0F;}
	@Override public double		getDouble	(String tag) { return 0D;}
	@Override public byte[]		getByteArray(String tag) { return Misc.BYTES_EMPTY; }
	@Override public int[]		getIntArray	(String tag) { return Misc.INTS_EMPTY; }
	@Override public String		getString	(String tag) { return "";}
	
	@Override public NBTTagCompound	getCompoundTag	(String tag) { return INSTANCE; }
	@Override public NBTTagList		getTagList		(String tag, int id) { return new NBTTagList(); }
	@Override public NBTBase		getTag			(String tag) { return null; }
	
	@Override public void setTag(String tag, NBTBase nbt){}
	@Override public void setBoolean(String tag, boolean value){}
	@Override public void setByte(String tag, byte value){}
	@Override public void setShort(String tag, short value){}
	@Override public void setInteger(String tag, int value){}
	@Override public void setLong(String tag, long value) {}
	@Override public void setFloat(String tag, float value){}
	@Override public void setDouble(String tag, double value){}
	@Override public void setString(String tag, String value){}
	@Override public void setByteArray(String tag, byte[] value){}
	@Override public void setIntArray(String tag, int[] value){}
	
	@Override public boolean hasKey(String tag, int value) {return false;}
	@Override public boolean hasKey(String tag) {return false;}
	@Override public boolean hasNoTags() { return true; }
	
	@Override public Set<String> getKeySet() { return ImmutableSet.of(); }
	@Override public void removeTag(String key) {}
	@Override public int hashCode() { return hashcode; }
	
	@Override
	public void merge(NBTTagCompound other)
	{
		throw new UnsupportedOperationException("This is only a immutable NBTTagCompound, you can't take any operation on it!");
	}
	
	@Override public int getSize() {return 0; }
	
	@Override public boolean hasUniqueId(String key) { return false; }
	
	@Override public UUID getUniqueId(String key) { return new UUID(0L, 0L); }
	
	@Override public byte getTagId(String key) { return 0; }
	
	@Override public boolean equals(Object object)
	{
		return object instanceof NBTTagCompound && ((NBTTagCompound) object).hasNoTags();
	}
	
	@Override
	public NBTTagCompound copy()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return "{}";
	}
}