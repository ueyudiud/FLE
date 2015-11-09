package fle.api.net;

import java.io.IOException;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fle.FLE;
import fle.api.FleAPI;

public abstract class IPacket
{
	
	NetHandlerPlayServer server;
	NetHandlerPlayClient client;
	
	public final void a(INetHandler s)
	{
        if (s instanceof NetHandlerPlayClient)
        {
            client = (NetHandlerPlayClient) s;
        }
        else if (s instanceof NetHandlerPlayServer)
        {
            server = (NetHandlerPlayServer) s;
        }
	}
	
	public abstract ByteArrayDataOutput encode() throws IOException;
	
	public abstract void decode(ByteArrayDataInput byteArrayDataInput) throws IOException;
	
	public abstract Object process(FleNetworkHandler nwh);
	
	public EntityPlayerMP getPlayer()
	{
		return (EntityPlayerMP) (server != null ? server.playerEntity : FleAPI.mod.getPlatform().getPlayerInstance());
	}

	private Side side;
	
	public void side(Side target)
	{
		side = target;
	}
	
	public Side getSide()
	{
		return side;
	}
}