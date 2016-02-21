package flapi.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumCraftingType;
import flapi.recipe.CraftingState;

public interface ISubPolishTool<E extends Item> 
{
	ItemStack getOutput(E item, ItemStack aStack, EntityPlayer aPlayer);
	
	CraftingState getState(E item, ItemStack aStack, EnumCraftingType aType, CraftingState aState);
}
