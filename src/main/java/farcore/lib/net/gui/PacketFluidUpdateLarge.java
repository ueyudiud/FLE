package farcore.lib.net.gui;

import java.io.IOException;

import farcore.interfaces.tile.IFluidTanks;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.FluidSlot;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.io.DataStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class PacketFluidUpdateLarge extends PacketAbstract
{
	private FluidStack[] stacks;
	
	public PacketFluidUpdateLarge() {}
	public PacketFluidUpdateLarge(ContainerBase<?> container)
	{
		stacks = new FluidStack[container.fluidSlotList.size()];
		for(int i = 0; i < stacks.length; ++i)
		{
			stacks[i] = container.fluidSlotList.get(i).getStack().copy();
		}
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		EntityPlayer player = getPlayer();
		if(player.openContainer instanceof ContainerBase)
		{
			for(int i = 0; i < stacks.length; ++i)
			{
				FluidSlot slot = (FluidSlot) ((ContainerBase) player.openContainer).fluidSlotList.get(i);
				slot.setStack(stacks[i]);
			}
		}
		return null;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeInt(stacks.length);
		for(int i = 0; i < stacks.length; 
				output.writeFluidStack(stacks[i++]));
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		stacks = new FluidStack[stacks.length];
		for(int i = 0; i < stacks.length; 
				stacks[i++] = input.readFluidStack());
	}
}