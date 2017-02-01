/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public interface INBTWriter<T>
{
	/**
	 * Write target to nbt with sub tag.
	 * @param target
	 * @param nbt
	 * @param key
	 * @return
	 */
	default NBTTagCompound writeToNBT(@Nullable T target, NBTTagCompound nbt, String key)
	{
		if(target == null) return nbt;
		nbt.setTag(key, writeToNBT1(target, new NBTTagCompound()));
		return nbt;
	}
	
	default NBTTagCompound writeToNBT1(T target, NBTTagCompound nbt)
	{
		writeToNBT(target, nbt);
		return nbt;
	}
	
	@SuppressWarnings("unchecked")
	default NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		return writeToNBT1((T) this, nbt);
	}
	
	void writeToNBT(T target, NBTTagCompound nbt);
}