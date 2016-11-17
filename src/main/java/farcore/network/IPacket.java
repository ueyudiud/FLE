package farcore.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.relauncher.Side;

public interface IPacket
{
	/**
	 * Called after decode packet, used to receive message.
	 * @param network
	 * @return If there is packet needed send bake to server/client
	 * return that packet from there.
	 */
	IPacket process(Network network) throws IOException;
	
	/**
	 * Get side when called.
	 * @return
	 */
	Side getSide();
	
	/**
	 * Internal method, just for mark side of packet.
	 * @param side
	 */
	void side(Side side);
	
	/**
	 * Internal method, for mark handler processed packet.
	 * @param handler
	 */
	void handler(INetHandler handler);
	
	/**
	 * Get player of this packet belong:
	 * server side is the sender of packet.
	 * client side is the local player.
	 * @return
	 */
	EntityPlayer getPlayer();
	
	/**
	 * Encode packet.
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	ByteBuf encode(ByteBuf buf) throws IOException;
	
	/**
	 * Decode packet.
	 * @param buf
	 * @throws IOException
	 */
	void decode(ByteBuf buf) throws IOException;
	
	/**
	 * Check if this packet is needed to send to server/client.
	 * @return
	 */
	boolean needToSend();
}