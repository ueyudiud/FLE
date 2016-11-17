package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.ContainerBase;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;
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
		output.writeByte(id);
		output.writeFluidStack(stack);
	}

	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		id = input.readByte();
		stack = input.readFluidStack();
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container != null)
		{
			container.fluidSlots.get(id).putStack(stack);
		}
		return null;
	}
}