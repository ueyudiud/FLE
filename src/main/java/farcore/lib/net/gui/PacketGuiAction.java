package farcore.lib.net.gui;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import farcore.interfaces.gui.IGuiUpdatable;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class PacketGuiAction extends PacketAbstract
{
	private int type;
	private int contain;
	
	public PacketGuiAction()
	{
		
	}
	public PacketGuiAction(int type, int contain)
	{
		this.type = type;
		this.contain = contain;
	}

	@Override
	public IPacket process(NetworkBasic network)
	{
		EntityPlayer player = getPlayer();
		if(player.openContainer instanceof IGuiUpdatable)
		{
			((IGuiUpdatable) player.openContainer).onActive(type, contain);
		}
		return null;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeInt(type);
		output.writeInt(contain);
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		type = input.readInt();
		contain = input.readInt();
	}
}