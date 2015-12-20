package flapi.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import flapi.FleAPI;
import flapi.gui.ContainerBase;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;

public class FluidWindowPacket extends FleAbstractPacket
{
	private int id;
	private FluidStack[] infos;
	
	public FluidWindowPacket()
	{
		
	}
	public FluidWindowPacket(ContainerBase container)
	{
		id = container.windowId;
		infos = new FluidStack[container.fluidSlotList.size()];
		for(int i = 0; i < infos.length; ++i)
			infos[i] = container.fluidSlotList.get(i).getStack();
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		id = is.readInt();
		int size = is.readInt();
		infos = new FluidStack[size];
		for(int i = 0; i < infos.length; ++i)
		{
			infos[i] = is.readFluidStack();
		}
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(id);
		os.writeInt(infos.length);
		for(int i = 0; i < infos.length; ++i)
		{
			os.writeFluidStack(infos[i]);
		}
	}

	@Override
	public Object process(FleNetworkHandler handler)
	{
		EntityPlayer player = FleAPI.mod.getPlatform().getPlayerInstance();
		if(player.openContainer.windowId == id)
		{
			ContainerBase container = (ContainerBase) player.openContainer;
			for(int i = 0; i < infos.length; ++i)
			{
				container.fluidSlotList.get(i).setStack(infos[i]);
			}
		}
		return null;
	}
}
