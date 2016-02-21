package flapi.recipe;

import flapi.recipe.stack.AbstractStack;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * The recipe adder of FLE and compact some other mods recipe list.
 * You can add recipe here.
 * @author ueyudiud
 *
 */
@Deprecated
public interface RecipeAdder
{
	void addPlayerCraftingRecipe(IPlayerToolCraftingRecipe recipe);
	
	void addPlayerCraftingRecipe(AbstractStack input1, AbstractStack input2, AbstractStack tool, float toolDamage, ItemStack output);
	
	void addWashingRecipe(AbstractStack input, DropInfo info);

	void addPolishRecipe(AbstractStack input, String map, ItemStack output);
	
//	RecipeHandlerI getDryingHandler();
	
	void addColdForgingRecipe(AbstractStack[] input, String map, ItemStack output);
	
	void addStoneMillRecipe(AbstractStack input, int tick, SolidStack output1, FluidStack output2);

	void addSifterRecipe(String name, AbstractStack input, SolidStack output1, float chance, ItemStack output2);

	void addSifterRecipe(String name, Solid input, SolidStack output1, float chance, ItemStack output2);
	
	void addOilMillRecipe(String name, AbstractStack input, FluidStack output1, float change, ItemStack output2);
	
	void addBoilingRecipe(String name, AbstractStack input1, FluidStack input2, int energyRequire, ItemStack output);
	
	void addSoakRecipe(String name, FluidStack fInput, AbstractStack iInput, int tick, ItemStack output);
	
	void addRCBlastFurnaceRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput);
	
	void addRCCokeOvenRecipe(ItemStack aInput, boolean flag, int tick, ItemStack aOutput1, FluidStack aOutput2);
	
	void addRCRollingRecipe(ItemStack aOutput, Object...objects);
}