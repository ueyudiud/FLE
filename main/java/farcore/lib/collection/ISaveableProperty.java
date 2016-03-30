package farcore.lib.collection;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveableProperty<T, O> extends IProperty<T, O>
{
	O readFromNBT(NBTTagCompound nbt);
	
	void writeToNBT(NBTTagCompound nbt, O object); 
}