package fle.core.te;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.recipe.IRecipeHandler.RecipeKey;
import flapi.solid.SolidTank;
import flapi.te.TEISolidTank;
import fle.core.init.Lang;
import fle.core.recipe.FLESifterRecipe;
import fle.core.recipe.FLESifterRecipe.SifterKey;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;
import fle.core.recipe.RecipeHelper;
import fle.core.recipe.RecipeHelper.FDType;

public class TileEntitySifter extends TEISolidTank
{
	public GuiCondition type;

	private static final int[] a = {0};
	private static final int[] b = {1, 2};

	private SolidTank inputTank = new SolidTank(1000);
	private RecipeKey key;
	public int recipeTick;
	
	public TileEntitySifter()
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
	protected void update()
	{
		if(isClient()) return;
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
					type = GuiError.DEFAULT;
				}
				else
				{
					type = GuiError.CAN_NOT_INPUT;
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
						type = GuiError.DEFAULT;
					}
					else
					{
						type = GuiError.CAN_NOT_OUTPUT;
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
	
	@SideOnly(Side.CLIENT)
	public int getRecipeProgress(int length)
	{
		return (int) ((double) recipeTick / 200 * length);
	}

	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_sifter;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
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
	public int getProgressSize()
	{
		return 1;
	}
	
	@Override
	public int getProgress(int id)
	{
		return id == 0 ? recipeTick : 0;
	}
	
	@Override
	public void setProgress(int id, int value)
	{
		if(id == 0) recipeTick = value;
	}
}