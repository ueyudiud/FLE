package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.gui.ContainerBase;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.network.PacketBuffer;
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
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeByte(stacks.length);
		for (FluidStack stack : stacks)
		{
			writeFluidStack(output, stack);
		}
	}
	
	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		stacks = new FluidStack[input.readByte()];
		for(int i = 0; i < stacks.length; ++i)
		{
			stacks[i] = readFluidStack(input);
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