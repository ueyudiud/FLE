package farcore.interfaces.item;

import farcore.enums.EnumDamageResource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public interface ICustomDamageItem
{
	/**
	 * Called when item is damaged.
	 * @param stack
	 * @param amount
	 * @param user
	 * @param resource
	 */
	void damangeItem(ItemStack stack, int amount, EntityLivingBase user, EnumDamageResource resource);
	
	/**
	 * Called when crafted item in crafting matrix.
	 * @param stack The tool.
	 * @param crafting The crafting inventory.
	 * @param x The x coordinate of tool in crafting inventory.
	 * @param y The y coordinate of tool in crafting inventory.
	 * @return
	 */
	ItemStack getCraftedItem(ItemStack stack, InventoryCrafting crafting, int x, int y);
}