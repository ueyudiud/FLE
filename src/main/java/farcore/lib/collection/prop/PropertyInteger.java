package farcore.lib.collection.prop;

import net.minecraft.nbt.NBTTagCompound;

public class PropertyInteger extends PropertyAbstract<Integer, Integer>
{
	public PropertyInteger(String name)
	{
		super(name);
	}

	@Override
	public Integer get(Integer object)
	{
		return object;
	}

	@Override
	public Integer set(Integer target)
	{
		return target;
	}

	@Override
	public Integer def()
	{
		return 0;
	}
	
	@Override
	public Integer readFromNBT(NBTTagCompound nbt)
	{
		return nbt.getInteger(name);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, Integer object)
	{
		if(object != null)
		{
			nbt.setInteger(name, object.intValue());
		}
	}
}