package fle.api.te;

import net.minecraftforge.common.util.ForgeDirection;
import fle.api.inventory.InventorySolidTank;
import fle.api.soild.ISolidHandler;
import fle.api.soild.ISolidTanks;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.soild.SolidTankInfo;

public abstract class TEIST<T extends InventorySolidTank> extends TEInventory<T> implements ISolidHandler, ISolidTanks
{
	protected TEIST(T inv)
	{
		super(inv);
	}

	@Override
	public int fillS(ForgeDirection from, SolidStack resource, boolean doFill)
	{
		return getTileInventory().fillS(from, resource, doFill);
	}

	@Override
	public SolidStack drainS(ForgeDirection from, SolidStack resource,
			boolean doDrain)
	{
		return getTileInventory().drainS(from, resource, doDrain);
	}

	@Override
	public SolidStack drainS(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getTileInventory().drainS(from, maxDrain, doDrain);
	}

	@Override
	public boolean canFillS(ForgeDirection from, Solid Solid)
	{
		return getTileInventory().canFillS(from, Solid);
	}

	@Override
	public boolean canDrainS(ForgeDirection from, Solid Solid)
	{
		return getTileInventory().canDrainS(from, Solid);
	}

	@Override
	public SolidTankInfo[] getSolidTankInfo(ForgeDirection from)
	{
		return getTileInventory().getSolidTankInfo(from);
	}

	@Override
	public int getSizeSolidTank()
	{
		return getTileInventory().getSizeSolidTank();
	}

	@Override
	public SolidTank getSolidTank(int index) 
	{
		return getTileInventory().getSolidTank(index);
	}

	@Override
	public SolidStack getSolidStackInTank(int index)
	{
		return getTileInventory().getSolidStackInTank(index);
	}

	@Override
	public void setSolidStackInTank(int index, SolidStack aStack)
	{
		getTileInventory().setSolidStackInTank(index, aStack);
	}

	@Override
	public SolidStack drainSolidTank(int index, int maxDrain, boolean doDrain)
	{
		return getTileInventory().drainSolidTank(index, maxDrain, doDrain);
	}

	@Override
	public int fillSolidTank(int index, SolidStack resource, boolean doFill)
	{
		return getTileInventory().fillSolidTank(index, resource, doFill);
	}
}