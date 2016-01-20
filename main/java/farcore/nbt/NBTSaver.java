package farcore.nbt;

import net.minecraft.nbt.NBTTagCompound;

import farcore.nbt.NBT.NBTControlor;

public interface NBTSaver extends NBTControlor
{
	boolean canSave(Class clazz);
	
	void save(String name, Object obj, NBTTagCompound nbt);
}