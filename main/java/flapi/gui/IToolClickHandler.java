package flapi.gui;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IToolClickHandler
{
	ItemStack onToolClick(ItemStack stack, EntityLivingBase player, int activeID);
}