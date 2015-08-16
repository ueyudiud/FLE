package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import fle.api.enums.EnumCraftingType;
import fle.api.enums.EnumDamageResource;
import fle.api.item.ISubPolishTool;
import fle.api.item.ItemFleMetaBase;
import fle.api.recipe.CraftingState;

public class BehaviorAwl extends BehaviorBase implements ISubPolishTool<ItemFleMetaBase>
{
	@Override
	public ItemStack getOutput(ItemFleMetaBase item, ItemStack aStack,
			EntityPlayer aPlayer)
	{
		item.damageItem(aStack, aPlayer, EnumDamageResource.Crafting, 1);
		return aStack;
	}

	@Override
	public CraftingState getState(ItemFleMetaBase item, ItemStack aStack,
			EnumCraftingType aType, CraftingState aState)
	{
		return aType != EnumCraftingType.polish ? aState : aState == CraftingState.DEFAULT ? aState.CRUSH : aState;
	}
}