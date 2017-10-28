/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.gui.ContainerBase;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
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
		this.stacks = new FluidStack[container.getFluidSlots().size()];
		for(int i = 0; i < this.stacks.length; ++i)
		{
			this.stacks[i] = container.getFluidSlots().get(i).getStackInSlot();
		}
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.stacks.length);
		for (FluidStack stack : this.stacks)
		{
			output.writeFluidStack(stack);
		}
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.stacks = new FluidStack[input.readByte()];
		for(int i = 0; i < this.stacks.length; ++i)
		{
			this.stacks[i] = input.readFluidStack();
		}
	}
	
	@Override
	public IPacket process(Network network)
	{
		ContainerBase container = container();
		if(container != null)
		{
			for(int i = 0; i < this.stacks.length; ++i)
			{
				container.getFluidSlots().get(i).putStack(this.stacks[i]);
			}
		}
		return null;
	}
}