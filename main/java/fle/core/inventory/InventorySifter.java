package fle.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.gui.GuiError;
import fle.api.inventory.InventorySolidTank;
import fle.api.recipe.IRecipeHandler.RecipeKey;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.core.init.Lang;
import fle.core.recipe.FLESifterRecipe;
import fle.core.recipe.FLESifterRecipe.SifterKey;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.RecipeHelper.FDType;
import fle.core.te.TileEntitySifter;

public class InventorySifter extends InventorySolidTank<TileEntitySifter>
{
	private static final int[] a = {0};
	private static final int[] b = {1, 2};

	private SolidTank inputTank = new SolidTank(1000);
	private RecipeKey key;
	public int recipeTick;
	
	public InventorySifter()
	{
		super(4, 1000);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		recipeTick = nbt.getShort("RecipeTick");
		key = FLESifterRecipe.getInstance().getRecipeKey(nbt.getString("Recipe"));
		NBTTagCompound nbt1 = nbt.getCompoundTag("InputTank");
		inputTank.readFromNBT(nbt1);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("RecipeTick", (short) recipeTick);
		if(key != null)
			nbt.setString("Recipe", key.toString());
		nbt.setTag("InputTank", inputTank.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void updateEntity(TileEntitySifter tile)
	{
		RecipeHelper.fillOrDrainInventoryTank(this, sTank, 2, 3, FDType.F);
		RecipeHelper.fillOrDrainInventoryTank(this, inputTank, 2, 3, FDType.D);
		if(key == null)
		{
			if(stacks[0] != null)
			{
				SifterRecipe recipe = FLESifterRecipe.getInstance().getRecipe(new SifterKey(stacks[0]));
				if(recipe != null)
				{
					RecipeHelper.onInputItemStack(this, 0);
					key = recipe.getRecipeKey();
				}
				else
				{
					tile.type = GuiError.CAN_NOT_INPUT;
				}
			}
			else if(inputTank.getStack() != null)
			{
				SifterRecipe recipe = FLESifterRecipe.getInstance().getRecipe(new SifterKey(inputTank.getStack()));
				if(recipe != null)
				{
					inputTank.drain(1, true);
					key = recipe.getRecipeKey();
					recipeTick = 180;
				}
			}
		}
		if(key != null)
		{
			++recipeTick;
			if(recipeTick > 200)
			{
				SifterRecipe recipe = FLESifterRecipe.getInstance().getRecipe(key);
				if(recipe != null)
				{
					if(RecipeHelper.matchOutSolidStack(sTank, recipe.output1) && RecipeHelper.matchOutput(this, 1, recipe.output2))
					{
						RecipeHelper.onOutputSolidStack(sTank, recipe.output1);
						RecipeHelper.onOutputItemStack(this, 1, recipe.getRandOutput());
						recipeTick = 0;
						key = null;
					}
					else
					{
						tile.type = GuiError.CAN_NOT_OUTPUT;
						recipeTick = 200;
					}
				}
				else
				{
					key = null;
				}
			}
		}
	}

	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack)
	{
		return i == 0;
	}

	protected boolean isInputSlot(int i)
	{
		return i == 0;
	}

	protected boolean isOutputSlot(int i)
	{
		return i == 1 || i == 2;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return dir == ForgeDirection.UP ? a : dir == ForgeDirection.DOWN ? b : null;
	}

	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.UP && isInputSlot(aSlotID);
	}

	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aResource,
			ForgeDirection aDirection)
	{
		return aDirection == ForgeDirection.DOWN && isOutputSlot(aSlotID);
	}

	@Override
	public String getInventoryName()
	{
		return Lang.inventory_sifter;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public int getSizeSolidTank()
	{
		return 2;
	}
	
	@Override
	public SolidTank getSolidTank(int index)
	{
		return index == 0 ? inputTank : sTank;
	}
	
	@Override
	public SolidStack getSolidStackInTank(int index)
	{
		return index == 0 ? inputTank.getStack() : sTank.getStack();
	}
	
	@Override
	protected SolidTank getTankFromSide(ForgeDirection aSide) 
	{
		return aSide == ForgeDirection.UP ? inputTank : aSide == ForgeDirection.DOWN ? sTank : null;
	}
}