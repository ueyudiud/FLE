package fle.cg.recipe;

import java.awt.Rectangle;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.GuiBookBase;
import fle.api.cg.RecipeHandler;
import fle.api.recipe.CraftingState;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.init.IB;

public class PolishRecipe extends RecipeBase implements RecipeHandler
{
	private ItemStack output;
	private ItemAbstractStack input;
	private CraftingState[] states;
	
	public PolishRecipe(fle.core.recipe.FLEPolishRecipe.PolishRecipe recipe)
	{
		output = recipe.output.copy();
		input = recipe.getinput();
		states = CraftingState.getStates(recipe.getRecipeMap());
	}

	@Override
	public boolean match(ItemStack target)
	{
		return output.isItemEqual(target) || input.isStackEqul(target) || new ItemBaseStack(IB.woodMachine, 0).isStackEqul(target);
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
		gui.drawTexturedModalRect(xOffset, yOffset, 79, 174, 79, 58);
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				gui.drawCondition(xOffset + 5 + 17 * i, yOffset + 4 + 17 * j, states[i + j * 3], false);
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
		return new Rectangle(57, 4 + index * 17, 16, 16);
	}

	@Override
	public Rectangle getTankPosition(int index)
	{
		return null;
	}

	@Override
	public ItemStack getShowStackInSlot(int index)
	{
		return index == 0 ? input.toArray().get(0) : index == 1 ? new ItemStack(IB.woodMachine, 1, 0) : output.copy();
	}

	@Override
	public ItemAbstractStack getStackInSlot(int index)
	{
		return index == 0 ? input : index == 1 ? new ItemBaseStack(IB.woodMachine, 0) : new ItemBaseStack(output);
	}

	@Override
	public FluidStack getFluidInTank(int index)
	{
		return null;
	}

	@Override
	public String getTip(int mouseX, int mouseY)
	{
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				if(new Rectangle(5 + i * 17, 4 + j * 17, 16, 16).contains(mouseX, mouseY))
					return states[i + j * 3].getName();
		return null;
	}
}