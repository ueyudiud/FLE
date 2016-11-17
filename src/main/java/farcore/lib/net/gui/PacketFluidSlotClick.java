package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.ContainerBase;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;

public class PacketFluidSlotClick extends PacketGui
{
	private byte clickID;
	
	public PacketFluidSlotClick()
	{
	}
	public PacketFluidSlotClick(ContainerBase container, int id)
	{
		super(container);
		clickID = (byte) id;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(clickID);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		clickID = input.readByte();
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container != null)
		{
			container.fluidSlots.get(clickID).onSlotClick(getPlayer(), getPlayer().inventory.getItemStack());
		}
		return null;
	}
}