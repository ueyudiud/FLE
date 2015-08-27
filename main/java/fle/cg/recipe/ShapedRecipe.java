package fle.cg.recipe;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapedFleRecipe;
import fle.cg.GuiBookBase;
import fle.cg.RecipeHandler;

public class ShapedRecipe extends RecipeBase implements RecipeHandler
{
	private static boolean init = false;
	protected static Field oreRecipeWidth;
	protected static Field oreRecipeHeight;
	
	private static void init()
	{
		try
		{
			oreRecipeWidth = ShapedOreRecipe.class.getDeclaredField("width");
			oreRecipeHeight = ShapedOreRecipe.class.getDeclaredField("height");
			init = true;
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	protected int xSize;
	protected int ySize;
	protected boolean isSmallRecipe;
	public ItemAbstractStack[] stacks;
	public ItemStack output;

	public ShapedRecipe(ShapedFleRecipe recipe)
	{
		stacks = recipe.getInputs().clone();
		isSmallRecipe = recipe.getXSize() <= 2 && recipe.getYSize() <= 2;
		output = recipe.getRecipeOutput().copy();
		xSize = recipe.getXSize();
		ySize = recipe.getYSize();
	}
	public ShapedRecipe(ShapedOreRecipe recipe)
	{
		Object[] inputs = recipe.getInput();
		stacks = new ItemAbstractStack[inputs.length];
		for(int i = 0; i < inputs.length; ++i)
		{
			if(inputs[i] instanceof ItemStack)
			{
				stacks[i] = new ItemBaseStack((ItemStack) inputs[i]);
			}
			else if(inputs[i] instanceof List)
			{
				stacks[i] = new ItemArrayStack((List) inputs[i]);
			}
		}
		isSmallRecipe = inputs.length <= 4 && inputs.length != 3;
		output = recipe.getRecipeOutput().copy();
		if(!init) init();
		try
		{
			xSize = oreRecipeWidth.getInt(recipe);
			ySize = oreRecipeHeight.getInt(recipe);
		}
		catch(Throwable e)
		{
			xSize = ySize = isSmallRecipe ? 2 : 3;
		}
	}
	public ShapedRecipe(ShapedRecipes recipe)
	{
		stacks = new ItemAbstractStack[recipe.recipeItems.length];
		for(int i = 0; i < recipe.recipeItems.length; ++i)
		{
			stacks[i] = new ItemBaseStack(recipe.recipeItems[i]);
		}
		isSmallRecipe = recipe.recipeHeight <= 2 && recipe.recipeWidth <= 2;
		output = recipe.getRecipeOutput().copy();
		xSize = recipe.recipeWidth;
		ySize = recipe.recipeHeight;
	}

	@Override
	public boolean match(ItemStack target)
	{
		for(int i = 0; i < stacks.length; ++i)
		{
			if(stacks[i] == null) continue;
			if(stacks[i].isStackEqul(target)) return true;
		}
		return output.isItemEqual(target);
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
			gui.drawTexturedModalRect(xOffset, yOffset, 0, 58, 79, 58);
		}
		else
		{
			gui.drawTexturedModalRect(xOffset, yOffset, 0, 0, 79, 58);
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
		return isSmallRecipe ? new Rectangle(12 + (index % xSize) * 18, 12 + (index / xSize) * 18, 16, 16) : new Rectangle(3 + (index % xSize) * 18, 3 + (index / xSize) * 18, 16, 16);
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