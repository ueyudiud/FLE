package fle.api.cg;

import java.awt.Rectangle;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.ItemAbstractStack;

@Deprecated
public interface RecipeHandler
{
	boolean match(ItemStack target);
	
	boolean match(FluidStack target);
	
	void drawRecipeBackground(GuiBookBase gui, int xOffset, int yOffset);

	void drawRecipeFortground(GuiBookBase gui, int xOffset, int yOffset);
	
	void onUpdate(GuiBookBase gui);
	
	int getSlotContain();
	
	int getTankContain();
	
	Rectangle getSlotPosition(int index);
	
	Rectangle getTankPosition(int index);
	
	ItemStack getShowStackInSlot(int index);
	
	ItemAbstractStack getStackInSlot(int index);
	
	FluidStack getFluidInTank(int index);
	
	String getTip(int mouseX, int mouseY);

	String getStackTip(int slotID);
}