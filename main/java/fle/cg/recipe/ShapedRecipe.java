package fle.cg.recipe;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import fle.api.cg.GuiBookBase;
import fle.api.cg.RecipeHandler;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapedFleRecipe;

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
	private Object[][] showArray;

	public ShapedRecipe(ShapedFleRecipe recipe)
	{
		stacks = recipe.getInputs().clone();
		showArray = new Object[stacks.length][];
		for(int i = 0; i < stacks.length; ++i)
		{
			if(stacks[i] != null)
				showArray[i] = stacks[i].toArray().toArray();
		}
		isSmallRecipe = recipe.getXSize() <= 2 && recipe.getYSize() <= 2;
		output = recipe.getRecipeOutput().copy();
		xSize = recipe.getXSize();
		ySize = recipe.getYSize();
	}
	public ShapedRecipe(ShapedOreRecipe recipe)
	{
		Object[] inputs = recipe.getInput();
		stacks = new ItemAbstractStack[inputs.length];
		showArray = new Object[inputs.length][];
		for(int i = 0; i < inputs.length; ++i)
		{
			if(inputs[i] instanceof ItemStack)
			{
				stacks[i] = new ItemBaseStack((ItemStack) inputs[i]);
				showArray[i] = new Object[]{inputs[i]};
			}
			else if(inputs[i] instanceof List)
			{
				stacks[i] = new ItemArrayStack((List) inputs[i]);
				showArray[i] = stacks[i].toArray().toArray();
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
		showArray = new Object[stacks.length][];
		for(int i = 0; i < recipe.recipeItems.length; ++i)
		{
			stacks[i] = new ItemBaseStack(recipe.recipeItems[i]);
			if(recipe.recipeItems[i] != null)
				showArray[i] = new Object[]{recipe.recipeItems[i]};
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
	
	private int updateTick = 0;

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