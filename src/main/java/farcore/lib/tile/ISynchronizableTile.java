package farcore.lib.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface ISynchronizableTile
{
	void readFromDescription(NBTTagCompound nbt);

	void markBlockRenderUpdate();

	void syncToPlayer(EntityPlayer player);
}