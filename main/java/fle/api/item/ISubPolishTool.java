package fle.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import fle.api.enums.EnumCraftingType;
import fle.api.recipe.CraftingState;

public interface ISubPolishTool<E extends Item> 
{
	ItemStack getOutput(E item, ItemStack aStack, EntityPlayer aPlayer);
	
	CraftingState getState(E item, ItemStack aStack, EnumCraftingType aType, CraftingState aState);
}
