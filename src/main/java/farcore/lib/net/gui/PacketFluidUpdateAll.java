package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.ContainerBase;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;
import net.minecraftforge.fluids.FluidStack;

public class PacketFluidUpdateAll extends PacketGui
{
	private FluidStack[] stacks;
	
	public PacketFluidUpdateAll()
	{
		
	}
	public PacketFluidUpdateAll(ContainerBase container)
	{
		super(container);
		stacks = new FluidStack[container.fluidSlots.size()];
		for(int i = 0; i < stacks.length; ++i)
		{
			stacks[i] = container.fluidSlots.get(i).getStackInSlot();
		}
	}

	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(stacks.length);
		for (FluidStack stack : stacks)
		{
			output.writeFluidStack(stack);
		}
	}

	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		stacks = new FluidStack[input.readByte()];
		for(int i = 0; i < stacks.length; ++i)
		{
			stacks[i] = input.readFluidStack();
		}
	}

	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container != null)
		{
			for(int i = 0; i < stacks.length; ++i)
			{
				container.fluidSlots.get(i).putStack(stacks[i]);
			}
		}
		return null;
	}
}