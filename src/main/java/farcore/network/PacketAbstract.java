package farcore.network;

import java.io.IOException;

import farcore.util.U;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketAbstract implements IPacket
{
	protected Side side;
	protected INetHandler handler;

	@Override
	public Side getSide()
	{
		return side;
	}

	@Override
	public void side(Side side)
	{
		this.side = side;
	}

	@Override
	public void handler(INetHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public EntityPlayer getPlayer()
	{
		return (handler instanceof NetHandlerPlayServer) ?
				((NetHandlerPlayServer) handler).playerEntity :
					U.Players.player();
	}

	@Override
	public ByteBuf encode(ByteBuf buf) throws IOException
	{
		encode(new PacketBuffer(buf));
		return buf;
	}

	protected abstract void encode(PacketBuffer output) throws IOException;

	@Override
	public void decode(ByteBuf buf) throws IOException
	{
		decode(new PacketBuffer(buf));
	}

	protected abstract void decode(PacketBuffer input) throws IOException;

	@Override
	public boolean needToSend()
	{
		return true;
	}
}