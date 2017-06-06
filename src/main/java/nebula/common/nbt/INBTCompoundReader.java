/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 * @param <T> The reading target type.
 */
public interface INBTCompoundReader<T> extends INBTReader<T, NBTTagCompound>
{
	/**
	 * Read target from nbt with sub tag.
	 * @param nbt
	 * @param key
	 * @return
	 */
	@Override
	default @Nullable T readFromNBT(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? readFromNBT(nbt.getCompoundTag(key)) : null;
	}
	
	default void readFromNBT1(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
}