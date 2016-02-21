package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumCraftingType;
import flapi.item.interfaces.ISubPolishTool;
import flapi.recipe.CraftingState;

public class BehaviorFlintChip extends BehaviorBase implements ISubPolishTool
{
	@Override
	public ItemStack getOutput(Item item, ItemStack aStack, EntityPlayer aPlayer)
	{
		aStack.stackSize--;
		return aStack;
	}

	@Override
	public CraftingState getState(Item item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState) 
	{
		return aState == CraftingState.DEFAULT ? CraftingState.CRUSH : aState;
	}
}