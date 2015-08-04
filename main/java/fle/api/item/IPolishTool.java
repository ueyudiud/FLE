package fle.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import fle.api.enums.EnumCraftingType;
import fle.api.recipe.CraftingState;

public interface IPolishTool
{
	ItemStack getOutput(EntityPlayer player, ItemStack input);

	CraftingState getState(ItemStack input, EnumCraftingType aType, CraftingState aState);
}
