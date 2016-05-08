package farcore.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import farcore.util.U;
import farcore.util.io.DataStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

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
	public EntityPlayerMP getPlayer()
	{
		return (EntityPlayerMP) 
				((handler instanceof NetHandlerPlayServer) ? 
						((NetHandlerPlayServer) handler).playerEntity : 
							U.Worlds.player());
	}
	
	@Override
	public ByteBuf encode(ByteBuf buf) throws IOException
	{
		encode(new DataStream(buf));
		return buf;
	}
	
	protected abstract void encode(DataStream output) throws IOException;

	@Override
	public void decode(ByteBuf buf) throws IOException
	{
		decode(new DataStream(buf));
	}

	protected abstract void decode(DataStream input) throws IOException;
}