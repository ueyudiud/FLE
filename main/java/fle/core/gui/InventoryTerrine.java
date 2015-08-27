package fle.core.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.api.gui.GuiCondition;
import fle.api.gui.GuiError;
import fle.api.gui.InventoryWithFluidTank;
import fle.api.material.Matter.AtomStack;
import fle.api.material.MatterDictionary;
import fle.core.recipe.RecipeHelper;
import fle.core.te.argil.TileEntityTerrine;

public class InventoryTerrine extends InventoryWithFluidTank<TileEntityTerrine>
{
	public GuiCondition type = GuiError.DEFAULT;
	public double recipeTime;
	
	public InventoryTerrine() 
	{
		super(2, 3000);
		maxHeat = 1500;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getDouble("RecipeTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setDouble("RecipeTime", recipeTime);
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
				syncTank(tile);
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
			if(MatterDictionary.getMelting(stacks[0]) != null && stacks[1] == null)
			{
				AtomStack stack = MatterDictionary.getMatter(stacks[0]);
				Integer[] is = MatterDictionary.getMeltRequires(stacks[0]);
				if(tile.getTemperature(ForgeDirection.UNKNOWN) > is[0])
				{
					double progress = (tile.getTemperature(ForgeDirection.UNKNOWN) - is[0]) * 10D;
					tile.onHeatEmit(ForgeDirection.UNKNOWN, progress);
					recipeTime += progress;
					if(recipeTime >= is[1])
					{
						FluidStack resource = MatterDictionary.getFluid(stack);
						resource.amount /= 4;
						if(fill(resource, false) == 0)
						{
							type = GuiError.CAN_NOT_OUTPUT;
						}
						else
						{
							fill(resource, true);
							type = GuiError.DEFAULT;
							recipeTime = 0;
							decrStackSize(0, 1);
						}
						syncTank(tile);
					}
				}
			}
			else
			{
				tile.mode = 0;
				recipeTime = 0;
			}
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

	public double getProgress()
	{
		return recipeTime / MatterDictionary.getMeltRequires(stacks[0])[1];
	}
}