package farcore.nbt;

import net.minecraft.nbt.NBTTagCompound;

import farcore.nbt.NBT.NBTControlor;

public interface NBTLoader extends NBTControlor
{
	boolean canLoad(Class clazz);
	
	Object load(String name, NBTTagCompound tag);
}
