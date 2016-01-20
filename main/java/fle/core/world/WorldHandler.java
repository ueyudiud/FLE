package fle.core.world;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldHandler
{
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt)
	{
		WorldData.remove(evt.world);
	}
}