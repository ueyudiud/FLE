package farcore.lib.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public interface INBTReaderAndWritter<T>
{
	/**
	 * Read target from nbt with sub tag.
	 * @param nbt
	 * @param key
	 * @return
	 */
	default T readFromNBT(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, NBT.TAG_COMPOUND) ? readFromNBT(nbt.getCompoundTag(key)) : null;
	}
	
	T readFromNBT(NBTTagCompound nbt);
	
	/**
	 * Write target to nbt with sub tag.
	 * @param target
	 * @param nbt
	 * @param key
	 * @return
	 */
	default NBTTagCompound writeToNBT(T target, NBTTagCompound nbt, String key)
	{
		if(target == null) return nbt;
		nbt.setTag(key, writeToNBT1(target, nbt));
		return nbt;
	}
	
	default NBTTagCompound writeToNBT1(T target, NBTTagCompound nbt)
	{
		writeToNBT(target, nbt);
		return nbt;
	}
	
	void writeToNBT(T target, NBTTagCompound nbt);
}