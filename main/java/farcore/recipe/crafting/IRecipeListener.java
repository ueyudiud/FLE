package farcore.recipe.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;

public interface IRecipeListener
{
	void onCrafting(InventoryCrafting inputs, EntityPlayer player);
}