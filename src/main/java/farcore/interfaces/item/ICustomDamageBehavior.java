package farcore.interfaces.item;

import javax.annotation.Nullable;

import farcore.lib.recipe.ICraftingInventory;
import net.minecraft.item.ItemStack;

public interface ICustomDamageBehavior 
{
	ItemStack getCraftedItem(ItemStack stack, @Nullable ICraftingInventory crafting);
}