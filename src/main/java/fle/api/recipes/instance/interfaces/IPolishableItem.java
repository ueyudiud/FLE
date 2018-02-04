/*
 * copyright© 2016-2018 ueyudiud
 */

package fle.api.recipes.instance.interfaces;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface IPolishableItem
{
	int getPolishLevel(ItemStack stack);
	
	char getPolishResult(ItemStack stack, char base);
	
	void onPolished(@Nullable EntityPlayer player, ItemStack stack);
}
