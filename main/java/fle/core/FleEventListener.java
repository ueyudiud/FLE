package fle.core;

import fle.FLE;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class FleEventListener
{
	@SubscribeEvent
	public void onClientUpdate(TickEvent.ClientTickEvent evt)
	{
		if(evt.phase == Phase.START)
		{
			FLE.fle.getKeyboard().sendKeyUpdate();
		}
	}
}