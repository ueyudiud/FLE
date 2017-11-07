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
	 * 
	 * @param nbt
	 * @param key
	 * @return
	 */
	default @Nullable T readFromNBT(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key) ? readFromNBT((N) nbt.getTag(key)) : null;
	}
	
	/**
	 * Used if this instance implements target type.
	 * 
	 * @param nbt
	 * @param key
	 */
	default void readFromNBT1(NBTTagCompound nbt, String key)
	{
		readFromNBT1((N) nbt.getTag(key));
	}
	
	default void readFromNBT1(N nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	T readFromNBT(N nbt);
}
