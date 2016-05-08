package farcore.lib.recipe;

import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

public interface IFleRecipe
{
	boolean matchRecipe(ICraftingInventoryMatching inventory);
	
	int getRecipeTick(ICraftingInventoryMatching inventory);
	
	ItemStack getOutput(ICraftingInventoryMatching inventory);
	
	void onOutputStack(ICraftingInventory inventory);
}