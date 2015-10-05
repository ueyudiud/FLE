package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import fle.api.cg.GuiBookBase;
import fle.api.cg.RecipeHandler;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapelessFleRecipe;

public class ShapelessRecipe extends RecipeBase implements RecipeHandler
{
	protected boolean isSmallRecipe;
	public ItemAbstractStack[] stacks;
	public ItemStack output;
	public Object[][] showArray;

	public ShapelessRecipe(ShapelessFleRecipe recipe)
	{
		stacks = recipe.getInputs().clone();
		showArray = new Object[stacks.length][];
		for(int i = 0; i < stacks.length; ++i)
			showArray[i] = stacks[i].toArray().toArray();
		isSmallRecipe = recipe.getRecipeSize() <= 4;
		output = recipe.getRecipeOutput().copy();
	}
	public ShapelessRecipe(ShapelessOreRecipe recipe)
	{
		List<Object> inputs = recipe.getInput();
		stacks = new ItemAbstractStack[inputs.size()];
		showArray = new Object[stacks.length][];
		int i = 0;
		for(Object obj : inputs)
		{
			if(obj instanceof ItemStack)
			{
				stacks[i] = new ItemBaseStack((ItemStack) obj);
				showArray[i] = new Object[]{obj};
			}
			else if(obj instanceof List)
			{
				stacks[i] = new ItemArrayStack((List) obj);
				showArray[i] = ((List) obj).toArray();
			}
			++i;
		}
		isSmallRecipe = inputs.size() <= 4;
		output = recipe.getRecipeOutput().copy();
	}
	public ShapelessRecipe(ShapelessRecipes recipe)
	{
		Object[] ts = recipe.recipeItems.toArray(new Object[recipe.recipeItems.size()]);
		stacks = new ItemAbstractStack[recipe.recipeItems.size()];
		showArray = new Object[stacks.length][];
		for(int i = 0; i < ts.length; ++i)
		{
			stacks[i] = new ItemBaseStack((ItemStack) ts[i]);
			showArray[i] = new Object[]{ts[i]};
		}
		isSmallRecipe = recipe.getRecipeSize() <= 4;
		output = recipe.getRecipeOutput().copy();
	}
	
	@Override
	public boolean match(ItemStack target)
	{
		for(ItemAbstractStack tStack : stacks)
		{
			if(tStack.isStackEqul(target)) return true;
		}
		return false;
	}

	@Override
	public boolean match(FluidStack target) 
	{
		return false;
	}

	@Override
	public void drawRecipeBackground(GuiBookBase gui, int xOffset, int yOffset)
	{
		gui.bindTexture(locate1);
		if(isSmallRecipe)
		{
			gui.drawTexturedModalRect(xOffset, yOffset, 0, 174, 79, 58);
		}
		else
		{
			gui.drawTexturedModalRect(xOffset, yOffset, 0, 116, 79, 58);
		}
	}

	@Override
	public void drawRecipeFortground(GuiBookBase gui, int xOffset, int yOffset)
	{
		
	}
	
	int updateTick;

	@Override
	public void onUpdate(GuiBookBase gui)
	{
		++updateTick;
	}

	@Override
	public int getSlotContain()
	{
		return stacks.length + 1;
	}

	@Override
	public int getTankContain()
	{
		return 0;
	}

	@Override
	public Rectangle getSlotPosition(int index)
	{
		if(index == stacks.length) return new Rectangle(59, 21, 16, 16);
		return isSmallRecipe ? new Rectangle(12 + (index % 2) * 18, 12 + (index / 2) * 18, 16, 16) : new Rectangle(3 + (index % 3) * 18, 3 + (index / 3) * 18, 16, 16);
	}

	@Override
	public Rectangle getTankPosition(int index)
	{
		return null;
	}

	@Override
	public ItemStack getShowStackInSlot(int index)
	{
		if(index == stacks.length) return output.copy();
		if(showArray[index] == null) return null;
		if(showArray[index].length == 0) return null;
		return (ItemStack) showArray[index][(updateTick / 80) % showArray[index].length];
	}

	@Override
	public ItemAbstractStack getStackInSlot(int index)
	{
		return index == stacks.length ? new ItemBaseStack(output) : stacks[index];
	}

	@Override
	public FluidStack getFluidInTank(int index)
	{
		return null;
	}

	@Override
	public String getTip(int mouseX, int mouseY)
	{
		return null;
	}
	
	@Override
	public String getStackTip(int slotID)
	{
		return ItemAbstractStack.getStackTipInfo(slotID == stacks.length ? output : stacks[slotID]);
	}
}