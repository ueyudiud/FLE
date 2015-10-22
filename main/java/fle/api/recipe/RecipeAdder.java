package fle.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;

public interface RecipeAdder
{
	public void addPolishRecipe(ItemAbstractStack input, String map, ItemStack output);
	
	public void addDryingRecipe(ItemAbstractStack input, int tick, ItemStack output);
	
	public void addColdForgingRecipe(ItemAbstractStack[] input, String map, ItemStack output);
	
	public void addStoneMillRecipe(ItemAbstractStack input, int tick, SolidStack output1, FluidStack output2);

	public void addSifterRecipe(ItemAbstractStack input, SolidStack output1, float chance, ItemStack output2);

	public void addSifterRecipe(Solid input, SolidStack output1, float chance, ItemStack output2);
	
	public void addOilMillRecipe(ItemAbstractStack input, FluidStack output1, float change, ItemStack output2);
	
	public void addBoilingRecipe(ItemAbstractStack input1, FluidStack input2, int energyRequire, ItemStack output);
	
	public void addRCBlastFurnaceRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput);
	
	public void addRCCokeOvenRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput1, FluidStack aOutput2);
	
	public void addRCRollingRecipe(ItemStack aOutput, Object...objects);
}