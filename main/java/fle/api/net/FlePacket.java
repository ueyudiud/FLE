package fle.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public abstract class FlePacket<I extends PacketInfo>
{
	public abstract void createPacket(I packetInfo);
	
	public abstract void sendPacket();
	
	public abstract void sendPacket(EntityPlayerMP player);
	
	public abstract void sendToAllAround(NetworkRegistry.TargetPoint paramTargetPoint);
}