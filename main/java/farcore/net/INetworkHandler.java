package farcore.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import farcore.world.IObjectInWorld;

/**
 * Standard network handler.
 * It will auto create a new channel, and has two side.(Client and Server)<br>
 * The packet sended need register first and it should have similar register
 * order in two sided. Send packet by instance.<br>
 * @author ueyudiud
 *
 */
public interface INetworkHandler
{
	FMLEmbeddedChannel getChannel(Side side);
	 
	/**
	 * Register new message type, and distribute a index for packet id.
	 * @param type The type be registered.
	 * @param side The send side to. A packet should always send from one side to 
	 * another side.
	 * @see farcore.net.IPacket
	 */
	<T extends IPacket> void registerMessage(Class<? extends T> type, Side side);

	/**
	 * Send an packet with default target.
	 * If this side is client, send to server.
	 * If this side is server, send to player instance(No use yet).
	 * @param aPacket
	 */
	void sendTo(IPacket packet);
	
	void sendToPlayer(IPacket packet, EntityPlayer player);

	void sendToDim(IPacket packet, int dim);

	@SideOnly(Side.CLIENT)
	void sendToServer(IPacket packet);

	void sendToNearBy(IPacket packet, int dim, int x, int y, int z, float range);

	void sendToNearBy(IPacket packet, IObjectInWorld obj, float range);

	/**
	 * Send a large packet (Array length more than 2048).
	 * It will divide a packet to many smaller packet and send them
	 * one by one.<br>
	 * @param aPacket
	 * @param aPoint The target point.
	 */
	void sendLargePacket(IPacket packet, TargetPoint point);

	void sendLargePacket(IPacket packet, EntityPlayerMP player);

	void onPacket(int id, FlePacketBuffer subData);
}