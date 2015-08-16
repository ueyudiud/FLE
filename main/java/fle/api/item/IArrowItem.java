package fle.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IArrowItem
{
	boolean isShootable(ItemStack aTool, ItemStack aStack);
	
	Entity onShoot(EntityLivingBase aEntity);
}
