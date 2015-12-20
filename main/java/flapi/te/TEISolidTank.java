package flapi.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.solid.ISolidHandler;
import flapi.solid.ISolidTanks;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import flapi.solid.SolidTank;
import flapi.solid.SolidTankInfo;

public abstract class TEISolidTank extends TEInventory implements ISolidTanks, ISolidHandler
{
	protected SolidTank sTank;

	public TEISolidTank(int i, int size)
	{
		super(i);
		sTank = new SolidTank(size);
	}
	
	@Override
	protected void readFromNBT1(NBTTagCompound nbt)
	{
		super.readFromNBT1(nbt);
		sTank.readFromNBT(nbt.getCompoundTag("Solid"));
	}
	
	@Override
	public NBTTagCompound writeToNBT1(NBTTagCompound nbt)
	{
		super.writeToNBT1(nbt);
		nbt.setTag("Solid", sTank.writeToNBT(new NBTTagCompound()));
		return nbt;
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
}