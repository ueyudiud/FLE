/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.io.binding;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

import nebula.common.base.Ety;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class NBTBinding implements Bindings
{
	NBTTagCompound nbt;
	private Collection<Object> values;
	private Set<Entry<String, Object>> entrySet;
	
	NBTBinding(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}
	
	@Override
	public int size()
	{
		return this.nbt.getSize();
	}
	
	@Override
	public boolean isEmpty()
	{
		return this.nbt.hasNoTags();
	}
	
	@Override
	//Raw method.
	public boolean containsValue(Object value)
	{
		return false;
	}
	
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Set<String> keySet()
	{
		return this.nbt.getKeySet();
	}
	
	@Override
	public Collection<Object> values()
	{
		return this.values != null ? this.values :
			(this.values = Collections2.transform(this.nbt.getKeySet(), key -> decode(this.nbt.getTag(key))));
	}
	
	private Object decode(NBTBase tag)
	{
		return tag == null ? null : decode(tag, tag.getId());
	}
	
	private Object decode(NBTBase tag, byte id)
	{
		switch (id)
		{
		case NBT.TAG_BYTE :
		case NBT.TAG_SHORT :
		case NBT.TAG_INT :
			return ((NBTPrimitive) tag).getInt();
		case NBT.TAG_LONG :
			return ((NBTPrimitive) tag).getLong();
		case NBT.TAG_FLOAT :
			return ((NBTPrimitive) tag).getFloat();
		case NBT.TAG_DOUBLE :
			return ((NBTPrimitive) tag).getDouble();
		case NBT.TAG_STRING :
			return ((NBTTagString) tag).getString();
		case NBT.TAG_COMPOUND :
			return new NBTBinding((NBTTagCompound) tag);
		case NBT.TAG_INT_ARRAY :
			return ((NBTTagIntArray) tag).getIntArray();
		case NBT.TAG_BYTE_ARRAY :
			return ((NBTTagByteArray) tag).getByteArray();
		case NBT.TAG_LIST :
			return toObjectArray((NBTTagList) tag);
		default : return null;
		}
	}
	
	private Object[] toObjectArray(NBTTagList list)
	{
		byte id = (byte) list.getTagType();
		if(id == 0) return new Object[0];
		Object[] result = new Object[list.tagCount()];
		for(int i = 0; i < result.length; ++i)
		{
			result[i] = decode(list.get(i), id);
		}
		return result;
	}
	
	@Override
	public Set<Entry<String, Object>> entrySet()
	{
		if(this.entrySet != null) return this.entrySet;
		Collection<Entry<String, Object>> collection = Collections2.transform(this.nbt.getKeySet(), key -> new Ety(key, decode(this.nbt.getTag(key))));
		return this.entrySet = ImmutableSet.copyOf(collection);
	}
	
	@Override
	public Object put(String name, Object value)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return key instanceof String ? this.nbt.hasKey((String) key) : false;
	}
	
	@Override
	public Object get(Object key)
	{
		return !(key instanceof String) ? null : decode(this.nbt.getTag((String) key));
	}
	
	@Override
	public Object remove(Object key)
	{
		throw new UnsupportedOperationException();
	}
}