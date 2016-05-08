package farcore.lib.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTTagCompoundEmpty extends NBTTagCompound
{
	public static final NBTTagCompoundEmpty instance = new NBTTagCompoundEmpty();
	
	public boolean getBoolean(String tag){return false;}
	public byte getByte(String tag){return 0;}
	public short getShort(String tag) {return 0;}
	public int getInteger(String tag){return 0;}
	public long getLong(String tag){return 0L;}
	public float getFloat(String tag){return 0F;}
	public double getDouble(String tag){return 0D;}
	public byte[] getByteArray(String tag){return new byte[0];}
	public int[] getIntArray(String tag){return new int[0];}
	public String getString(String tag){return "";}
	public NBTTagCompound getCompoundTag(String tag){return instance;}
	public NBTTagList getTagList(String tag, int id){return new NBTTagList();}
	public NBTBase getTag(String tag){return null;}
	public void setTag(String tag, NBTBase nbt){}	
	public void setBoolean(String tag, boolean value){}
	public void setByte(String tag, byte value){}
	public void setShort(String tag, short value){}
	public void setInteger(String tag, int value){}
	public void setLong(String tag, long value) {}
	public void setFloat(String tag, float value){}
	public void setDouble(String tag, double value){}
	public void setString(String tag, String value){}
	public void setByteArray(String tag, byte[] value){}
	public void setIntArray(String tag, int[] value){}
	public boolean hasKey(String tag, int value) {return false;}
	public boolean hasKey(String tag) {return false;}
	public boolean hasNoTags() {return true;}
	
	public int hashCode()
	{
		return 274817393;
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