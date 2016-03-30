package farcore.interfaces.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IDescribableTile
{	
	NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt);
	
	void readDescriptionsFromNBT(NBTTagCompound nbt);
	
	void markNBTSync(EntityPlayer player);
}