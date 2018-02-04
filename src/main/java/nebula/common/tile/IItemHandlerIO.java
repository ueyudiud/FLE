/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.tile;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface IItemHandlerIO
{
	/**
	 * Match side can extract item.
	 * 
	 * @return
	 */
	boolean canExtractItem(Direction to);
	
	/**
	 * Match side can insert item.
	 * 
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canInsertItem(Direction from, @Nullable ItemStack stack);
	
	ItemStack extractItem(int size, Direction to, boolean simulate);
	
	ItemStack extractItem(AbstractStack suggested, Direction to, boolean simulate);
	
	/**
	 * 
	 * @param stack
	 * @param from
	 * @param simulate
	 * @return the size insert in.
	 */
	int insertItem(ItemStack stack, Direction from, boolean simulate);
	
	/**
	 * Called when player use this IO.
	 * 
	 * @param current
	 * @param player
	 * @param side
	 * @param x
	 * @param y
	 * @param z
	 * @param isActiveHeld
	 * @return
	 */
	ActionResult<ItemStack> onPlayerTryUseIO(@Nullable ItemStack current, EntityPlayer player, Direction side, float x, float y, float z, boolean isActiveHeld);
}
