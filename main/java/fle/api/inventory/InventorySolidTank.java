package fle.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.soild.ISolidHandler;
import fle.api.soild.ISolidTanks;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.soild.SolidTankInfo;
import fle.api.te.TEIST;

public abstract class InventorySolidTank<T extends TEIST> extends InventoryTileBase<T> implements ISolidHandler, ISolidTanks
{
	protected SolidTank sTank;

	public InventorySolidTank(int i, int size)
	{
		super(i);
		sTank = new SolidTank(size);
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
}