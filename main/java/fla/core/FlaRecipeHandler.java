package fla.core;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fla.core.tool.AxeManager;

public class FlaRecipeHandler 
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent evt)
	{
		for(int i = 0; i < evt.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack stack = evt.craftMatrix.getStackInSlot(i);
			if(stack != null)
			{
				if(AxeManager.isItemAxe(stack))
				{
					++stack.stackSize;
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
