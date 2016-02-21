package fle.core.net;

import java.io.IOException;

import net.minecraftforge.fluids.FluidStack;
import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.te.interfaces.IFluidTanks;
import flapi.te.interfaces.IObjectInWorld;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public class FleFluidTankPacket extends FleCoordinatesPacket
{
	private FluidStack[] stacks;
	
	public FleFluidTankPacket()
	{
		super(true);
	}

	public FleFluidTankPacket(BlockPos pos)
	{
		super(true, pos);
		
	}

	public FleFluidTankPacket(IObjectInWorld tile)
	{
		super(true, tile.getBlockPos());
		
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof IFluidTanks)
		{
			os.writeBoolean(true);
			IFluidTanks inv = (IFluidTanks) pos.getBlockTile();
			os.writeInt(inv.getSizeTank());
			for(int i = 0; i < inv.getSizeTank(); ++i)
			{
				os.writeFluidStack(inv.getFluidStackInTank(i));
			}
		}
		else
		{
			os.writeBoolean(false);				
		}
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException 
	{
		super.read(is);
		if(is.readBoolean())
		{
			int size = is.readInt();
			stacks = new FluidStack[size];
			for(int i = 0; i < size; ++i)
			{
				stacks[i] = is.readFluidStack();
			}			
		}	
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof IFluidTanks)
		{
			IFluidTanks inv = (IFluidTanks) pos.getBlockTile();
			for(int i = 0; i < stacks.length; ++i)
			{
				inv.setFluidStackInTank(i, stacks[i]);
			}
		}
		return null;
	}	
}