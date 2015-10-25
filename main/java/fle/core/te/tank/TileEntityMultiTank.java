package fle.core.te.tank;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import fle.core.block.tank.ItemTankBlock;
import fle.core.inventory.tank.InventoryMultiTank;
import fle.core.util.TankBlockInfo;

public class TileEntityMultiTank extends TileEntityAbstractTank<InventoryMultiTank>
{	
	public TileEntityMultiTank()
	{
		super(new InventoryMultiTank("MultiTankBlock", 2));
	}
	public TileEntityMultiTank(String name, int size)
	{
		super(new InventoryMultiTank(name, size));
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
		setBlock(ItemTankBlock.b(nbt.getString("Material")));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(isMainTile())
		{
			nbt.setInteger("Capcity", tank.getCapacity());
			nbt.setTag("FluidTank", tank.writeToNBT(new NBTTagCompound()));
		}
		if(info != null)
		{
			nbt.setString("Material", ItemTankBlock.e(info));
		}
	}
	
	@Override
	protected void updateInventory()
	{
		if(canBeMainTile())
		{
			super.updateInventory();
		}
		if(isMainTile())
		{
			getTileInventory().updateEntity(this);
		}
	}
	
	protected boolean canBeMainTile()
	{
		return true;
	}
	
	public boolean isMainTile()
	{
		return isMainTile;
	}
	
	public boolean isMultiTank()
	{
		return isMainTile || mainTile != null;
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
	
	protected FluidTank getTank()
	{
		return new FluidTank(0);
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
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return getTank() == null ? new FluidTankInfo[0] : new FluidTankInfo[]{getTank().getInfo()};
	}
	
	private TankBlockInfo info;
	
	public Block getTankMaterial()
	{
		return info != null ? info.getMaterial() : Blocks.stone;
	}
	
	public int getTankMaterialMeta()
	{
		return info != null ? info.getMetadata() : 0;
	}
	
	public void setBlock(TankBlockInfo aInfo)
	{
		info = aInfo;
	}
	
	public int getInfoData()
	{
		return ItemTankBlock.d(info);
	}
}