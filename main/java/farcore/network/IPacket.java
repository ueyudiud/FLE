package farcore.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public interface IPacket
{
	IPacket process(NetworkBasic network);
	
	Side getSide();

	void side(Side side);

	void handler(INetHandler handler);
	
	EntityPlayerMP getPlayer();

	ByteBuf encode(ByteBuf buf) throws IOException;

	void decode(ByteBuf buf) throws IOException;
}