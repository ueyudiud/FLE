package nebula.common.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nebula.Log;
import nebula.Nebula;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;

public class PacketLarge implements IPacket
{
	protected Side side;
	protected INetHandler handler;
	
	private byte[] bs;
	private static volatile ByteBuf largePacketCache;
	
	private int id = 0;
	private boolean flag = false;
	
	public PacketLarge()
	{
		
	}
	public PacketLarge(byte[] arrays)
	{
		this.bs = arrays;
	}
	
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
		buf.writeShort(this.bs.length);
		buf.writeBytes(this.bs);
		return buf;
	}
	
	@Override
	public void decode(ByteBuf buf) throws IOException
	{
		byte[] b = new byte[buf.readShort()];
		buf.readBytes(b);
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(b));
		int type = stream.readInt();
		this.id = type >> 2;
		if((type & 0x1) != 0)
		{
			largePacketCache = Unpooled.buffer();
		}
		byte[] buffer = new byte[4096];
		int len;
		while ((len = stream.read(buffer)) != -1)
		{
			largePacketCache.writeBytes(buffer, 0, len);
		}
		if((type & 0x2) != 0)
		{
			this.flag = true;
		}
	}
	
	@Override
	public IPacket process(Network network)
	{
		if (!this.flag) return null;
		try
		{
			network.processPacket(this.id, Unpooled.wrappedBuffer(largePacketCache), this.side, this.handler);
			largePacketCache = null;
		}
		catch(Throwable exception)
		{
			if(Nebula.debug)
				throw new RuntimeException(exception);
			Log.warn("Fail to process packet.", exception);
		}
		return null;
	}
	
	@Override
	public boolean needToSend()
	{
		return true;
	}
}