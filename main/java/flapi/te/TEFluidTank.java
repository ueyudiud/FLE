package flapi.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import farcore.block.TEBase;
import flapi.te.interfaces.IFluidTanks;
import flapi.world.BlockPos;

public class TEFluidTank extends TEBase implements IFluidTanks, IFluidHandler
{
	protected FluidTank tank;
	
	public TEFluidTank(int amount)
	{
		tank = new FluidTank(amount);
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
		nbt.setTag("Fluid", tank.writeToNBT(new NBTTagCompound()));
	}

	protected FluidTank getFluidTankFromSide(ForgeDirection aSide)
	{
		return tank;
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
		return getFluidTankFromSide(from) == null ? new FluidTankInfo[0] : new FluidTankInfo[]{getFluidTankFromSide(from).getInfo()};
	}
}