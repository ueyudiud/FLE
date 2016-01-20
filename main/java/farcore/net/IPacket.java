package farcore.net;

import java.io.IOException;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import flapi.FleAPI;

public abstract class IPacket
{
	NetHandlerPlayServer server;
	NetHandlerPlayClient client;
	
	public final void initPacket(INetHandler s)
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
		
	public abstract PacketBuffer encode(FlePacketBuffer buffer) throws IOException;
	
	public abstract void decode(FlePacketBuffer buffer) throws IOException;
	
	public abstract IPacket process(INetworkHandler handler);
	
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