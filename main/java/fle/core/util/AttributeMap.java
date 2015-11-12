package fle.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import fle.api.util.FleLog;

public class AttributeMap
{
	private final Map<Attribute, Object> map;

	public AttributeMap(Entry<Attribute, Object>...attributes)
	{
		this(attributes.length);
		setAttributess(attributes);
	}
	public AttributeMap(Attribute...attributes)
	{
		this(attributes.length);
		setAttributess(attributes);
	}
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

	private void setAttributess(Entry<Attribute, Object>...attributes)
	{
		try
		{
			for(Entry<Attribute, Object> atb : attributes)
			{
				if(!atb.getKey().isInstance(atb.getValue())) throw new IllegalAccessException("FLE: attribute value with class " + atb.getValue().getClass() + "can not case to " + atb.getKey().getAccessClass());
				setAttribute(atb.getKey(), atb.getValue());
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	private void setAttributess(Attribute...attributes)
	{
		for(Attribute atb : attributes)
		{
			setAttribute(atb);
		}
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
	
	public boolean contain(Attribute flag)
	{
		return map.containsKey(flag);
	}
}