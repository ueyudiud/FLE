package fla.core.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import fla.api.recipe.DropInfo;
import fla.api.recipe.IItemChecker;
import fla.api.recipe.RecipeRegistry;
import fla.core.recipe.machine.PolishRecipe;
import fla.core.tool.WasherManager;

public class FlaRecipeRegistry extends RecipeRegistry
{
	@Override
	public void addCraftingRecipe(IRecipe recipe) 
	{
		GameRegistry.addRecipe(recipe);
	}

	@Override
	public void addWashingRecipe(DropInfo output) 
	{
		WasherManager.registryDust(output);
	}

	@Override
	public void addPolishRecipe(IItemChecker input, String map, ItemStack output) 
	{
		PolishRecipe.registerRecipe(new PolishRecipe(input, map, output));
	}
}
