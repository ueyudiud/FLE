package flapi.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerToolCraftingRecipe
{
	boolean match(ItemStack input1, ItemStack input2, ItemStack tool);
	
	ItemStack useTool(EntityPlayer player, ItemStack tool);
	
	ItemStack getOutput(ItemStack input1, ItemStack input2, ItemStack tool);
}