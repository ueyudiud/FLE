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
public interface INBTReader<T, N extends NBTBase>
{
	/**
	 * Read target from nbt with sub tag.
	 * @param nbt
	 * @param key
	 * @return
	 */
	default @Nullable T readFromNBT(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key) ? readFromNBT((N) nbt.getTag(key)) : null;
	}
	
	T readFromNBT(N nbt);
}