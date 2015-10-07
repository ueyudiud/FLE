package fle.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fle.FLE;
import fle.api.net.FLELargePacket;
import fle.api.net.FLENBTPacket;
import fle.api.net.FleAbstractPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.net.FlePackets.CoderFWMUpdate;
import fle.api.net.FlePackets.CoderGuiUpdate;
import fle.api.net.FlePackets.CoderInventoryUpdate;
import fle.api.net.FlePackets.CoderMatterUpdate;
import fle.api.net.FlePackets.CoderPTUpdate;
import fle.api.net.FlePackets.CoderSolidTankUpdate;
import fle.api.net.FlePackets.CoderTankUpdate;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.core.net.FlePackets.CoderCropUpdate;
import fle.core.net.FlePackets.CoderKeyType;

public class NetWorkHandler implements FleNetworkHandler
{
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(FLE.MODID.toLowerCase());
	private static final Map<Integer, Class<? extends FleAbstractPacket>> map = new HashMap();
	private static final Map<Class<? extends FleAbstractPacket>, Integer> idMap = new HashMap();
	
	private int idx = 0;

	public void init()
	{
		registerMessage(CoderFWMUpdate.class, Side.CLIENT);
		registerMessage(CoderKeyType.class, Side.SERVER);
		registerMessage(CoderGuiUpdate.class, Side.SERVER);
		registerMessage(CoderInventoryUpdate.class, Side.CLIENT);
		registerMessage(CoderTankUpdate.class, Side.CLIENT);
		registerMessage(FLENBTPacket.class, Side.CLIENT);
		registerMessage(CoderPTUpdate.class, Side.CLIENT);
		registerMessage(CoderTileUpdate.class, Side.CLIENT);
		registerMessage(CoderCropUpdate.class, Side.CLIENT);
		registerMessage(CoderMatterUpdate.class, Side.CLIENT);
		registerMessage(CoderSolidTankUpdate.class, Side.CLIENT);
		registerMessage(FLELargePacket.class, Side.CLIENT);
	}
	
	@Override
	public <T extends FleAbstractPacket> void registerMessage(Class<? extends T> aType, Side side)
	{
		try
		{
			map.put(idx, aType);
			idMap.put(aType, idx);
			instance.registerMessage(aType.newInstance(), aType, idx, side);
		} 
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		++idx;
	}
	
	@Override
	public void sendTo(FleAbstractPacket aPacket)
	{
		if(FLE.fle.getPlatform().isSimulating())
			instance.sendToAll(aPacket);
		else instance.sendToServer(aPacket);
	}
	
	@Override
	public void sendToPlayer(FleAbstractPacket aPacket, EntityPlayerMP aPlayer)
	{
		instance.sendTo(aPacket, aPlayer);
	}
	
	@Override
	public void sendToDim(FleAbstractPacket aPacket, int dim)
	{
		instance.sendToDimension(aPacket, dim);
	}
	
	@Override
	public void sendToServer(FleAbstractPacket aPacket)
	{
		instance.sendToServer(aPacket);
	}
	
	@Override
	public void sendToNearBy(FleAbstractPacket aPacket, TargetPoint aPoint)
	{
		instance.sendToAllAround(aPacket, aPoint);
	}
	
	public void sendLargePacket(FleAbstractPacket aPacket, TargetPoint aPoint)
	{
		try
		{
			ByteBuf buffer = new UnpooledByteBufAllocator(true).buffer();
			aPacket.init(buffer, false);
		    byte[] data;
			@SuppressWarnings("resource")
			ByteBufInputStream input = new ByteBufInputStream(Unpooled.unmodifiableBuffer(buffer));
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
		    	int id = idMap.get(aPacket.getClass());
		    	state |= id << 2;
		    	os.writeInt(state);
		    	os.write(data, offset, Math.min(maxSize, data.length - offset));
		    	sendToNearBy(new FLELargePacket(osRaw.toByteArray()), aPoint);
		    }
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public void onPacket(int typeID, ByteBuf subData, MessageContext ctx) throws Throwable
	{
		FleAbstractPacket pkg = map.get(typeID).newInstance();
		pkg.fromBytes(subData);
		pkg.onMessage(pkg, ctx);
	}
}