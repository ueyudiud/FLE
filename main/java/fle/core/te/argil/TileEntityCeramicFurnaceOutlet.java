package fle.core.te.argil;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.energy.IThermalTileEntity;
import fle.api.gui.IInventoryTile;
import fle.api.net.FlePackets.CoderTileUpdate;
import fle.api.net.INetEventListener;
import fle.api.te.TEInventory;
import fle.core.energy.ThermalTileHelper;
import fle.core.gui.InventoryCeramicFurnaceOutlet;
import fle.core.init.Materials;

public class TileEntityCeramicFurnaceOutlet extends TEInventory<InventoryCeramicFurnaceOutlet> implements IFluidHandler, IThermalTileEntity, INetEventListener
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	
	public TileEntityCeramicFurnaceOutlet()
	{
		super(new InventoryCeramicFurnaceOutlet());
	}
	
	public TileEntityCeramicFurnaceCrucible getCrucibleTile()
	{
		if(getBlockPos().toPos(getDirction(getBlockPos())).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(getDirction(getBlockPos())).getBlockTile());
		}
		return null;
	}

	@Override
	public void updateEntity()
	{
		inv.updateEntity(this);
		if(getBlockPos().toPos(dir.getOpposite()).getBlockTile() instanceof IFluidHandler)
		{
			FluidStack stack = drain(dir.getOpposite(), 5, false);
			if(stack != null)
			{
				int drain = ((IFluidHandler) getBlockPos().toPos(dir.getOpposite()).getBlockTile()).fill(dir, stack, true);
				drain(dir.getOpposite(), drain, true);
			}
		}
		else if(getBlockPos().toPos(dir.getOpposite()).toPos(ForgeDirection.DOWN).getBlockTile() instanceof IFluidHandler)
		{
			FluidStack stack = drain(dir.getOpposite(), 5, false);
			if(stack != null)
			{
				int drain = ((IFluidHandler) getBlockPos().toPos(dir.getOpposite()).toPos(ForgeDirection.DOWN).getBlockTile()).fill(ForgeDirection.UP, stack, true);
				drain(dir.getOpposite(), drain, true);
			}
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
		sendToNearBy(new CoderTileUpdate(this, (byte) 1, FleAPI.getIndexFromDirection(dir)), 16.0F);
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
		if(getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(dir).getBlockTile()).drain(from, resource, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if(getBlockPos().toPos(dir).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(dir).getBlockTile()).drain(from, maxDrain, doDrain);
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

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 1)
		{
			setDirction(ForgeDirection.VALID_DIRECTIONS[(Integer) contain]);
		}
	}
}