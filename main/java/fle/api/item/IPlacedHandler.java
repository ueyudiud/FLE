package fle.api.item;

import net.minecraft.item.ItemStack;
import fle.api.te.ITEInWorld;

/**
 * The item handler when placed on the ground.
 * @author ueyudiud
 *
 */
public interface IPlacedHandler
{
	ItemStack onBlockRemove(ITEInWorld te, ItemStack target);
	
	ItemStack updatePlacedItem(ITEInWorld te, ItemStack target);
}