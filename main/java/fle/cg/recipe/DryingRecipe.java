package fle.cg.recipe;

import java.awt.Rectangle;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.GuiBookBase;
import fle.api.cg.RecipeHandler;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.init.IB;

public class DryingRecipe extends RecipeBase implements RecipeHandler
{
	ItemAbstractStack input;
	int tick;
	ItemStack output;
	
	public DryingRecipe(fle.core.recipe.FLEDryingRecipe.DryingRecipe recipe)
	{
		input = recipe.input;
		tick = recipe.recipeTime;
		output = recipe.output.copy();
	}

	@Override
	public boolean match(ItemStack target)
	{
		return input.isStackEqul(input) || output.isItemEqual(target) || new ItemBaseStack(IB.woodMachine1, 0).isStackEqul(target);
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
		gui.drawTexturedModalRect(xOffset, yOffset, 158, 0, 79, 58);
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
		return 3;
	}

	@Override
	public int getTankContain()
	{
		return 0;
	}

	@Override
	public Rectangle getSlotPosition(int index)
	{
		switch(index)
		{
		case 0 : return new Rectangle(13, 30, 16, 16);
		case 1 : return new Rectangle(50, 21, 16, 16);
		case 2 : return new Rectangle(13, 12, 16, 16);
		}
		return null;
	}

	@Override
	public Rectangle getTankPosition(int index)
	{
		return null;
	}

	@Override
	public ItemStack getShowStackInSlot(int index)
	{
		return index == 0 ? input.toArray().get(0) : index == 1 ? output.copy() : new ItemStack(IB.woodMachine1, 1, 0);
	}

	@Override
	public ItemAbstractStack getStackInSlot(int index)
	{
		return index == 0 ? input : index == 1 ? new ItemBaseStack(output) : new ItemBaseStack(IB.woodMachine1, 0);
	}

	@Override
	public FluidStack getFluidInTank(int index)
	{
		return null;
	}

	@Override
	public String getTip(int mouseX, int mouseY)
	{
		if(new Rectangle(30, 20, 19, 18).contains(mouseX, mouseY))
		{
			return "Recipe tick: " + tick;
		}
		return null;
	}
}