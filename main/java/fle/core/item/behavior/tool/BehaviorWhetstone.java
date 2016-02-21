package fle.core.item.behavior.tool;

import flapi.enums.EnumCraftingType;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.ISubPolishTool;
import flapi.recipe.CraftingState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BehaviorWhetstone extends BehaviorTool implements ISubPolishTool<ItemFleMetaBase>
{
	@Override
	public ItemStack getOutput(ItemFleMetaBase item, ItemStack aStack,
			EntityPlayer aPlayer) 
	{
		item.damageItem(aStack, aPlayer, EnumDamageResource.UseTool, 1);
		return aStack;
	}

	@Override
	public CraftingState getState(ItemFleMetaBase item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState)
	{
		return aState == CraftingState.DEFAULT ? CraftingState.POLISH : aState;
	}	
}