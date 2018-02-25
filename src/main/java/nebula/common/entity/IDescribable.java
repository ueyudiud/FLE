/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * For any describable object. Use for network to send message.
 * 
 * @author ueyudiud
 */
public interface IDescribable
{
	NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt);
	
	void readDescriptionsFromNBT(NBTTagCompound nbt);
	
	/**
	 * Mark a player loaded this object and wait sending message from server.
	 * 
	 * @param player
	 */
	void markNBTSync(EntityPlayer player);
}
