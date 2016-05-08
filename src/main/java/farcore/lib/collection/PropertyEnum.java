package farcore.lib.collection;

import net.minecraft.nbt.NBTTagCompound;

public class PropertyEnum<E extends Enum> extends PropertyAbstract<E, Integer>
{
	private E defaultValue;
	private Class<E> clazz;

	public PropertyEnum(String name, E defaultValue)
	{
		super(name);
		this.clazz = defaultValue.getDeclaringClass();
		this.defaultValue = defaultValue;
	}
	public PropertyEnum(String name, Class<E> clazz)
	{
		super(name);
		this.clazz = clazz;
		this.defaultValue = null;
	}

	@Override
	public E get(Integer object)
	{
		return clazz.getEnumConstants()[object.intValue()];
	}

	@Override
	public Integer set(E target)
	{
		return target == null ? defaultValue.ordinal() :
			target.ordinal();
	}

	@Override
	public E def()
	{
		return defaultValue;
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