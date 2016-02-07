package fle.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import fle.core.FLE;

public class PlayerEventHandler
{
	@SubscribeEvent
	public void sendKey(TickEvent.ClientTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			FLE.fle.getKeyboard().sendKeyUpdate();
		}
	}
}