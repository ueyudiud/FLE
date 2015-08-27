package fle.cg.recipe;

import java.awt.Rectangle;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.ItemAbstractStack;
import fle.cg.GuiBookBase;
import fle.cg.RecipeHandler;

public class PolishRecipe extends RecipeBase implements RecipeHandler
{

	@Override
	public boolean match(ItemStack target)
	{
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
		
	}

	@Override
	public void drawRecipeFortground(GuiBookBase gui, int xOffset, int yOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(GuiBookBase gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSlotContain() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTankContain() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Rectangle getSlotPosition(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getTankPosition(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getShowStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemAbstractStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack getFluidInTank(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTip(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		return null;
	}

}
