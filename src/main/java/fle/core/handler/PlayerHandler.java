package fle.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import farcore.handler.FarCoreKeyHandler;
import fle.api.FleAPI;

public class PlayerHandler
{
	@SubscribeEvent
	public void onPlayerPress(TickEvent.PlayerTickEvent event)
	{
		if(event.phase == Phase.END && event.side == Side.SERVER)
		{
			if(event.player.openContainer == event.player.inventoryContainer && 
					FarCoreKeyHandler.get(event.player, "crafting"))
			{
				FleAPI.openGui(-2, event.player, event.player.worldObj, 0, 0, 0);
			}
		}
	}
}