package nebula.common.tile;

import nebula.common.world.ICoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;

public interface ISynchronizableTile extends ICoord
{
	void readFromDescription(NBTTagCompound nbt);
	
	void markBlockRenderUpdate();
	
	void markLightForUpdate(EnumSkyBlock type);
	
	void syncToPlayer(EntityPlayer player);
}