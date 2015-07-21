package fla.core.tileentity.argil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fla.api.recipe.ErrorType;
import fla.api.world.BlockPos;
import fla.core.gui.InventoryTerrine;
import fla.core.tileentity.TileEntityInventory;

public class TileEntityTerrine extends TileEntityInventory<InventoryTerrine> implements IFluidTank, IFluidHandler
{
	public int mode;
	
	public TileEntityTerrine() 
	{
		super(new InventoryTerrine());
		this.mode = 0;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		mode = nbt.getShort("Mode");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("Mode", (short) mode);
	}

	@Override
	public void updateEntity() 
	{
		inv.updateEntity(this);
	}

	@Override
	public boolean canSetDirection(World world, BlockPos pos) 
	{
		return false;
	}

	@Override
	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos,
			double yPos, double zPos, ItemStack itemstack) 
	{
		return false;
	}

	@Override
	public ForgeDirection setDirectionWith(World world, BlockPos pos,
			double xPos, double yPos, double zPos, ItemStack itemstack) 
	{
		return null;
	}

	
	@Override
	public FluidStack getFluid() 
	{
		return inv.getFluid();
	}

	@Override
	public int getFluidAmount() 
	{
		return inv.getFluidAmount();
	}

	@Override
	public int getCapacity() 
	{
		return inv.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return inv.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return inv.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return inv.drain(maxDrain, doDrain);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return from == ForgeDirection.UP ? inv.fill(resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) 
	{
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return new FluidTankInfo[]{getInfo()};
	}	
	
	public ErrorType getError()
	{
		return inv.type;
	}
}