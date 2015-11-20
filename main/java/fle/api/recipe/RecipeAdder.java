package fle.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;
import fle.api.util.DropInfo;

/**
 * The recipe adder of FLE and compact some other mods recipe list.
 * You can add recipe here.
 * @author ueyudiud
 *
 */
public interface RecipeAdder
{
	void addWashingRecipe(ItemAbstractStack input, DropInfo info);

	void addPolishRecipe(ItemAbstractStack input, String map, ItemStack output);
	
	void addDryingRecipe(ItemAbstractStack input, int tick, ItemStack output);
	
	void addColdForgingRecipe(ItemAbstractStack[] input, String map, ItemStack output);
	
	void addStoneMillRecipe(ItemAbstractStack input, int tick, SolidStack output1, FluidStack output2);

	void addSifterRecipe(ItemAbstractStack input, SolidStack output1, float chance, ItemStack output2);

	void addSifterRecipe(Solid input, SolidStack output1, float chance, ItemStack output2);
	
	void addOilMillRecipe(ItemAbstractStack input, FluidStack output1, float change, ItemStack output2);
	
	void addBoilingRecipe(ItemAbstractStack input1, FluidStack input2, int energyRequire, ItemStack output);
	
	void addRCBlastFurnaceRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput);
	
	void addRCCokeOvenRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput1, FluidStack aOutput2);
	
	void addRCRollingRecipe(ItemStack aOutput, Object...objects);
}