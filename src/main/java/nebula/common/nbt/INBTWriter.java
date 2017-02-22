/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
@FunctionalInterface
public interface INBTWriter<T, N extends NBTBase>
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
	
	@SuppressWarnings("unchecked")
	default N writeToNBT()
	{
		return writeToNBT((T) this);
	}
	
	N writeToNBT(T target);
	
	default N apply(T target)
	{
		return writeToNBT(target);
	}
}