package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.IRecievableGui;
import farcore.lib.io.DataStream;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;

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
	public IPacket process(Network network)
	{
		EntityPlayer player = getPlayer();
		if(player.openContainer instanceof IRecievableGui)
		{
			((IRecievableGui) player.openContainer).onActive(type, contain);
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