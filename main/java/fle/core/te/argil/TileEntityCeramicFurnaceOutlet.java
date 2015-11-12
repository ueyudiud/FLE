package fle.core.te.argil;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.te.TEInventory;
import fle.core.energy.ThermalTileHelper;
import fle.core.energy.TransportHelper;
import fle.core.init.Materials;
import fle.core.inventory.InventoryCeramicFurnaceOutlet;

public class TileEntityCeramicFurnaceOutlet extends TEInventory<InventoryCeramicFurnaceOutlet> implements IFluidHandler, IThermalTileEntity
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	
	public TileEntityCeramicFurnaceOutlet()
	{
		super(new InventoryCeramicFurnaceOutlet());
	}
	
	public TileEntityCeramicFurnaceCrucible getCrucibleTile()
	{
		if(getBlockPos().toPos(getDirction(getBlockPos()).getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(getDirction(getBlockPos()).getOpposite()).getBlockTile());
		}
		return null;
	}

	@Override
	public void updateInventory()
	{
		getTileInventory().updateEntity(this);
		int amount = TransportHelper.matchOutputFluid(drain(dir, 5, false), getBlockPos().toPos(dir), dir.getOpposite());
		if(amount != 0)
		{
			TransportHelper.matchAndTransferOutputFluid(drain(dir, amount, true), getBlockPos().toPos(dir), dir.getOpposite());
		}
		else if((amount = TransportHelper.matchOutputFluid(drain(dir, 5, false), getBlockPos().toPos(dir).toPos(ForgeDirection.DOWN), ForgeDirection.UP)) != 0)
		{
			TransportHelper.matchAndTransferOutputFluid(drain(dir, amount, true), getBlockPos().toPos(dir).toPos(ForgeDirection.DOWN), ForgeDirection.UP);
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		if(getBlockPos().toPos(dir.getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(dir.getOpposite()).getBlockTile()).drain(from, resource, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if(getBlockPos().toPos(dir.getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(dir.getOpposite()).getBlockTile()).drain(from, maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return dir == from && 
				getBlockPos().toPos(dir.getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return getBlockPos().toPos(dir.getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible ? ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(dir.getOpposite()).getBlockTile()).getTankInfo(from) : new FluidTankInfo[0];
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return tc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		tc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		tc.emitHeat(heatValue);
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}
}