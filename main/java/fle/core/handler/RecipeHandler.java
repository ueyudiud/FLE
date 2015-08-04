package fle.core.handler;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fle.api.enums.EnumDamageResource;
import fle.api.item.ItemFleTool;
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
					if(stack.getItem() instanceof ItemFleTool)
					{
						((ItemFleTool) stack.getItem()).damageItem(stack, evt.player, EnumDamageResource.Crafting, 1);
						continue;
					}
					int d = stack.getItemDamage() + 1;
					if(d >= stack.getMaxDamage())
					{
						--stack.stackSize;
					}
					else
					{
						stack.setItemDamage(d);
					}
					continue;
				}
			}
		}
	}
}