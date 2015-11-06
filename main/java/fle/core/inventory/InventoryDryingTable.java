package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.gui.GuiError;
import fle.api.inventory.InventoryTileCraftable;
import fle.api.recipe.IRecipeHandler.RecipeKey;
import fle.core.init.Lang;
import fle.core.recipe.FLEDryingRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipeKey;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityDryingTable;

public class InventoryDryingTable extends InventoryTileCraftable<TileEntityDryingTable>
{
	public RecipeKey recipeName;
	public int recipeTime;
	private double tem;
	private double water;
	private int levelCheckBuffer;
	
	public InventoryDryingTable() 
	{
		super(Lang.inventory_dryingTable, 2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getInteger("RecipeTime");
		recipeName = nbt.hasKey("RecipeName") ? FLEDryingRecipe.getInstance().getRecipeKey(nbt.getString("RecipeName")) : null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setInteger("RecipeTime", recipeTime);
		if(recipeName != null)
			nbt.setString("RecipeName", recipeName.toString());
	}

	@Override
	public void updateEntity(TileEntityDryingTable tile) 
	{
		tem = tile.tem;
		water = tile.water;
		if(recipeName == null)
		{
			recipeTime = 0;
			DryingRecipe str = FLEDryingRecipe.getInstance().getRecipe(new DryingRecipeKey(stacks[0]));
			if(str != null)
			{
				RecipeHelper.onInputItemStack(this, 0);
				recipeName = str.getRecipeKey();
			}
		}
		if(recipeName != null && tile.getWorldObj().isRaining())
		{
			tile.type = GuiError.RAINING;
		}
		else if(recipeName != null)
		{
			int tTime = ((DryingRecipeKey) recipeName).getDryingTick();
			if(tTime == -1)
			{
				recipeName = null;
				return;
			}
			if(tile.type == GuiError.RAINING) tile.type = GuiError.DEFAULT;
			double d1 = tem != 0D ? tem : tile.getTempretureLevel();
			double d2 = water != 0D ? water : tile.getWaterLevel();
			double speed = MathHelper.randomFloatClamp(rand, (float) (d1 / d2) * 0.8F, (float) (d1 / d2) * 1.2F) * 100D;
			recipeTime += speed;
			if(recipeTime >= tTime)
			{
				ItemStack output = FLEDryingRecipe.getInstance().getRecipe(recipeName).output;
				if(RecipeHelper.matchOutput(this, 1, output))
				{
					RecipeHelper.onOutputItemStack(this, 1, output);
					recipeName = null;
					recipeTime = 0;
					tile.type = GuiError.DEFAULT;
				}
				else
				{
					recipeTime = ((DryingRecipeKey) recipeName).getDryingTick();
					tile.type = GuiError.CAN_NOT_OUTPUT;
				}
			}
		}
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
		return recipeName == null ? 0 : i * recipeTime / ((DryingRecipeKey) recipeName).getDryingTick();
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return isInputSlot(aSlotID);
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return isOutputSlot(aSlotID);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return new int[]{0};
	}
}