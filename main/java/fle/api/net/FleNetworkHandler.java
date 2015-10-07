package fle.api.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public interface FleNetworkHandler 
{
	<T extends FleAbstractPacket> void registerMessage(Class<? extends T> aType, Side side);

	void sendTo(FleAbstractPacket aPacket);

	void sendToPlayer(FleAbstractPacket aPacket, EntityPlayerMP aPlayer);

	void sendToDim(FleAbstractPacket aPacket, int dim);

	void sendToServer(FleAbstractPacket aPacket);

	void sendToNearBy(FleAbstractPacket aPacket, TargetPoint aPoint);

	void sendLargePacket(FleAbstractPacket aPacket, TargetPoint aPoint);
	
	void onPacket(int typeID, ByteBuf subData, MessageContext ctx) throws Throwable;
}