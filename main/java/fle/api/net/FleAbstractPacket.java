package fle.api.net;

import fle.api.FleAPI;
import fle.api.util.FleDataOutputStream;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public final class FleAbstractPacket
{
	public FMLProxyPacket pkt;

	public FleAbstractPacket(IFlePacketCoder aCoder) 
	{
		pkt = new FMLProxyPacket(Unpooled.wrappedBuffer(init(aCoder)), FleAPI.MODID);
	}

	private byte[] init(IFlePacketCoder aCoder)
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		FleDataOutputStream os = new FleDataOutputStream(new DataOutputStream(buffer));
		try
		{
			os.writeInt(aCoder.getCoderID());
			aCoder.createPacket(os);
			os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}
}