package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiError;
import flapi.recipe.IRecipeHandler.RecipeKey;
import flapi.te.TEInventory;
import fle.FLE;
import fle.core.init.Lang;
import fle.core.recipe.FLEDryingRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEDryingRecipe.DryingRecipeKey;
import fle.core.recipe.RecipeHelper;
import fle.core.util.WorldUtil;

public class TileEntityDryingTable extends TEInventory
{
	private int levelCheckBuffer;
	public double tem;
	public double water;
	public GuiError type;
	public RecipeKey recipeName;
	public int recipeTime;
	private int maxRecipeTime;
	
	public TileEntityDryingTable() 
	{
		super(2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		recipeTime = nbt.getInteger("RecipeTime");
		recipeName = nbt.hasKey("RecipeName") ? FLEDryingRecipe.getInstance().getRecipeKey(nbt.getString("RecipeName")) : null;
		if(recipeName != null)
			maxRecipeTime = ((DryingRecipeKey) recipeName).getDryingTick();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("RecipeTime", recipeTime);
		if(recipeName != null)
			nbt.setString("RecipeName", recipeName.toString());
	}
	
	public double getTempretureLevel()
	{
		return tem == 0D ? FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) : tem;
	}
	
	public double getWaterLevel() 
	{
		return water == 0D ? WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord) : water;
	}
	
	int tick = 0;
	
	@Override
	public void update() 
	{
		if(recipeName == null)
		{
			recipeTime = 0;
			DryingRecipe str = FLEDryingRecipe.getInstance().getRecipe(new DryingRecipeKey(stacks[0]));
			if(str != null)
			{
				RecipeHelper.onInputItemStack(this, 0);
				recipeName = str.getRecipeKey();
				maxRecipeTime = str.recipeTime;
			}
		}
		if(isClient()) return;
		if(recipeName != null && isCatchRain())
		{
			type = GuiError.RAINING;
		}
		else if(recipeName != null)
		{
			if(maxRecipeTime == 0)
			{
				recipeName = null;
				return;
			}
			if(type == GuiError.RAINING) type = GuiError.DEFAULT;
			double d1 = tem != 0D ? tem : getTempretureLevel();
			double d2 = water != 0D ? water : getWaterLevel();
			recipeTime += MathHelper.randomFloatClamp(rand, (float) (d1 / d2) * 0.8F, (float) (d1 / d2) * 1.2F);
			if(recipeTime >= maxRecipeTime)
			{
				DryingRecipe recipe = FLEDryingRecipe.getInstance().getRecipe(recipeName);
				if(recipe == null)
				{
					recipeName = null;
					recipeTime = maxRecipeTime = 0;
					return;
				}
				ItemStack output = recipe.output;
				if(RecipeHelper.matchOutput(this, 1, output))
				{
					RecipeHelper.onOutputItemStack(this, 1, output);
					recipeName = null;
					recipeTime = 0;
					type = GuiError.DEFAULT;
				}
				else
				{
					recipeTime = maxRecipeTime;
					type = GuiError.CAN_NOT_OUTPUT;
				}
			}
		}
		++levelCheckBuffer;
		if(levelCheckBuffer >= 200)
		{
			levelCheckBuffer = 0;
			tem = FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
			water = WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
		}
		++tick;
		if(tick > 100)
		{
			syncSlot();
			tick = 0;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return i == 0;
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgressBar(int i) 
	{
		return maxRecipeTime == 0 ? 0 : i * recipeTime / maxRecipeTime;
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_dryingTable;
	}

	protected boolean isInputSlot(int i) 
	{
		return i == 0;
	}

	protected boolean isOutputSlot(int i) 
	{
		return i == 1;
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
	
	@Override
	public int getProgressSize()
	{
		return 2;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? recipeTime : id == 1 ? maxRecipeTime : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) recipeTime = value;
		if(id == 1) maxRecipeTime = value;
	}
}