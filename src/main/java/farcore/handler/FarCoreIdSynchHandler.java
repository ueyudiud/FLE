/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.handler;

import java.io.IOException;

import farcore.lib.solid.Solid;
import nebula.Log;
import nebula.Nebula;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;
import nebula.common.util.Sides;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ueyudiud
 */
public class FarCoreIdSynchHandler
{
	public static void initialize()
	{
		Nebula.network.registerPacket(PackeSolidSynch.class, Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(FarCoreIdSynchHandler.class);
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (Sides.isServer())
		{
			Nebula.network.sendLargeToPlayer(new PackeSolidSynch(), event.player);
		}
	}
	
	public static class PackeSolidSynch extends PacketAbstract
	{
		@Override
		public IPacket process(Network network) throws IOException
		{
			return null;
		}
		
		@Override
		protected void encode(PacketBufferExt output) throws IOException
		{
			Solid.REGISTRY.serialize(output);
		}
		
		@Override
		protected void decode(PacketBufferExt input) throws IOException
		{
			Log.reset();
			Solid.REGISTRY.deserialize(input, Log::cache);
			Log.logCachedInformations(Object::toString, "Some missing solid name found: ");
		}
	}
	
}