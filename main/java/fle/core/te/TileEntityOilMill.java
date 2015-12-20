package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.recipe.IRecipeHandler.RecipeKey;
import flapi.te.TEIFluidTank;
import fle.core.init.Lang;
import fle.core.recipe.FLEOilMillRecipe;
import fle.core.recipe.FLEOilMillRecipe.OilMillKey;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;
import fle.core.recipe.RecipeHelper;

public class TileEntityOilMill extends TEIFluidTank
{
	private static final int[] a = {0};
	
	private int cache;
	private int recipeTime;
	private RecipeKey key;
	public GuiCondition type = GuiError.DEFAULT;
	
	public TileEntityOilMill()
	{
		super(2, 2000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getShort("RecipeTime");
		key = FLEOilMillRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("RecipeTime", (short) recipeTime);
		if(key != null)
			nbt.setString("Recipe", key.toString());
	}

	@Override
	protected void update()
	{
		if(cache > 0)
		{
			--cache;
			if(key == null && !isClient())
			{
				OilMillRecipe recipe = FLEOilMillRecipe.getInstance().getRecipe(new OilMillKey(stacks[0]));
				if(recipe != null)
				{
					RecipeHelper.onInputItemStack(this, 0);
					key = recipe.getRecipeKey();
					type = GuiError.DEFAULT;
				}
				else
				{
					type = GuiError.CAN_NOT_INPUT;
				}
			}
			if(key != null)
			{
				++recipeTime;
				if(recipeTime > 200 && !isClient())
				{
					OilMillRecipe recipe = FLEOilMillRecipe.getInstance().getRecipe(key);
					if(recipe != null)
					{
						if(RecipeHelper.matchOutput(this, 1, recipe.output1) && RecipeHelper.matchOutFluidStack(tank, recipe.output2))
						{
							RecipeHelper.onOutputItemStack(this, 1, recipe.getRandOutput());
							RecipeHelper.onOutputFluidStack(tank, recipe.output2);
							type = GuiError.DEFAULT;
							recipeTime = 0;
							key = null;
						}
						else
						{
							type = GuiError.CAN_NOT_OUTPUT;
							recipeTime = 200;
						}
					}
					else
					{
						key = null;
						recipeTime = 0;
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int i)
	{
		return (int) ((float) (recipeTime * i) / 200F);
	}

	@SideOnly(Side.CLIENT)
	public int getCache(int i)
	{
		return (int) ((float) (cache * i) / 40F);
	}

	public void onWork()
	{
		if(cache < 40)
			cache += 40;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? a : null;
	}
	
	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID == 0;
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aSlotID == 1;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return from != ForgeDirection.UP && from != ForgeDirection.DOWN;
	}
	
	@Override
	public int getProgressSize()
	{
		return 1;
	}
	
	@Override
	public int getProgress(int id) 
	{
		return id == 0 ? recipeTime : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) recipeTime = value;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_leverOilMill;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i == 0;
	}
}