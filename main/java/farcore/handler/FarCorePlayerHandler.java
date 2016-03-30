package farcore.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import farcore.interfaces.item.IContainerItemCollectable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class FarCorePlayerHandler
{
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if(event.item.getEntityItem() == null || event.item.getEntityItem().stackSize <= 0)
		{
			event.setResult(Result.DENY);
			return;
		}
		if(event.entityPlayer != null)
		{
			InventoryPlayer inventory = event.entityPlayer.inventory;
			for(int i = 0; i < inventory.mainInventory.length; ++i)
			{
				if(inventory.mainInventory[i] != null)
				{
					if(inventory.mainInventory[i].getItem() instanceof IContainerItemCollectable)
					{
						ItemStack container = inventory.mainInventory[i];
						IContainerItemCollectable collectable = (IContainerItemCollectable) container.getItem();
						if(collectable.canCollectItemToContainer(container, event.item.getEntityItem()))
						{
							collectable.collectItemToContainer(container, event.item.getEntityItem());
							if(event.item.getEntityItem().stackSize <= 0)
							{
								event.setResult(Result.DENY);
								return;
							}
						}
					}
				}
			}
		}
	}
}