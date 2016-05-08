package farcore.network;

import java.util.EnumMap;
import java.util.List;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import farcore.lib.collection.Register;
import farcore.util.U;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkBasic extends MessageToMessageCodec<FMLProxyPacket, IPacket>
{
	static final Register<NetworkBasic> register = new Register();
	
	public static NetworkBasic getNetwork(String name)
	{
		return register.get(name);
	}
	
	public void registerPacket(Class<? extends IPacket> packet, Side side)
	{
		if(packetTypes.contain(packet.getName())) throw new IllegalArgumentException("Duplicate Packet! " + id);
		if(id >= 256) throw new ArrayIndexOutOfBoundsException(id);
		packetTypes.register(id, packet.getName(), packet);
		++id;
	}

	private final EnumMap<Side, FMLEmbeddedChannel> channel;
	private final String channelName;
	private Register<Class<? extends IPacket>> packetTypes = new Register(256);
	private int id = 0;
	
	protected NetworkBasic(String name)
	{
		this.channelName = name;
		this.channel = NetworkRegistry.INSTANCE.newChannel(name, new ChannelHandler[]{this, new HandlerClient(this), new HandlerServer(this)});
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IPacket msg,
			List<Object> out) throws Exception
	{
		out.add(new FMLProxyPacket(
				msg.encode(Unpooled.buffer().writeByte(packetTypes.id(msg.getClass()))).copy(), (String) ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get()));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg,
			List<Object> out) throws Exception
	{
	    ByteBuf buf = msg.payload();
	    int id = buf.readByte();
	    if (!packetTypes.contain(id))
	    {
	    	FMLLog.warning("Your Version of '" + channelName + "' definetly does not match the Version installed on the Server you joined! Do not report this as a Bug! You failed to install the proper Version of '" + channelName + "' all by yourself!");
	    } 
	    else
	    {
	    	IPacket packet;
	    	try
	    	{
	    		packet = packetTypes.get(id).newInstance();
	    		packet.decode(buf);
	    		packet.side(msg.getTarget());
	    		packet.handler(msg.handler());
	    		out.add(packet);
	    	}
	    	catch(Exception e)
	    	{
	    		throw e;
	    	}
	    }
	}
	
	@ChannelHandler.Sharable
	static final class HandlerClient extends SimpleChannelInboundHandler<IPacket>
	{
	    public final NetworkBasic network;
	    
	    public HandlerClient(NetworkBasic network)
	    {
	    	this.network = network;
	    }
	    
	    protected void channelRead0(ChannelHandlerContext ctx, IPacket packet)
	      throws Exception
	    {
	    	IPacket obj = packet.process(network);
	    	if(obj != null)
	    	{
		    	Channel tChannel = ctx.channel();
		    	IPacket pkt = (IPacket) obj;
			    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
			    tChannel.writeAndFlush(pkt);
	    	}
	    }
	    
	    @Override
	    public boolean acceptInboundMessage(Object msg) throws Exception
	    {
	    	return super.acceptInboundMessage(msg) && ((IPacket) msg).getSide().isClient();
	    }
	}
	
	@ChannelHandler.Sharable
	static final class HandlerServer extends SimpleChannelInboundHandler<IPacket>
	{
		public final NetworkBasic network;
	    
	    public HandlerServer(NetworkBasic network)
	    {
	    	this.network = network;
	    }
	    
	    protected void channelRead0(ChannelHandlerContext ctx, IPacket packet)
	      throws Exception
	    {	    	
	    	IPacket retPacket = packet.process(network);
	    	if(retPacket != null)
	    	{
		    	Channel tChannel = ctx.channel();
		    	if(packet.getPlayer() != null)
		    	{
					tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
				    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(packet.getPlayer());
		    	}
		    	else
		    	{
		    		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		    	}
	    		tChannel.writeAndFlush(retPacket);
	    	}
	    }
	    
	    @Override
	    public boolean acceptInboundMessage(Object msg) throws Exception
	    {
	    	return super.acceptInboundMessage(msg) && ((IPacket) msg).getSide().isServer();
	    }
	}
	
	public FMLEmbeddedChannel getChannel(Side side)
	{
		return channel.get(side);
	}
	
	public void sendTo(IPacket packet)
	{
		if(U.Sides.isSimulating())
		{
			if(U.Worlds.player() instanceof EntityPlayerMP)
				sendToPlayer(packet, U.Worlds.player());
		}
		else
		{
			sendToServer(packet);
		}
	}
	
	public void sendToAll(IPacket packet)
	{
		if (packet == null)
		{
			return;
		}
		FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
	    tChannel.writeAndFlush(packet);
	}
	
	public void sendToPlayer(IPacket packet, EntityPlayer player)
	{
		if(!(player instanceof EntityPlayerMP))
		{
			return;
		}
		if (packet == null)
		{
			return;
		}
		FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
	    tChannel.writeAndFlush(packet);
	}

	public void sendToDim(IPacket packet, int dim)
	{
		if (packet == null)
		{
			return;
		}
		FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dim);
	    tChannel.writeAndFlush(packet);
	}

	public void sendToServer(IPacket packet)
	{
	    if (packet == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.CLIENT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
	    tChannel.writeAndFlush(packet);
	}

	public void sendToNearBy(IPacket packet, int dim, int x, int y,
			int z, float range)
	{
	    if (packet == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dim, x + 0.5D, y + 0.5D, z + 0.5D, range));
	    tChannel.writeAndFlush(packet);
	}

	public void sendToNearBy(IPacket packet, TargetPoint point)
	{
	    if (packet == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
	    tChannel.writeAndFlush(packet);
	}
}