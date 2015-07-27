package fle.api.net;

import net.minecraft.nbt.NBTTagCompound;

public interface INetEventHandler
{
	public NBTTagCompound onEmmitNBT();
	
	public void onReseaveNBT(NBTTagCompound nbt);
}
