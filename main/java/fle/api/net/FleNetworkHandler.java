package fle.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;

public interface FleNetworkHandler 
{
	public abstract void registerCoder(Class<? extends IFlePacketCoder> clazz1, Class<? extends IFlePacketDecoder> clazz2);
	
	public abstract void sendTo(IFlePacketCoder aPacket);
	
	public abstract void sendToPlayer(IFlePacketCoder aPacket, EntityPlayerMP aPlayer);
	  
	public abstract void sendToAllAround(IFlePacketCoder aPacket, NetworkRegistry.TargetPoint aPoint);
	
	public abstract void sendToServer(IFlePacketCoder aPacket);
}