package fle.core.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumMap;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import farcore.FarCore;
import farcore.collection.Register;
import farcore.net.FleEntityPacket;
import farcore.net.FleFallingBlockPacket;
import farcore.net.FleNBTPacket;
import farcore.net.FleTEPacket;
import farcore.net.INetworkHandler;
import farcore.net.IPacket;
import farcore.util.FleLog;
import farcore.world.BlockPos;
import farcore.world.IObjectInWorld;
import fle.core.FLE;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

@ChannelHandler.Sharable
public class NWH extends MessageToMessageCodec<FMLProxyPacket, IPacket> implements INetworkHandler
{
	private static Register<NWH> register = new Register();
	
	public static NWH init()
	{		
		NWH nwh = new NWH("fle");

//		nwh.registerMessage(FluidWindowPacket.class, Side.CLIENT);
//		nwh.registerMessage(SolidWindowPacket.class, Side.CLIENT);
//		nwh.registerMessage(FluidUpdatePacket.class, Side.CLIENT);
//		nwh.registerMessage(SolidUpdatePacket.class, Side.CLIENT);
		nwh.registerMessage(FleKeyTypePacket.class, Side.SERVER);
		nwh.registerMessage(FleGuiPacket.class, Side.SERVER);
		nwh.registerMessage(FleFallingBlockPacket.class, Side.CLIENT);
		nwh.registerMessage(FleEntityPacket.class, Side.CLIENT);
		nwh.registerMessage(FleNBTPacket.class, Side.CLIENT);
		nwh.registerMessage(FleTileLoadedAskSyncPacket.class, Side.SERVER);
		nwh.registerMessage(FleTileLoadedSyncPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleSyncAskFWMPacket.class, Side.SERVER);
//		nwh.registerMessage(FleSyncAskTileMetaPacket.class, Side.SERVER);
//		nwh.registerMessage(FleSyncFWMSmallPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleInventoryPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleFluidTankPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleSolidTankPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleNBTPacket.class, Side.CLIENT);
//		nwh.registerMessage(FlePlayerTechPacket.class, Side.CLIENT);
		nwh.registerMessage(FleTEPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleCropUpdatePacket.class, Side.CLIENT);
		nwh.registerMessage(FleLargePacket.class, Side.CLIENT);
//		nwh.registerMessage(FleWorldMetaSyncPacket.class, Side.CLIENT);
//		nwh.registerMessage(FleBaseTEPacket.class, Side.CLIENT);
		return nwh;
	}
	
	public static NWH createNewHandler(String name)
	{
		if(register.contain(name))
		{
			FleLog.getLogger().info("A mod has already created handler with name : " + name);
			return register.get(name);
		}
		NWH nw = new NWH(name);
		register.register(nw, name);
		return nw;
	}
	
	public final String getChannelName()
	{
		return channelName;
	}
	
	private final EnumMap<Side, FMLEmbeddedChannel> channel;
	private final String channelName;
	private Register<Class<? extends IPacket>> packetTypes = new Register();
	private int index = 0;

	private NWH(String name)
	{
		channelName = name;
		channel = NetworkRegistry.INSTANCE.newChannel(name, new ChannelHandler[]{this, new HandlerClient(this), new HandlerServer(this)});
		packetTypes = new Register<Class<? extends IPacket>>(256);
	}
	  
	@Override
	public FMLEmbeddedChannel getChannel(Side side)
	{
		return channel.get(side);
	}

	@Override
	public <T extends IPacket> void registerMessage(
			Class<? extends T> aType, Side side)
	{
		if(packetTypes.contain(aType.getName())) throw new IllegalArgumentException("Duplicate Packet! " + index);
		if(index >= 256) throw new ArrayIndexOutOfBoundsException(index);
		packetTypes.register(index, aType, aType.getName());
		++index;
	}

	@Override
	public void sendTo(IPacket aPacket)
	{
		if(FarCore.isSimulating())
		{
			if(FarCore.getPlayerInstance() instanceof EntityPlayerMP)
				sendToPlayer(aPacket, FarCore.getPlayerInstance());
		}
		else
		{
			sendToServer(aPacket);
		}
	}
	
	@Override
	public void sendToPlayer(IPacket aPacket, EntityPlayer aPlayer)
	{
		if(!(aPlayer instanceof EntityPlayerMP))
		{
			return;
		}
		if (aPacket == null)
		{
			return;
		}
		FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(aPlayer);
	    tChannel.writeAndFlush(aPacket);
	}

