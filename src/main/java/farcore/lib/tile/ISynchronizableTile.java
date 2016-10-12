package farcore.lib.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;

public interface ISynchronizableTile
{
	void readFromDescription(NBTTagCompound nbt);

	void markBlockRenderUpdate();
	
	void markLightForUpdate(EnumSkyBlock type);

	void syncToPlayer(EntityPlayer player);
}