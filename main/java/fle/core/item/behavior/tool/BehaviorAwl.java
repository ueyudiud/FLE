package fle.core.item.behavior.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumCraftingType;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.ISubPolishTool;
import flapi.recipe.CraftingState;
import fle.core.item.behavior.BehaviorBase;

public class BehaviorAwl extends BehaviorBase implements ISubPolishTool<ItemFleMetaBase>
{
	@Override
	public ItemStack getOutput(ItemFleMetaBase item, ItemStack aStack,
			EntityPlayer aPlayer)
	{
		item.damageItem(aStack, aPlayer, EnumDamageResource.Crafting, 0.2F);
		return aStack;
	}

	@Override
	public CraftingState getState(ItemFleMetaBase item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState)
	{
		return aType != EnumCraftingType.polish ? aState : aState == CraftingState.DEFAULT ? CraftingState.CRUSH : aState;
	}
}