	@Override
	public void sendToDim(IPacket aPacket, int dim)
	{
		if (aPacket == null)
		{
			return;
		}
		FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dim);
	    tChannel.writeAndFlush(aPacket);
	}

	@Override
	public void sendToServer(IPacket aPacket)
	{
	    if (aPacket == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.CLIENT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
	    tChannel.writeAndFlush(aPacket);
	}

	@Override
	public void sendToNearBy(IPacket aPacket, int dim, int x, int y,
			int z, float range)
	{
	    if (aPacket == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dim, x + 0.5D, y + 0.5D, z + 0.5D, range));
	    tChannel.writeAndFlush(aPacket);
	}

	@Override
	public void sendToNearBy(IPacket aPacket, IObjectInWorld obj,
			float range)
	{
		BlockPos pos = obj.pos();
		sendToNearBy(aPacket, pos.t, pos.x, pos.y, pos.z, range);
	}

	public void sendToNearBy(IPacket aPacket, TargetPoint aPoint)
	{
	    if (aPacket == null)
	    {
	    	return;
	    }
	    FMLEmbeddedChannel tChannel = getChannel(Side.SERVER);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
	    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(aPoint);
	    tChannel.writeAndFlush(aPacket);
	}
	
	@Override
	public void sendLargePacket(IPacket aPacket, TargetPoint aPoint)
	{
		try
		{
		    byte[] data = aPacket.encode().toByteArray();
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			ByteArrayOutputStream buf = new ByteArrayOutputStream(16384);
	        int len;
			byte[] cache = new byte[4096];
			while ((len = input.read(cache)) != -1)
			{
				buf.write(cache, 0, len);
			}
			data = buf.toByteArray();
		    
		    int maxSize = Short.MAX_VALUE - 5;
		    for (int offset = 0; offset < data.length; offset += maxSize)
		    {
		    	ByteArrayOutputStream osRaw = new ByteArrayOutputStream();
		    	DataOutputStream os = new DataOutputStream(osRaw);
		    	int state = 0;
		    	if (offset == 0)
		    	{
		    		state |= 0x1;
		    	}
		    	
		    	if (offset + maxSize > data.length)
		    	{
		    		state |= 0x2;
		    	}
		    	int id = packetTypes.serial(aPacket.getClass());
		    	state |= id << 2;
		    	os.writeInt(state);
		    	os.write(data, offset, Math.min(maxSize, data.length - offset));
		    	sendToNearBy(new FleLargePacket(osRaw.toByteArray()), aPoint);
		    }
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sendLargePacket(IPacket aPacket, EntityPlayerMP player)
	{
		try
		{
		    byte[] data = aPacket.encode().toByteArray();
			ByteArrayInputStream input = new ByteArrayInputStream(data);
			ByteArrayOutputStream buf = new ByteArrayOutputStream(16384);
	        int len;
			byte[] cache = new byte[4096];
			while ((len = input.read(cache)) != -1)
			{
				buf.write(cache, 0, len);
			}
			data = buf.toByteArray();
		    
		    int maxSize = Short.MAX_VALUE - 5;
		    for (int offset = 0; offset < data.length; offset += maxSize)
		    {
		    	ByteArrayOutputStream osRaw = new ByteArrayOutputStream();
		    	DataOutputStream os = new DataOutputStream(osRaw);
		    	int state = 0;
		    	if (offset == 0)
		    	{
		    		state |= 0x1;
		    	}
		    	
		    	if (offset + maxSize > data.length)
		    	{
		    		state |= 0x2;
		    	}
		    	int id = packetTypes.serial(aPacket.getClass());
		    	state |= id << 2;
		    	os.writeInt(state);
		    	os.write(data, offset, Math.min(maxSize, data.length - offset));
		    	sendToPlayer(new FleLargePacket(osRaw.toByteArray()), player);
		    }
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public void onPacket(int id, ByteArrayDataInput subData)
	{
		try
		{
			IPacket pkg = packetTypes.get(id).newInstance();
			pkg.decode(subData);
			pkg.process(this);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(Level.WARN, e);
		}
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IPacket msg,
			List<Object> out) throws Exception
	{
		out.add(new FMLProxyPacket(Unpooled.buffer().writeByte(packetTypes.serial(msg.getClass())).writeBytes(msg.encode().toByteArray()).copy(), (String) ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get()));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg,
			List<Object> out) throws Exception
	{
	    ByteArrayDataInput aData = ByteStreams.newDataInput(msg.payload().array());
	    int id = aData.readByte();
	    if (!packetTypes.contain(id))
	    {
	    	FMLLog.warning("Your Version of '" + FLE.MODID + "' definetly does not match the Version installed on the Server you joined! Do not report this as a Bug! You failed to install the proper Version of '" + FLE.MODID + "' all by yourself!", new Object[0]);
	    } 
	    else
	    {
	    	IPacket packet;
	    	try
	    	{
	    		packet = packetTypes.get(id).newInstance();
	    		packet.decode(aData);
	    		packet.side(msg.getTarget());
	    		packet.a(msg.handler());
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
	    public final INetworkHandler nwh;
	    
	    public HandlerClient(INetworkHandler aNWH)
	    {
	    	nwh = aNWH;
	    }
	    
	    protected void channelRead0(ChannelHandlerContext ctx, IPacket aPacket)
	      throws Exception
	    {
	    	Object obj = aPacket.process(nwh);
	    	if(obj instanceof IPacket)
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
		public final INetworkHandler nwh;
	    
	    public HandlerServer(INetworkHandler aNWH)
	    {
	    	nwh = aNWH;
	    }
	    
	    protected void channelRead0(ChannelHandlerContext ctx, IPacket aPacket)
	      throws Exception
	    {	    	
	    	Object obj = aPacket.process(nwh);
	    	if(obj instanceof IPacket)
	    	{
		    	Channel tChannel = ctx.channel();
		    	if(aPacket.getPlayer() != null)
		    	{
					tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
				    tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(aPacket.getPlayer());
		    	}
		    	else
		    	{
		    		tChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		    	}
			    tChannel.writeAndFlush(obj);
	    	}
	    }
	    
	    @Override
	    public boolean acceptInboundMessage(Object msg) throws Exception
	    {
	    	return super.acceptInboundMessage(msg) && ((IPacket) msg).getSide().isServer();
	    }
	}
}