package fla.core.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import fla.api.recipe.ErrorType;
import fla.core.gui.base.InventoryTileCraftable;
import fla.core.gui.base.RecipeHelper;
import fla.core.recipe.machine.DryingRecipe;
import fla.core.tileentity.TileEntityDryingTable;
import fla.core.world.FWM;

public class InventoryDryingTable extends InventoryTileCraftable<TileEntityDryingTable>
{
	public String recipeName;
	public int recipeTime;
	private double tem;
	private double water;
	private int levelCheckBuffer;
	
	public InventoryDryingTable() 
	{
		super("inventory.dryingTable", 2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getShort("RecipeTime");
		recipeName = nbt.hasKey("RecipeName") ? nbt.getString("RecipeName") : null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("RecipeTime", (short) recipeTime);
		if(recipeName != null)
			nbt.setString("RecipeName", recipeName);
	}

	@Override
	public void updateEntity(TileEntityDryingTable tile) 
	{
		++levelCheckBuffer;
		if(levelCheckBuffer >= 200)
		{
			levelCheckBuffer = 0;
			tem = getTempreture(tile);
			water = getWater(tile);
		}
		if(recipeName == null)
		{
			recipeTime = 0;
			String str = DryingRecipe.canDrying(stacks[0]);
			if(str != null)
			{
				RecipeHelper.onInputItemStack(this, 0);
				recipeName = str;
			}
		}
		if(recipeName != null)
		{
			double d1 = tem != 0D ? tem : getTempreture(tile);
			double d2 = water != 0D ? water : getWater(tile);
			double speed = MathHelper.randomFloatClamp(rand, (float) (d1 / d2) * 0.8F, (float) (d1 / d2) * 1.2F) * 100D;
			recipeTime += speed;
			if(recipeTime >= DryingRecipe.getRecipeTime(recipeName))
			{
				if(RecipeHelper.matchOutput(this, 1, DryingRecipe.getRecipeResult(recipeName)))
				{
					RecipeHelper.onOutputItemStack(this, 1, DryingRecipe.getRecipeResult(recipeName));
					recipeName = null;
					recipeTime = 0;
					tile.type = ErrorType.DEFAULT;
				}
				else
				{
					recipeTime = DryingRecipe.getRecipeTime(recipeName);
					tile.type = ErrorType.CAN_NOT_OUTPUT;
				}
			}
		}
	}

	public double getTempreture(TileEntity tile)
	{
		return FWM.getTempretureLevel(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public double getWater(TileEntity tile)
	{
		return FWM.getWaterLevel(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack) 
	{
		return i == 1;
	}

	@Override
	protected boolean isInputSlot(int i) 
	{
		return i == 0;
	}

	@Override
	protected boolean isOutputSlot(int i) 
	{
		return i == 1;
	}
	
	public int getProgressBar(int i)
	{
		return i * recipeTime / DryingRecipe.getRecipeTime(recipeName);
	}
}