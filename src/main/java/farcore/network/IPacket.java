package farcore.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.relauncher.Side;

public interface IPacket
{
	IPacket process(Network network);

	Side getSide();

	void side(Side side);

	void handler(INetHandler handler);

	EntityPlayer getPlayer();

	ByteBuf encode(ByteBuf buf) throws IOException;

	void decode(ByteBuf buf) throws IOException;

	/**
	 * Check if this packet is needed to send to server/client.
	 * @return
	 */
	boolean needToSend();
}