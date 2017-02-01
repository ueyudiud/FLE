package nebula.common.inventory;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public interface IMatrixInventory extends IBasicInventory
{
	/**
	 * Return the width of inventory slot matrix.
	 * @return
	 */
	int getWidth();

	/**
	 * Return the height of inventory slot matrix.
	 * @return
	 */
	int getHeight();
	
	/**
	 * Return the stack in inventory slot with coordinate.
	 * @param x
	 * @param y
	 * @return
	 */
	@Nullable
	ItemStack getStackInSlot(int x, int y);
	
	/**
	 * Put stack into slot with selected coordinate.
	 * @param x
	 * @param y
	 * @param stack
	 */
	void setStackInSlot(int x, int y, @Nullable ItemStack stack);
}