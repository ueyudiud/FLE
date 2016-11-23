package farcore.lib.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagCompoundEmpty extends NBTTagCompound
{
	public static final NBTTagCompoundEmpty INSTANCE = new NBTTagCompoundEmpty();
	
	/** The empty tag compound hash is 10. */
	private static final int hashcode = 10;
	
	@Override
	public boolean getBoolean(String tag){return false;}
	@Override
	public byte getByte(String tag){return 0;}
	@Override
	public short getShort(String tag) {return 0;}
	@Override
	public int getInteger(String tag){return 0;}
	@Override
	public long getLong(String tag){return 0L;}
	@Override
	public float getFloat(String tag){return 0F;}
	@Override
	public double getDouble(String tag){return 0D;}
	@Override
	public byte[] getByteArray(String tag){return new byte[0];}
	@Override
	public int[] getIntArray(String tag){return new int[0];}
	@Override
	public String getString(String tag){return "";}
	@Override
	public NBTTagCompound getCompoundTag(String tag){return INSTANCE;}
	@Override
	public NBTTagList getTagList(String tag, int id){return new NBTTagList();}
	@Override
	public NBTBase getTag(String tag){return null;}
	@Override
	public void setTag(String tag, NBTBase nbt){}
	@Override
	public void setBoolean(String tag, boolean value){}
	@Override
	public void setByte(String tag, byte value){}
	@Override
	public void setShort(String tag, short value){}
	@Override
	public void setInteger(String tag, int value){}
	@Override
	public void setLong(String tag, long value) {}
	@Override
	public void setFloat(String tag, float value){}
	@Override
	public void setDouble(String tag, double value){}
	@Override
	public void setString(String tag, String value){}
	@Override
	public void setByteArray(String tag, byte[] value){}
	@Override
	public void setIntArray(String tag, int[] value){}
	@Override
	public boolean hasKey(String tag, int value) {return false;}
	@Override
	public boolean hasKey(String tag) {return false;}
	@Override
	public boolean hasNoTags() {return true;}
	
	@Override
	public int hashCode()
	{
		return hashcode;
	}
	
	@Override
	public boolean equals(Object object)
	{
		return object instanceof NBTTagCompound ?
				((NBTTagCompound) object).hasNoTags() : false;
	}
	
	@Override
	public String toString()
	{
		return "{}";
	}
}