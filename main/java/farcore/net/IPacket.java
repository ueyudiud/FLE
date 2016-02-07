package farcore.net;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import farcore.FarCore;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

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
	
	public abstract Object process(INetworkHandler nwh);
	
	public EntityPlayerMP getPlayer()
	{
		return (EntityPlayerMP) (server != null ? server.playerEntity : FarCore.getPlayerInstance());
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