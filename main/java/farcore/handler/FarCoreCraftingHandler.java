package farcore.handler;

import java.util.Arrays;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import farcore.interfaces.item.ICustomDamageItem;
import farcore.util.U;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class FarCoreCraftingHandler
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		if(event.craftMatrix instanceof InventoryCrafting)
		{
			int width = ((int) U.Reflect.getValue(InventoryCrafting.class, Arrays.asList("inventoryWidth", "field_70464_b"), event.craftMatrix));
			int height = event.craftMatrix.getSizeInventory() / width;
			for(int i = 0; i < width; ++i)
			{
				for(int j = 0; j < height; ++j)
				{
					ItemStack stack = ((InventoryCrafting) event.craftMatrix).getStackInRowAndColumn(width, height);
					if(stack != null)
					{
						if(stack.getItem() instanceof ICustomDamageItem)
						{
							ItemStack stack2 = ((ICustomDamageItem) stack.getItem()).getCraftedItem(stack, (InventoryCrafting) event.craftMatrix, j, i);
							if(stack2 != null)
							{
								stack2 = stack2.copy();
								stack2.stackSize ++;
								event.craftMatrix.setInventorySlotContents(j * width + i, stack2);
							}
							else
							{
								event.craftMatrix.setInventorySlotContents(j * width + i, null);
							}
						}
					}
				}
			}
		}
	}
}