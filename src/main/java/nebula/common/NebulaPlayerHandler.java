/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import nebula.Nebula;
import nebula.common.network.packet.PacketBlockData;
import nebula.common.util.Sides;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author ueyudiud
 */
public class NebulaPlayerHandler
{
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(Sides.isServer())
		{
			Nebula.network.sendLargeToPlayer(new PacketBlockData(), event.player);
		}
	}
}