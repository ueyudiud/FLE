package fle.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;

public interface FleNetworkHandler 
{
	public <T extends FleAbstractPacket> void registerMessage(Class<? extends T> aType, Side side);

	public void sendTo(FleAbstractPacket aPacket);

	public void sendToPlayer(FleAbstractPacket aPacket, EntityPlayerMP aPlayer);

	public void sendToDim(FleAbstractPacket aPacket, int dim);

	public void sendToServer(FleAbstractPacket aPacket);

	public void sendToNearBy(FleAbstractPacket aPacket, TargetPoint aPoint);
}