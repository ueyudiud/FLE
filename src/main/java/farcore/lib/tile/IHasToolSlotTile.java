/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile;

import nebula.common.world.ICoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * @author ueyudiud
 */
public interface IHasToolSlotTile extends ICoord
{
	ActionResult<ItemStack> clickSlot(int id, ItemStack stack, EntityPlayer player);
}
