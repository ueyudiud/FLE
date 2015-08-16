package fle.core.te.argil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.energy.IThermalTileEntity;
import fle.api.gui.GuiCondition;
import fle.api.world.BlockPos;
import fle.core.gui.InventoryTerrine;
import fle.core.init.Materials;
import fle.core.te.base.TEInventory;

public class TileEntityTerrine extends TEInventory<InventoryTerrine> implements IFluidTank, IFluidHandler, IThermalTileEntity
{
	private final double sh = Materials.Argil.getPropertyInfo().getSpecificHeat();
	private final double hc = Materials.Argil.getPropertyInfo().getThermalConductivity();
	
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
		if(resource != null)
			if(!resource.isFluidEqual(inv.getFluid())) return null;
		return from == ForgeDirection.UP ? inv.drain(resource.amount, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return from == ForgeDirection.UP ? inv.drain(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		return from == ForgeDirection.UP;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return new FluidTankInfo[]{getInfo()};
	}	
	
	public GuiCondition getError()
	{
		return inv.type;
	}

	public void setClose() 
	{
		mode = 1;
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FleValue.WATER_FREEZE_POINT;
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return hc;
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return 0;
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		
	}

	@Override
	public void onHeatEmmit(ForgeDirection dir, double heatValue)
	{
		
	}
}