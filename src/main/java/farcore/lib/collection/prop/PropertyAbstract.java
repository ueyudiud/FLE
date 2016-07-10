package farcore.lib.collection.prop;

import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyAbstract<T, O> implements ISaveableProperty<T, O>
{
	protected final String name;
	
	public PropertyAbstract(String name)
	{
		this.name = name;
	}

	@Override
	public final String name()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "property." + name;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == this ? true : 
			(obj instanceof PropertyAbstract) ? name.equals(((PropertyAbstract) obj).name) :
				false;
	}
}