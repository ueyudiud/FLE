package farcore.interfaces.item;

import javax.annotation.Nullable;

import farcore.enums.EnumDamageResource;
import farcore.lib.recipe.ICraftingInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
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
	 * @param crafting The crafting inventory. If this is null means use minecraft work bench.
	 * @return
	 */
	ItemStack getCraftedItem(ItemStack stack, @Nullable ICraftingInventory crafting);
}