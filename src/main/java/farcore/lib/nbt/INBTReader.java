/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.nbt;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 * @param <T> The reading target type.
 */
public interface INBTReader<T>
{
	/**
	 * Read target from nbt with sub tag.
	 * @param nbt
	 * @param key
	 * @return
	 */
	default @Nullable T readFromNBT(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? readFromNBT(nbt.getCompoundTag(key)) : null;
	}
	
	T readFromNBT(NBTTagCompound nbt);
}
