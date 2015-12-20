package fle.core.te.argil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import flapi.energy.IThermalTileEntity;
import flapi.material.MatterDictionary;
import flapi.material.MatterDictionary.IFreezingRecipe;
import flapi.te.TEInventory;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.energy.TransportHelper;
import fle.core.init.Materials;
import fle.core.recipe.RecipeHelper;

public class TileEntityCeramicFurnaceOutlet extends TEInventory implements IFluidHandler, IThermalTileEntity
{
	private int buf;
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	
	public TileEntityCeramicFurnaceOutlet()
	{
		super(2);
	}
	
	public TileEntityCeramicFurnaceCrucible getCrucibleTile()
	{
		if(getBlockPos().toPos(getDirction().getOpposite()).getBlockTile() instanceof TileEntityCeramicFurnaceCrucible)
		{
			return ((TileEntityCeramicFurnaceCrucible) getBlockPos().toPos(getDirction().getOpposite()).getBlockTile());
		}
		return null;
	}

	@Override
	public void update()
	{
		TileEntityCeramicFurnaceCrucible tile1 = getCrucibleTile();
		if(tile1 != null)
		{
			IFluidTank tank = tile1.getTank(0);
			IFreezingRecipe recipe = MatterDictionary.getFreeze(tank.getFluid(), this);
			if(recipe != null && !isClient())
			{
				int require = recipe.getMatterRequire(tank.getFluid(), this);
				if(tank.getFluidAmount() >= require)
				{
					++buf;
					if(buf > (tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FleValue.WATER_FREEZE_POINT))
					{
						ItemStack output = recipe.getOutput(tank.getFluid(), this).copy();
						if(RecipeHelper.matchOutput(this, 1, output))
						{
							buf = 0;
							tank.drain(require, true);
							RecipeHelper.onOutputItemStack(this, 1, output);
						}
						else
						{
							
						}
					}
					else
					{
						onHeatReceive(getDirction(), (tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos())) * 0.2D);
					}
				}
			}
			else if(recipe == null && !isClient())
			{
				buf = 0;
			}
		}
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

	@Override
	protected String getDefaultName()
	{
		return null;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}
}