package fle.core.handler;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.recipe.ItemOreStack;
import fle.core.tool.AxeHandler;

public class RecipeHandler 
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent evt)
	{
		for(int i = 0; i < evt.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack stack = evt.craftMatrix.getStackInSlot(i);
			if(stack != null)
			{
				if(AxeHandler.isItemAxe(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 1F);
					continue;
				}
				else if(new ItemOreStack("craftingToolDecortingPlate").isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(new ItemOreStack("craftingToolDecortingStick").isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
			}
		}
	}
}