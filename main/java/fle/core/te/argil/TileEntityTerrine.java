package fle.core.te.argil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.gui.GuiCondition;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.api.net.INetEventListener;
import fle.api.te.TEIT;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;
import fle.core.inventory.InventoryTerrine;

public class TileEntityTerrine extends TEIT<InventoryTerrine> implements IFluidTank, IFluidHandler, IThermalTileEntity, INetEventListener
{
	private ThermalTileHelper heatCurrect = new ThermalTileHelper(Materials.Argil);
	public int mode;
	
	public TileEntityTerrine() 
	{
		super(new InventoryTerrine());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		mode = nbt.getShort("Mode");
		heatCurrect.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("Mode", (short) mode);
		heatCurrect.writeToNBT(nbt);
	}

	@Override
	public void updateInventory() 
	{
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		getTileInventory().updateEntity(this);
		heatCurrect.update();
		if(!worldObj.isRemote)
			sendToNearBy(new CoderTileUpdate(this, (byte) 2, mode), 16.0F);
	}
	
	@Override
	public FluidStack getFluid() 
	{
		return getTileInventory().getFluid();
	}

	@Override
	public int getFluidAmount() 
	{
		return getTileInventory().getFluidAmount();
	}

	@Override
	public int getCapacity() 
	{
		return getTileInventory().getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return getTileInventory().getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return getTileInventory().fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return getTileInventory().drain(maxDrain, doDrain);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return from == ForgeDirection.UP ? getTileInventory().fill(resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) 
	{
		if(resource != null)
			if(!resource.isFluidEqual(getTileInventory().getFluid())) return null;
		return from == ForgeDirection.UP ? getTileInventory().drain(resource.amount, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return from == ForgeDirection.UP ? getTileInventory().drain(maxDrain, doDrain) : null;
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
		return getTileInventory().type;
	}

	public void setClose() 
	{
		mode = 1;
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return heatCurrect.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return heatCurrect.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir) 
	{
		return heatCurrect.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		heatCurrect.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		heatCurrect.emitHeat(heatValue);
	}
	
	public double getProgress()
	{
		return getTileInventory().getProgress();
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 1)
		{
			getTileInventory().recipeTime = (Double) contain;
		}
		else if(type == 2)
		{
			mode = (Integer) contain;
		}
	}
	@Override
	public double getPreHeatEmit()
	{
		return heatCurrect.getPreHeatEmit();
	}
}