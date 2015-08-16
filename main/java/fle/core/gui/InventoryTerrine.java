package fle.core.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.gui.InventoryWithFluidTank;
import fle.core.recipe.RecipeHelper;
import fle.core.te.argil.TileEntityTerrine;

public class InventoryTerrine extends InventoryWithFluidTank<TileEntityTerrine>
{
	public GuiCondition type = GuiError.DEFAULT;
	public String rName;
	public int recipeTime;
	
	public InventoryTerrine() 
	{
		super(2, 3000);
		maxHeat = 1500;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
	}
	
	@Override
	public void updateEntity(TileEntityTerrine tile) 
	{
		switch(tile.mode)
		{
		case 0 :
		{
			if(rand.nextFloat() < 0.03125F)
			{
				tank.drain(1, true);
			}
			if(RecipeHelper.fillOrDrainInventoryTank(this, this, 0, 1))
			{
				type = GuiError.DEFAULT;
			}
			else
			{
				type = GuiError.CAN_NOT_OUTPUT;
			}
		}
		break;
		case 1 :
		{
			tile.mode = 0;
		}
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

	public void getHeat(TileEntityTerrine tile, int pkg) 
	{
		;
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
}