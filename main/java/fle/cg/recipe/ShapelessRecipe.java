package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapelessFleRecipe;
import fle.cg.GuiBookBase;
import fle.cg.RecipeHandler;

public class ShapelessRecipe extends RecipeBase implements RecipeHandler
{
	protected boolean isSmallRecipe;
	public ItemAbstractStack[] stacks;
	public ItemStack output;

	public ShapelessRecipe(ShapelessFleRecipe recipe)
	{
		stacks = recipe.getInputs().clone();
		isSmallRecipe = recipe.getRecipeSize() <= 4;
		output = recipe.getRecipeOutput().copy();
	}
	public ShapelessRecipe(ShapelessOreRecipe recipe)
	{
		List<Object> inputs = recipe.getInput();
		stacks = new ItemAbstractStack[inputs.size()];
		int i = 0;
		for(Object obj : inputs)
		{
			if(obj instanceof ItemStack)
			{
				stacks[i] = new ItemBaseStack((ItemStack) obj);
			}
			else if(obj instanceof List)
			{
				stacks[i] = new ItemArrayStack((List) obj);
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
		for(int i = 0; i < ts.length; ++i)
		{
			stacks[i] = new ItemBaseStack((ItemStack) ts[i]);
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

	@Override
	public void onUpdate(GuiBookBase gui)
	{
		
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
		if(stacks[index] == null) return null;
		if(stacks[index].toArray() == null) return null;
		else if(stacks[index].toArray().isEmpty()) return null;
		ItemStack ret = stacks[index].toArray().get(0).copy();
		ret.stackSize = 1;
		return ret;
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
}