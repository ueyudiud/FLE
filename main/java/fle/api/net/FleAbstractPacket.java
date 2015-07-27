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

public abstract class FleAbstractPacket<I extends PacketInfo> extends FlePacket<I>
{
	private FMLProxyPacket pkt;
	private EntityPlayerMP player;

	protected FleAbstractPacket(EntityPlayerMP aPlayer, I aInfo) 
	{
		player = aPlayer;
		pkt = new FMLProxyPacket(Unpooled.wrappedBuffer(init(aInfo)), FleAPI.MODID);
	}
	protected FleAbstractPacket(I aInfo) 
	{
		pkt = new FMLProxyPacket(Unpooled.wrappedBuffer(init(aInfo)), FleAPI.MODID);
	}

	private byte[] init(I aInfo)
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		FleDataOutputStream os = new FleDataOutputStream(new DataOutputStream(buffer));
		createPacket(os, aInfo);
		try
		{
			os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}
	
	public abstract void createPacket(FleDataOutputStream aStream, I aInfo);

	@Override
	public void sendPacket() 
	{
		if (FleAPI.mod.getPlatform().isSimulating())
			FleAPI.mod.getNetworkHandler().getChannel().sendToAll(pkt);
		else
			FleAPI.mod.getNetworkHandler().getChannel().sendToServer(pkt);
	}

	@Override
	public void sendPacket(EntityPlayerMP player) 
	{
		FleAPI.mod.getNetworkHandler().getChannel().sendTo(pkt, player);
	}

	@Override
	public void sendToAllAround(TargetPoint aParamTargetPoint) 
	{
		FleAPI.mod.getNetworkHandler().getChannel().sendToAllAround(pkt, aParamTargetPoint);
	}
}