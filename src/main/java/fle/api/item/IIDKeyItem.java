/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.item;

import fle.api.tile.IIDOpenableTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Use to open locked tile entity.
 * 
 * @author ueyudiud
 */
public interface IIDKeyItem
{
	long getID(ItemStack stack);
	
	void onKeyUse(ItemStack stack, IIDOpenableTile tile, EntityPlayer player);
}
