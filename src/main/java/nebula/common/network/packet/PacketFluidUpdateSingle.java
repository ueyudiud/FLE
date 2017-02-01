package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.gui.ContainerBase;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
import net.minecraftforge.fluids.FluidStack;

public class PacketFluidUpdateSingle extends PacketGui
{
	private int id;
	private FluidStack stack;
	
	public PacketFluidUpdateSingle(ContainerBase container, int id, FluidStack stack)
	{
		super(container);
		this.id = id;
		this.stack = stack;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.id);
		output.writeFluidStack(this.stack);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.id = input.readByte();
		this.stack = input.readFluidStack();
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container != null)
		{
			container.fluidSlots.get(this.id).putStack(this.stack);
		}
		return null;
	}
}