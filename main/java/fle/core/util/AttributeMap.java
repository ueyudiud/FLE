package fle.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import fle.api.util.FleLog;

public class AttributeMap
{
	private final Map<Attribute, Object> map;

	public AttributeMap()
	{
		this(16);
	}
	public AttributeMap(int cap)
	{
		this(16, 0.75F);
	}
	public AttributeMap(int cap, float speed)
	{
		map = new HashMap<Attribute, Object>(cap, speed);
	}
	
	public <T> T getAttribute(Attribute<T> a)
	{
		return map.containsKey(a) ? (T) map.get(a) : a.defaultValue();
	}
	
	public <T> void setAttribute(Attribute<T> a)
	{
		map.put(a, a.defaultValue());
	}
	
	public <T> void setAttribute(Attribute<T> a, T value)
	{
		map.put(a, value);
	}
	
	public <T> T removeAttribute(Attribute<T> a)
	{
		return (T) map.remove(a);
	}
	
	public void clear()
	{
		map.clear();
	}
	
	public Set<Attribute> getAttributes()
	{
		return map.keySet();
	}
	
	public int size()
	{
		return map.size();
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, String name)
	{
		try
		{
			nbt.setByteArray(name, toByteArray());
		}
		catch (IOException e)
		{
			FleLog.getLogger().error("FLE : fail to write to nbt.", e);
		}
		return nbt;
	}
	
	public NBTTagCompound readFromNBT(NBTTagCompound nbt, String name)
	{
		try
		{
			fromByteArray(nbt.getByteArray(name));
		}
		catch (IOException e)
		{
			FleLog.getLogger().error("FLE : fail to write to nbt.", e);
		}
		return nbt;
	}
	
	public byte[] toByteArray() throws IOException
	{
		ByteArrayOutputStream raw = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(raw);
		stream.writeInt(map.size());
		for(Entry<Attribute, Object> art : map.entrySet())
		{
			stream.writeInt(art.getKey().hashCode());
			art.getKey().write(stream, art.getValue());
		}
		raw.close();
		return raw.toByteArray();
	}
	
	public void fromByteArray(byte[] bufs) throws IOException
	{
		clear();
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bufs));
		int length = stream.readInt();
		for(int i = 0; i < length; ++i)
		{
			int code = stream.readInt();
			if(!Attribute.map.contain(code)) throw new IOException("Fail to get attribute.");
			try
			{
				Attribute art = Attribute.map.get(code);
				Object obj = art.read(stream);
				map.put(art, obj);
			}
			catch(Throwable e)
			{
				throw new IOException("Fail to load attribute message.", e);
			}
		}
	}
}