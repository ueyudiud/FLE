package farcore.lib.net.gui;

import java.io.IOException;

import farcore.lib.container.ContainerBase;
import farcore.lib.container.FluidSlot;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class PacketFluidUpdate extends PacketAbstract
{
	private int index;
	private FluidStack stack;

	public PacketFluidUpdate()
	{
		
	}
	public PacketFluidUpdate(ContainerBase<?> container, int index)
	{
		this(index, container.fluidSlotList.get(index).getStack());
	}
	public PacketFluidUpdate(int index, FluidStack stack)
	{
		this.index = index;
		this.stack = stack;
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		EntityPlayer player = getPlayer();
		if(player.openContainer instanceof ContainerBase)
		{
			((FluidSlot) ((ContainerBase) player.openContainer).fluidSlotList.get(index)).setStack(stack);
		}
		return null;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeShort(index);
		output.writeFluidStack(stack);
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		index = input.readShort();
		stack = input.readFluidStack();
	}
}