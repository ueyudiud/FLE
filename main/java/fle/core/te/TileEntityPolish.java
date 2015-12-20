package fle.core.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.enums.EnumCraftingType;
import flapi.gui.GuiCondition;
import flapi.gui.GuiError;
import flapi.item.interfaces.IPolishTool;
import flapi.recipe.CraftingState;
import flapi.te.TEInventory;
import fle.core.init.Lang;
import fle.core.recipe.FLEPolishRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipeKey;
import fle.core.recipe.RecipeHelper;

public class TileEntityPolish extends TEInventory
{
	ItemStack input;
	ItemStack lastSlot;
	char[] inputMap = "         ".toCharArray();
	GuiCondition condition = GuiError.DEFAULT;
	
	public TileEntityPolish()
	{
		super(3);
	}
	
	@Override
	protected String getDefaultName()
	{
		return Lang.inventory_polishTable;
	}
	
	int tick = 0;

	public void clearMap()
	{
		input = null;
		stacks[2] = null;
		inputMap = "         ".toCharArray();
	}
	
	public void craftedOnce(EntityPlayer player, int id)
	{
		if(stacks[1] != null)
		{
			ItemStack i = stacks[1].copy();
			if(input == null)
			{
				if(FLEPolishRecipe.canPolish(stacks[0]))
				{
					input = stacks[0].copy();
					RecipeHelper.onInputItemStack(this, 0);
					input.stackSize = 1;
					condition = GuiError.DEFAULT;
				}
				else
				{
					condition = GuiError.CAN_NOT_INPUT;
				}
			}
			if(i.getItem() instanceof IPolishTool && input != null)
			{
				inputMap[id] = ((IPolishTool) i.getItem()).getState(stacks[1], EnumCraftingType.polish, CraftingState.getState(inputMap[id])).getCharIndex();
				stacks[1] = ((IPolishTool) i.getItem()).getOutput(player, i);
				if(stacks[1].stackSize <= 0)
				{
					stacks[1] = null;
				}
				PolishRecipe recipe = FLEPolishRecipe.getInstance().getRecipe(new PolishRecipeKey(input, new String(inputMap)));
				if(recipe != null)
				{
					ItemStack itemstack = recipe.output.copy();
					if(RecipeHelper.matchOutput(this, 2, itemstack))
						RecipeHelper.onOutputItemStack(this, 2, itemstack);
				}
				condition = GuiError.DEFAULT;
			}
			else
			{
				condition = GuiError.CAN_NOT_INPUT;
			}
		}
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		ItemStack ret = stacks[i].copy();
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		if(i == 2)
		{
			clearMap();
		}
		return ret;
	}
	
	@SideOnly(Side.CLIENT)
	public char[] getStates()
	{
		return inputMap;
	}

	@SideOnly(Side.CLIENT)
	public GuiCondition getCondition()
	{
		return condition;
	}

	public ItemStack getRecipeInput()
	{
		return input;
	}

	@Override
	public void update()
	{
		++tick;
		if(tick > 100)
		{
			syncSlot();
			tick = 0;
		}
	}
	
	@Override
	public boolean canUpdate() 
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(ForgeDirection dir)
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack resource,
			ForgeDirection direction)
	{
		return false;
	}
}