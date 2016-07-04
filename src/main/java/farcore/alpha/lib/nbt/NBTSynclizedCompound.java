package farcore.alpha.lib.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import farcore.util.FleLog;
import farcore.util.V;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class NBTSynclizedCompound
{
	private Map<String, NBTBase> tagMap = new HashMap();
	private boolean update;
	private NBTTagCompound nbt;
	
	public NBTSynclizedCompound()
	{
		reset();
	}
	
	private void mark()
	{
		update = true;
	}
	
	public void reset()
	{
		update = false;
		nbt = new NBTTagCompound();
	}
	
	public boolean hasKey(String name)
	{
		return tagMap.get(name) != null;
	}
	
	public boolean hasKey(String name, int value)
	{
		try
		{
			NBTBase nbt;
			return (nbt = tagMap.get(name)) != null ?
					value == 99 || nbt.getId() == value : false;
		}
		catch(Exception exception)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().catching(exception);
			}
			return false;
		}
	}

	public NBTBase getTag(String name)
	{
		return tagMap.get(name);
	}
	
	public boolean getBoolean(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? false :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150290_f() != 0;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	
	public byte getByte(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150290_f();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public short getShort(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150289_e();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public int getInt(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150287_d();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public long getLong(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150291_c();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public float getFloat(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150288_h();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public double getDouble(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? 0 :
				((NBTBase.NBTPrimitive) tagMap.get(name)).func_150286_g();
		}
		catch(Exception exception)
		{
			return 0;
		}
	}
	
	public String getString(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? "" :
				((NBTTagString) tagMap.get(name)).func_150285_a_();
		}
		catch(Exception exception)
		{
			return "";
		}
	}
	
	public byte[] getByteArray(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? new byte[0] :
				((NBTTagByteArray) tagMap.get(name)).func_150292_c();
		}
		catch(Exception exception)
		{
			return new byte[0];
		}
	}
	
	public int[] getIntArray(String name)
	{
		try
		{
			return !tagMap.containsKey(name) ? new int[0] :
				((NBTTagIntArray) tagMap.get(name)).func_150302_c();
		}
		catch(Exception exception)
		{
			return new int[0];
		}
	}
	
	public void setTag(String name, NBTBase tag)
	{
		try
		{
			if(hasKey(name))
			{
				if(!tag.equals(tagMap.get(name)))
				{
					tagMap.put(name, tag);
					nbt.setTag(name, tag);
					mark();
				}
			}
			else
			{
				tagMap.put(name, tag);
				nbt.setTag(name, tag);
				mark();
			}
		}
		catch(Exception exception)
		{
			if(V.debug)
			{
				FleLog.getCoreLogger().catching(exception);
			}
		}
	}

	public void setBoolean(String name, boolean value)
	{
		setTag(name, new NBTTagByte((byte) (value ? 1 : 0)));
	}
	
	public void setByte(String name, int value)
	{
		setTag(name, new NBTTagByte((byte) value));
	}
	
	public void setShort(String name, int value)
	{
		setTag(name, new NBTTagShort((short) value));
	}
	
	public void setInt(String name, int value)
	{
		setTag(name, new NBTTagInt(value));
	}
	
	public void setLong(String name, long value)
	{
		setTag(name, new NBTTagLong(value));
	}
	
	public void setFloat(String name, float value)
	{
		setTag(name, new NBTTagFloat(value));
	}
	
	public void setDouble(String name, double value)
	{
		setTag(name, new NBTTagDouble(value));
	}
	
	public void setString(String name, String value)
	{
		setTag(name, new NBTTagString(value));
	}
	
	public void setByteArray(String name, byte[] value)
	{
		setTag(name, new NBTTagByteArray(value));
	}
	
	public void setIntArray(String name, int[] value)
	{
		setTag(name, new NBTTagIntArray(value));
	}
	
	public NBTTagCompound asCompound()
	{
		NBTTagCompound ret = new NBTTagCompound();
		for(Entry<String, NBTBase> entry : tagMap.entrySet())
		{
			ret.setTag(entry.getKey(), entry.getValue());
		}
		return ret;
	}
	
	public boolean isChanged()
	{
		return update;
	}
	
	public NBTTagCompound getChanged(boolean reset)
	{
		NBTTagCompound ret = nbt;
		if(reset) reset();
		return ret;
	}

	public void clear()
	{
		tagMap.clear();
		reset();
	}
}