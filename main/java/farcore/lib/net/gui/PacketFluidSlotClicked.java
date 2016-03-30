package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.container.ContainerBase;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;

public class PacketFluidSlotClicked extends PacketAbstract
{
	int index;
	
	public PacketFluidSlotClicked(int index)
	{
		this.index = index;
	}

	@Override
	public IPacket process(NetworkBasic network)
	{
		if(getPlayer().openContainer instanceof ContainerBase)
		{
			((ContainerBase) getPlayer().openContainer).slotFluidClick(index, getPlayer());
		}
		return null;
	}

	@Override
	protected void encode(DataStream output) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void decode(DataStream input) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}