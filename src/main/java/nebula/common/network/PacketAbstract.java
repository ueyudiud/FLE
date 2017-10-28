/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The template packet.
 * @author ueyudiud
 */
public abstract class PacketAbstract implements IPacket
{
	protected Side side;
	protected INetHandler handler;
	
	@Override
	public Side getSide()
	{
		return this.side;
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
		return (this.handler instanceof NetHandlerPlayServer) ?
				((NetHandlerPlayServer) this.handler).playerEntity :
					Players.player();
	}
	
	@Override
	public ByteBuf encode(ByteBuf buf) throws IOException
	{
		encode(new PacketBufferExt(buf));
		return buf;
	}
	
	protected abstract void encode(PacketBufferExt output) throws IOException;
	
	@Override
	public void decode(ByteBuf buf) throws IOException
	{
		decode(new PacketBufferExt(buf));
	}
	
	protected abstract void decode(PacketBufferExt input) throws IOException;
	
	@Override
	public boolean needToSend()
	{
		return true;
	}
}