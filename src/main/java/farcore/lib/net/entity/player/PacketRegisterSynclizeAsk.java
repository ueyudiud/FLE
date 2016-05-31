package farcore.lib.net.entity.player;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import farcore.handler.FarCoreDataHandler;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public class PacketRegisterSynclizeAsk extends PacketAbstract
{
	@Override
	public IPacket process(NetworkBasic network)
	{
		FarCoreDataHandler.addSynclizePlayer(getPlayer());
		return null;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		
	}
}