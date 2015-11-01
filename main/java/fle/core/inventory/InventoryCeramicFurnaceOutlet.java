package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidTank;
import fle.FLE;
import fle.api.FleValue;
import fle.api.inventory.InventoryTileBase;
import fle.api.material.MatterDictionary;
import fle.api.material.MatterDictionary.IFreezingRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.te.argil.TileEntityCeramicFurnaceCrucible;
import fle.core.te.argil.TileEntityCeramicFurnaceOutlet;

public class InventoryCeramicFurnaceOutlet extends InventoryTileBase<TileEntityCeramicFurnaceOutlet>
{
	private int buf;

	public InventoryCeramicFurnaceOutlet()
	{
		super(2);
	}

	@Override
	public void updateEntity(TileEntityCeramicFurnaceOutlet tile)
	{
		TileEntityCeramicFurnaceCrucible tile1 = tile.getCrucibleTile();
		if(tile1 != null)
		{
			IFluidTank tank = tile1.getTank(0);
			IFreezingRecipe recipe = MatterDictionary.getFreeze(tank.getFluid(), this);
			if(recipe != null && !tile.getWorldObj().isRemote)
			{
				int require = recipe.getMatterRequire(tank.getFluid(), this);
				if(tank.getFluidAmount() >= require)
				{
					++buf;
					if(buf > (tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FleValue.WATER_FREEZE_POINT))
					{
						ItemStack output = recipe.getOutput(tank.getFluid(), tile).copy();
						if(RecipeHelper.matchOutput(tile, 1, output))
						{
							buf = 0;
							tank.drain(require, true);
							RecipeHelper.onOutputItemStack(tile, 1, output);
						}
						else
						{
							
						}
					}
					else
					{
						tile.onHeatReceive(tile.getDirction(tile.getBlockPos()), (tank.getFluid().getFluid().getTemperature(tank.getFluid()) - FLE.fle.getThermalNet().getEnvironmentTemperature(tile.getBlockPos())) * 0.2D);
					}
				}
			}
			else if(recipe == null && !tile.getWorldObj().isRemote)
			{
				buf = 0;
			}
		}
	}

	@Override
	public String getInventoryName()
	{
		return "inventory.ceramic.furnace.outlet";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
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
}