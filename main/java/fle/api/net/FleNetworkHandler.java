package fle.api.net;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import fle.api.te.IObjectInWorld;

public interface FleNetworkHandler 
{
	FMLEmbeddedChannel getChannel(Side paramSide);
	  
	<T extends IPacket> void registerMessage(Class<? extends T> aType, Side side);

	void sendTo(IPacket aPacket);
	
	void sendToPlayer(IPacket aPacket, EntityPlayer aPlayer);

	void sendToDim(IPacket aPacket, int dim);

	void sendToServer(IPacket aPacket);

	void sendToNearBy(IPacket aPacket, int dim, int x, int y, int z, float range);

	void sendToNearBy(IPacket aPacket, IObjectInWorld obj, float range);

	void sendLargePacket(IPacket aPacket, TargetPoint aPoint);

	void onPacket(int id, ByteArrayDataInput subData);

	IPacketMaker getPacketMaker();
}