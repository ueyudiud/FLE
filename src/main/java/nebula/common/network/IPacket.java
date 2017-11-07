/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The packet used to encode or decode by {@link nebula.network.Network}.
 * 
 * @author ueyudiud
 */
public interface IPacket
{
	/**
	 * Called after decode packet, used to receive message.
	 * 
	 * @param network the network.
	 * @return if there is packet needed send bake to server/client return that
	 *         packet from there.
	 */
	IPacket process(Network network) throws IOException;
	
	/**
	 * Get which side of the packet is in.
	 * <p>
	 * This method only take effect when processing packet.
	 * 
	 * @return the side.
	 */
	Side getSide();
	
	/**
	 * Internal method, just for mark side of packet.
	 * 
	 * @param side the side.
	 */
	void side(Side side);
	
	/**
	 * Internal method, for mark handler processed packet.
	 * 
	 * @param handler the handler.
	 */
	void handler(INetHandler handler);
	
	/**
	 * Get player of this packet belong :
	 * <li>server side is the sender of packet.
	 * <li>client side is the local player.</li>
	 * <p>
	 * This method only take effect when processing packet.
	 * 
	 * @return the player instance.
	 */
	EntityPlayer getPlayer();
	
	/**
	 * Encode packet.
	 * 
	 * @param buf the buffer to send out.
	 * @return the buffer which is the argument.
	 * @throws IOException
	 */
	ByteBuf encode(ByteBuf buf) throws IOException;
	
	/**
	 * Decode packet.
	 * 
	 * @param buf the buffer to send in.
	 * @throws IOException
	 */
	void decode(ByteBuf buf) throws IOException;
	
	/**
	 * Check if this packet is needed to send to server/client.
	 * 
	 * @return return <code>false</code> and network will cancel the sending
	 *         action.
	 */
	boolean needToSend();
}
