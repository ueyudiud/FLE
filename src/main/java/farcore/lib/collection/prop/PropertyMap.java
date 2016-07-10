package farcore.lib.collection.prop;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import farcore.lib.collection.AbstractPropertyMap;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyMap extends AbstractPropertyMap<ISaveableProperty>
{
	public PropertyMap(ISaveableProperty...properties)
	{
		this(ImmutableSet.copyOf(properties));
	}
	
	public PropertyMap(ImmutableSet<ISaveableProperty> properties)
	{
		super(properties);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		writeToNBT(nbt, "properties");
	}
	public void writeToNBT(NBTTagCompound nbt, String name)
	{
		NBTTagCompound nbt1 = new NBTTagCompound();
		for(Entry<ISaveableProperty, Object> entry : map.entrySet())
		{
			entry.getKey().writeToNBT(nbt1, entry.getValue());
		}
		nbt.setTag(name, nbt1);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		readFromNBT(nbt, "properties");
	}
	
	public void readFromNBT(NBTTagCompound nbt, String name)
	{
		NBTTagCompound nbt1 = nbt.getCompoundTag(name);
		for(ISaveableProperty property : propertySet())
		{
			if(nbt1.hasKey(property.name()))
			{
				map.put(property, property.readFromNBT(nbt1));
			}
		}
	}
}