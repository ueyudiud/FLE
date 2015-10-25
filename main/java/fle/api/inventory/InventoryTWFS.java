package fle.api.inventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.soild.ISolidHandler;
import fle.api.soild.ISolidTanks;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.soild.SolidTankInfo;
import fle.api.te.IFluidTanks;
import fle.api.te.ITEInWorld;
import fle.api.te.TEIST;
import fle.api.world.BlockPos;

public abstract class InventoryTWFS<T extends TEIST> extends InventoryTileBase<T> implements 
ISolidTanks, ISolidHandler, 
IFluidHandler, IFluidTanks, IFluidTank
{
	protected FluidTank fTank;
	protected SolidTank sTank;
	
	public InventoryTWFS(int slotSize, int sCapcity, int fCapcity)
	{
		super(slotSize);
		sTank = new SolidTank(sCapcity);
		fTank = new FluidTank(fCapcity);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		sTank.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		sTank.writeToNBT(nbt);
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
		return tTank == null ? null : tTank.has(resource) ? tTank.drain(resource.getSize(), doDrain) : null;
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
	public FluidStack getFluid() 
	{
		return fTank.getFluid();
	}

	@Override
	public int getFluidAmount() 
	{
		return fTank.getFluidAmount();
	}

	@Override
	public int getCapacity() 
	{
		return fTank.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return fTank.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return fTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return fTank.drain(maxDrain, doDrain);
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