package fle.api.inventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.te.IFluidTanks;
import fle.api.te.ITEInWorld;
import fle.api.te.TEIT;
import fle.api.world.BlockPos;

public abstract class InventoryWithFluidTank<T extends TEIT> extends InventoryTileAbstract<T> implements IFluidTank, IFluidTanks, IFluidHandler
{
	protected FluidTank tank;
	protected int maxHeat;

	public InventoryWithFluidTank(String name, int size, int capacity) 
	{
		super(name, size);
		tank = new FluidTank(capacity);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("Fluid"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		tank.writeToNBT(nbt1);
		nbt.setTag("Fluid", nbt1);
	}
	
	public void syncTank(T tile)
	{
		tile.syncFluidTank();
	}

	@Override
	public void updateEntity(T tile) 
	{
		updateFluidTank(tile);
		if(tank.getFluid() != null)
		{
			if(tank.getFluid().getFluid().getTemperature(tank.getFluid()) > maxHeat)
			{
				onMelted(tile);
			}
		}
	}
	
	protected abstract void onMelted(T tile);

	protected FluidTank getFluidTankFromSide(ForgeDirection aSide)
	{
		return tank;
	}
	
	@Override
	public FluidStack getFluid() 
	{
		return tank.getFluid();
	}

	@Override
	public int getFluidAmount() 
	{
		return tank.getFluidAmount();
	}

	@Override
	public int getCapacity() 
	{
		return tank.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return tank.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public int getSizeTank()
	{
		return 1;
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return tank;
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return getTank(index).getFluid();
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		IFluidTank tank = getTank(index);
		tank.drain(tank.getCapacity(), true);
		tank.fill(aStack, true);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return getTank(index).drain(maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return getTank(index).fill(resource, doFill);
	}

	public boolean tryDrainFluid(ITEInWorld oiw, ForgeDirection dir, int maxDrain, boolean doDrain)
	{
		return tryDrainFluid(oiw, dir, maxDrain, doDrain, false);
	}
	public boolean tryDrainFluid(ITEInWorld oiw, ForgeDirection dir, int maxDrain, boolean doDrain, boolean drainToVoid)
	{
		FluidStack aStack = drain(maxDrain, false);
		if(aStack != null)
		{
			BlockPos pos = oiw.getBlockPos().toPos(dir);
			if(pos.getBlockTile() instanceof IFluidHandler)
			{
				if(((IFluidHandler) pos.getBlock()).canFill(dir.getOpposite(), aStack.getFluid()))
				{
					int drain = ((IFluidHandler) pos.getBlock()).fill(dir.getOpposite(), aStack, doDrain);
					if(doDrain && drain > 0)
					{
						drain(drain, true);
					}
					return drain > 0;
				}
			}
			else if(drainToVoid)
			{
				if(doDrain)
				{
					if(pos.getBlock().isReplaceable(oiw.getTileEntity().getWorldObj(), pos.x, pos.y, pos.z))
					{
						if(aStack.getFluid().canBePlacedInWorld() && aStack.amount >= 1000)
						{
							oiw.getTileEntity().getWorldObj().setBlock(pos.x, pos.y, pos.z, aStack.getFluid().getBlock());
						}
					}
					drain(aStack.amount, doDrain);
				}
				return true;
			}
			else return false;
		}
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return getFluidTankFromSide(from) == null ? 0 : getFluidTankFromSide(from).fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		IFluidTank tank = getFluidTankFromSide(from);
		return tank == null ? null : tank.getInfo().fluid != null && tank.getInfo().fluid.containsFluid(resource) ? tank.drain(resource.amount, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getFluidTankFromSide(from) == null ? null : getFluidTankFromSide(from).drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return getFluidTankFromSide(from) != null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return getFluidTankFromSide(from) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{getFluidTankFromSide(from).getInfo()};
	}
}