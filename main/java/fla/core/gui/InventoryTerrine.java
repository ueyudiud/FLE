package fla.core.gui;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import fla.api.recipe.ErrorType;
import fla.core.gui.base.InventoryWithFluidTank;
import fla.core.gui.base.RecipeHelper;
import fla.core.tileentity.argil.TileEntityTerrine;

public class InventoryTerrine extends InventoryWithFluidTank<TileEntityTerrine>
{
	public ErrorType type = ErrorType.DEFAULT;
	
	public InventoryTerrine() 
	{
		super(2, 3000);
		maxHeat = 500;
	}
	
	@Override
	public void updateEntity(TileEntityTerrine tile) 
	{
		if(rand.nextFloat() < 0.1F)
		{
			tank.drain(1, true);
		}
		if(RecipeHelper.fillOrDrainInventoryTank(this, this, 0, 1))
		{
			type = ErrorType.DEFAULT;
		}
		else
		{
			type = ErrorType.CAN_NOT_OUTPUT;
		}
		super.updateEntity(tile);
	}

	@Override
	public String getInventoryName() 
	{
		return "inventory.terrine";
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}

	@Override
	protected void onMelted(TileEntityTerrine tile) 
	{
		if(getFluid().getFluid().canBePlacedInWorld())
		{
			tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, getFluid().getFluid().getBlock(), 0, 2);
			tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		}
		else
		{
			tile.getWorldObj().setBlockToAir(tile.xCoord, tile.yCoord, tile.zCoord);
			tile.getWorldObj().removeTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		}
	}
}