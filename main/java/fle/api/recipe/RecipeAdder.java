package fle.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeAdder
{
	public void addPolishRecipe(ItemAbstractStack input, String map, ItemStack output);
	
	public void addDryingRecipe(ItemAbstractStack input, int tick, ItemStack output);
	
	public void addRCBlastFurnaceRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput);
	
	public void addRCCokeOvenRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput1, FluidStack aOutput2);
	
	public void addRCRollingRecipe(ItemStack aOutput, Object...objects);
}