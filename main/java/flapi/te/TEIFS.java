package flapi.te;

import flapi.solid.ISolidHandler;
import flapi.solid.ISolidTanks;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import flapi.solid.SolidTank;
import flapi.solid.SolidTankInfo;
import flapi.te.interfaces.IFluidTanks;
import flapi.world.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public abstract class TEIFS extends TEInventory implements
ISolidTanks, ISolidHandler, 
IFluidHandler, IFluidTanks
{
	protected FluidTank fTank;
	protected SolidTank sTank;
	
	public TEIFS(InventoryTile inv, int sCapcity, int fCapcity)
	{
		super(inv);
		sTank = new SolidTank(sCapcity);
		fTank = new FluidTank(fCapcity);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		sTank.readFromNBT(nbt.getCompoundTag("Solid"));
		fTank.readFromNBT(nbt.getCompoundTag("Fluid"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setTag("Solid", sTank.writeToNBT(new NBTTagCompound()));
		nbt.setTag("Fluid", fTank.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public int getSizeSolidTank()
	{
		return 1;
	}

	@Override
	public SolidTank getSolidTank(int index)
	{
		return sTank;
	}

	@Override
	public SolidStack getSolidStackInTank(int index)
	{
		return sTank.getStack();
	}

	@Override
	public void setSolidStackInTank(int index, SolidStack aStack)
	{
		sTank.setStack(aStack);
	}

	@Override
	public SolidStack drainSolidTank(int index, int maxDrain, boolean doDrain)
	{
		return getSolidTank(index).drain(maxDrain, doDrain);
	}

	@Override
	public int fillSolidTank(int index, SolidStack resource, boolean doFill)
	{
		return getSolidTank(index).fill(resource, doFill);
	}
	
	protected SolidTank getTankFromSide(ForgeDirection aSide)
	{
		return sTank;
	}

	@Override
	public int fillS(ForgeDirection from, SolidStack resource, boolean doFill)
	{
		return getTankFromSide(from) != null ? getTankFromSide(from).fill(resource, doFill) : 0;
	}

	@Override
	public SolidStack drainS(ForgeDirection from, SolidStack resource,
			boolean doDrain)
	{
		SolidTank tTank = getTankFromSide(from);
		return tTank == null ? null : tTank.has(resource) ? tTank.drain(resource.size(), doDrain) : null;
	}

	@Override
	public SolidStack drainS(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getTankFromSide(from) != null ? getTankFromSide(from).drain(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFillS(ForgeDirection from, Solid Solid)
	{
		return getTankFromSide(from) != null;
	}

	@Override
	public boolean canDrainS(ForgeDirection from, Solid Solid)
	{
		return getTankFromSide(from) != null;
	}

	@Override
	public SolidTankInfo[] getSolidTankInfo(ForgeDirection from)
	{
		return new SolidTankInfo[]{sTank.getInfo()};
	}

	protected FluidTank getFluidTankFromSide(ForgeDirection aSide)
	{
		return fTank;
	}
	
	@Override
	public int getSizeTank()
	{
		return 1;
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return fTank;
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

	/**
	 * 
	 * @param dir
	 * @param maxDrain
	 * @param doDrain
	 * @return
	 */
	public boolean tryDrainFluid(ForgeDirection dir, int maxDrain, boolean doDrain)
	{
		return tryDrainFluid(dir, maxDrain, doDrain, false);
	}
	/**
	 * Drain fluid to a direction.
	 * @param dir
	 * @param maxDrain
	 * @param doDrain
	 * @param drainToVoid fluid drain without check weather has container to fill.
	 * @return
	 */
	public boolean tryDrainFluid(ForgeDirection dir, int maxDrain, boolean doDrain, boolean drainToVoid)
	{
		FluidStack aStack = drain(dir, maxDrain, false);
		if(aStack != null)
		{
			BlockPos pos = getBlockPos().toPos(dir);
			if(pos.getBlockTile() instanceof IFluidHandler)
			{
				if(((IFluidHandler) pos.getBlock()).canFill(dir.getOpposite(), aStack.getFluid()))
				{
					int drain = ((IFluidHandler) pos.getBlock()).fill(dir.getOpposite(), aStack, doDrain);
					if(doDrain && drain > 0)
					{
						drain(dir, drain, true);
					}
					return drain > 0;
				}
			}
			else if(drainToVoid)
			{
				if(doDrain)
				{
					if(pos.getBlock().isReplaceable(worldObj, pos.x, pos.y, pos.z))
					{
						if(aStack.getFluid().canBePlacedInWorld() && aStack.amount >= 1000)
						{
							worldObj.setBlock(pos.x, pos.y, pos.z, aStack.getFluid().getBlock());
						}
					}
					drain(dir, aStack.amount, doDrain);
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