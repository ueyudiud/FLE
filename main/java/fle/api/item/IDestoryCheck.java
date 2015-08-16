package fle.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDestoryCheck
{
	void onToolDestoryed(World aWorld, ItemStack aTool, EntityPlayer aPlayer);
}
