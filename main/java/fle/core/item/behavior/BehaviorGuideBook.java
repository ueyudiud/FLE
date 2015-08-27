package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.item.ItemFleMetaBase;

public class BehaviorGuideBook extends BehaviorBase
{
	@Override
	public ItemStack onItemRightClick(ItemFleMetaBase item,
			ItemStack itemstack, World aWorld, EntityPlayer player)
	{
		if(!aWorld.isRemote)
		{
			player.openGui(FLE.MODID, -4, aWorld, player.serverPosX, player.serverPosY, player.serverPosZ);
		}
		return itemstack;
	}
}