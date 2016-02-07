package farcore.net;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import farcore.world.IObjectInWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface FleNetworkHandler 
{
	FMLEmbeddedChannel getChannel(Side paramSide);
	  
	<T extends IPacket> void registerMessage(Class<? extends T> type, Side side);

	void sendTo(IPacket aPacket);
	
	void sendToPlayer(IPacket aPacket, EntityPlayer player);

	void sendToDim(IPacket aPacket, int dim);

	void sendToServer(IPacket aPacket);

	void sendToNearBy(IPacket aPacket, int dim, int x, int y, int z, float range);

	void sendToNearBy(IPacket aPacket, IObjectInWorld obj, float range);

	void sendLargePacket(IPacket aPacket, TargetPoint aPoint);

	void sendLargePacket(IPacket aPacket, EntityPlayerMP player);

	void onPacket(int id, ByteArrayDataInput subData);
}