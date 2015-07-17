package fla.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPolishTool 
{
	public ItemStack getOutput(EntityPlayer player, ItemStack input);
}
