package fle.core.net;

import io.netty.buffer.ByteBufInputStream;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.net.FleAbstractPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.net.IFlePacketCoder;
import fle.api.net.IFlePacketDecoder;
import fle.api.net.IFlePacketHandler;
import fle.api.util.FleDataInputStream;
import fle.core.net.FlePackets.CoderFWMUpdate;
import fle.core.net.FlePackets.CoderGuiUpdate;
import fle.core.net.FlePackets.CoderInventorUpdate;
import fle.core.net.FlePackets.CoderKeyType;
import fle.core.net.FlePackets.CoderNBTUpdate;
import fle.core.net.FlePackets.CoderPTUpdate;
import fle.core.net.FlePackets.CoderTileUpdate;

public class NetWorkHandler implements FleNetworkHandler
{
	static FMLEventChannel channel;
	private static final Map<Class<? extends IFlePacketCoder>, Class<? extends IFlePacketDecoder>> coderMap = new HashMap();
	private static final Map<Integer, Class<? extends IFlePacketDecoder>> decoderMap = new HashMap();
	public static final int tileUpdateID = 1;
	public static final int guiUpdateID = 2;
	public static final int inventoryUpdateID = 3;
	public static final int fwmID = 4;
	public static final int ptID = 5;
	public static final int keyID = 6;

	public void init()
	{
		if(channel == null)
			channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(FleAPI.MODID);
		channel.register(this);
	}
	
	public NetWorkHandler() 
	{
		registerCoder(CoderKeyType.class);
		registerCoder(CoderTileUpdate.class);
		registerCoder(CoderGuiUpdate.class);
		registerCoder(CoderInventorUpdate.class);
		registerCoder(CoderFWMUpdate.class);
		registerCoder(CoderPTUpdate.class);
		registerCoder(CoderNBTUpdate.class);
	}

	public void registerCoder(Class<? extends IFlePacketHandler> clazz)
	{
		registerCoder(clazz, clazz);
	}
	@Override
	public void registerCoder(Class<? extends IFlePacketCoder> clazz1, Class<? extends IFlePacketDecoder> clazz2)
	{
		try
		{
			if(clazz1 == null || clazz2 == null) throw new NullPointerException();
			coderMap.put(clazz1, clazz2);
			decoderMap.put(clazz1.newInstance().getCoderID(), clazz2);
		}
		catch(Throwable e)
		{
			throw new RuntimeException(e);
		}
	}

	@SubscribeEvent
	public void readPkt(ClientCustomPacketEvent evt)
	{
		onPacketData(new ByteBufInputStream(evt.packet.payload()), Minecraft.getMinecraft().thePlayer, Side.CLIENT);
	}
	
	@SubscribeEvent
	public void readPkt(ServerCustomPacketEvent evt)
	{
		onPacketData(new ByteBufInputStream(evt.packet.payload()), ((NetHandlerPlayServer)evt.handler).playerEntity, Side.SERVER);
	}
	
	protected void onPacketData(InputStream isRaw, EntityPlayer player, Side side)
	{
		try 
		{
			if (isRaw.available() == 0)
				return;
			DataInputStream isRaw1 = new DataInputStream(isRaw);
			int id = isRaw1.readInt();
			FleDataInputStream is = new FleDataInputStream(isRaw1);
			IFlePacketDecoder tDecoder = decoderMap.get(new Integer(id)).newInstance();
			if(tDecoder != null)
			{
				tDecoder.decode(is);
				tDecoder.process(side, player);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sendTo(IFlePacketCoder aCoder) 
	{
		FMLProxyPacket packet = new FleAbstractPacket(aCoder).pkt;
		if(FLE.fle.getPlatform().isSimulating())
			channel.sendToAll(packet);
		else
			channel.sendToServer(packet);
	}

	@Override
	public void sendToPlayer(IFlePacketCoder aCoder, EntityPlayerMP aPlayer) 
	{
		channel.sendTo(new FleAbstractPacket(aCoder).pkt, aPlayer);
	}

	@Override
	public void sendToAllAround(IFlePacketCoder aCoder, TargetPoint aPoint) 
	{
		channel.sendToAllAround(new FleAbstractPacket(aCoder).pkt, aPoint);
	}

	@Override
	public void sendToServer(IFlePacketCoder aCoder) 
	{
		channel.sendToServer(new FleAbstractPacket(aCoder).pkt);
	}
}