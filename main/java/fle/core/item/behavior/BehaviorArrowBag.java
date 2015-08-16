package fle.core.item.behavior;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.item.IArrowItem;
import fle.api.item.ItemFleMetaBase;

public class BehaviorArrowBag extends BehaviorBag
{
	@Override
	public ItemStack onItemRightClick(ItemFleMetaBase item,
			ItemStack itemstack, World aWorld, EntityPlayer player)
	{
		if(!aWorld.isRemote)
		{
			player.openGui(FLE.MODID, -2, aWorld, player.serverPosX, player.serverPosY, player.serverPosZ);
		}
		return itemstack;
	}
	
	@Override
	public boolean isValidArmor(ItemFleMetaBase item, ItemStack aStack,
			int ammorType, Entity aEntity)
	{
		return ammorType == 1;
	}
	
	@Override
	public int getSize(ItemStack aStack)
	{
		return 4;
	}

	@Override
	public boolean isItemValid(ItemStack aStack, ItemStack aInput)
	{
		return aInput == null ? true : aInput.getItem() instanceof IArrowItem || aInput.getItem() == Items.arrow;
	}	
}