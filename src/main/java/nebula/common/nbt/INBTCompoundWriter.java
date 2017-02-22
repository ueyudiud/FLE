/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public interface INBTCompoundWriter<T> extends INBTWriter<T, NBTTagCompound>
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
		nbt.setTag(key, writeToNBT(target));
		return nbt;
	}
	
	/**
	 * Only call when this type is implements the writer.
	 * @param nbt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		writeToNBT((T) this, nbt);
		return nbt;
	}
	
	@Override
	default NBTTagCompound writeToNBT(T target)
	{
		NBTTagCompound nbt;
		writeToNBT(target, nbt = new NBTTagCompound());
		return nbt;
	}
	
	void writeToNBT(T target, NBTTagCompound nbt);
}