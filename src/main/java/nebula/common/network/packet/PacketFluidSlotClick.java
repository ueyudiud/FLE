/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.gui.ContainerBase;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;

public class PacketFluidSlotClick extends PacketGui
{
	private byte clickID;
	
	public PacketFluidSlotClick()
	{
	}
	
	public PacketFluidSlotClick(ContainerBase container, int id)
	{
		super(container);
		this.clickID = (byte) id;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.clickID);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.clickID = input.readByte();
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if (container != null)
		{
			container.getFluidSlots().get(this.clickID).onSlotClick(getPlayer(), getPlayer().inventory.getItemStack());
		}
		return null;
	}
}
