package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.api.gui.GuiError;
import fle.api.inventory.InventoryTWFTC;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.IRecipeHandler.RecipeKey;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.core.recipe.FLEStoneMillRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipeKey;
import fle.core.recipe.RecipeHelper.FDType;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityStoneMill;

public class InventoryStoneMill extends InventoryTWFTC<TileEntityStoneMill>
{
	private int recipeTick;
	private RecipeKey recipe;
	public SolidTank sTank = new SolidTank(1000);
	
	public InventoryStoneMill()
	{
		super("inventory.stone.mill", 3, 2000);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		recipeTick = nbt.getInteger("RecipeTime");
		recipe = FLEStoneMillRecipe.getInstance().getRecipeKey(nbt.getString("RecipeName"));
		sTank.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("RecipeTime", recipeTick);
		if(recipe != null)
			nbt.setString("RecipeName", recipe.toString());
		sTank.writeToNBT(nbt);
	}
	
	@Override
	public void updateEntity(TileEntityStoneMill tile)
	{
		super.updateEntity(tile);
		double energy = tile.getRotationHelper().getRotationEnergy();
		if(energy > 200)
		{
			onWork(tile, (int) Math.log(energy / 200D + Math.E));
			tile.getRotationHelper().minusInnerEnergy(200.0D);
			tile.markRenderForUpdate();
		}
		RecipeHelper.fillOrDrainInventoryTank(this, sTank, 1, 2, FDType.F);
	}
	
	public void onWork(TileEntityStoneMill tile, int speed)
	{
		if(recipe == null)
		{
			recipeTick = 0;
			MachineRecipe str = FLEStoneMillRecipe.getInstance().getRecipe(new StoneMillRecipeKey(stacks[0]));
			if(str != null)
			{
				RecipeHelper.onInputItemStack(this, 0);
				recipe = str.getRecipeKey();
				tile.type = GuiError.DEFAULT;
			}
			else
			{
				tile.type = GuiError.CAN_NOT_INPUT;
			}
		}
		if(recipe != null)
		{
			int tTime = ((StoneMillRecipeKey) recipe).getRecipeTick();
			if(tTime == -1)
			{
				recipe = null;
				return;
			}
			if(tile.type == GuiError.RAINING) tile.type = GuiError.DEFAULT;
			recipeTick += speed;
			if(recipeTick >= tTime)
			{
				StoneMillRecipe r = FLEStoneMillRecipe.getInstance().getRecipe(recipe);
				if(r == null)
				{
					recipeTick = 0;
					recipe = null;
					return;
				}
				SolidStack output = r.getOutput();
				FluidStack output1 = r.getFluidOutput();
				if(RecipeHelper.matchOutSolidStack(sTank, output) && RecipeHelper.matchOutFluidStack(tank, output1))
				{
					RecipeHelper.onOutputSolidStack(sTank, output);
					RecipeHelper.onOutputFluidStack(tank, output1);
					recipe = null;
					recipeTick = 0;
					tile.type = GuiError.DEFAULT;
				}
				else
				{
					recipeTick = ((StoneMillRecipeKey) recipe).getRecipeTick();
					tile.type = GuiError.CAN_NOT_OUTPUT;
				}
			}
		}
	}

	@Override
	protected void onMelted(TileEntityStoneMill tile)
	{
		
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
		return dir == ForgeDirection.UP ? new int[]{0} : dir != ForgeDirection.DOWN ? new int[]{1, 2, 3} : null;
	}

	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	protected boolean isInputSlot(int i)
	{
		return i == 0;
	}

	@Override
	protected boolean isOutputSlot(int i)
	{
		return i != 0;
	}

	public double getProgress()
	{
		return recipe == null ? 0D : (double) recipeTick / (double) ((StoneMillRecipeKey) recipe).getRecipeTick();
	}

	public boolean isWorking()
	{
		return recipe != null;
	}
}