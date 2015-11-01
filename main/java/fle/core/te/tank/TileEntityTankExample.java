package fle.core.te.tank;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.te.TileEntityAbstractTank;
import fle.core.inventory.tank.InventoryTankExample;
import fle.core.recipe.RecipeHelper;

public class TileEntityTankExample extends TileEntityAbstractTank<InventoryTankExample>
{	
	public TileEntityTankExample()
	{
		super(new InventoryTankExample(2));
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		int cap = nbt.getInteger("Capcity");
		if(cap > 0)
		{
			tank = new FluidTank(cap);
			tank.readFromNBT(nbt.getCompoundTag("FluidTank"));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(isMainTile)
		{
			nbt.setInteger("Capcity", tank.getCapacity());
			nbt.setTag("FluidTank", tank.writeToNBT(new NBTTagCompound()));
		}
	}
	
	@Override
	protected void updateInventory()
	{
		super.updateInventory();
		if(isMainTile)
		{
			getTileInventory().updateEntity(this);
		}
		if(isMainTile || mainTile != null)
		{
			RecipeHelper.fillOrDrainInventoryTank(this, getTank(), 0, 1);
		}
	}

	@Override
	protected boolean canConnectWith(TileEntity tile)
	{
		return getClass().isInstance(tile);
	}

	protected void initMainTank(int capcity)
	{
		tank = new FluidTank(capcity);
	}
	
	@Override
	protected void removeMainTank()
	{
		tank = null;
	}

	@Override
	public int getSizeTank()
	{
		return isMainTile || mainTile != null ? 1 : 0;
	}
	
	private FluidTank getTank()
	{
		return isMainTile ? tank : mainTile != null ? mainTile.tank : new FluidTank(0);
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return getTank();
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return getTank().getFluid();
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		getTank().setFluid(aStack);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return getTank().drain(maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return getTank().fill(resource, doFill);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return getTank().fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		return getTank().getFluidAmount() > 0 && resource.isFluidEqual(getTank().getFluid()) ? getTank().drain(resource.amount, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getTank().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.DOWN && getTank().getCapacity() > 0;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.UP;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return getTank() == null ? new FluidTankInfo[0] : new FluidTankInfo[]{getTank().getInfo()};
	}

}