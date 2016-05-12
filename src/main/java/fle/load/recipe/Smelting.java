package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.lib.stack.BaseStack;
import fle.api.recipe.smelting.SmeltingRecipes;
import fle.api.recipe.smelting.SmeltingRecipes.SmeltingRecipe;

public class Smelting
{
	public static void init()
	{
		SmeltingRecipes.addSmeltingRecipe("fle:beef_kebab", 
				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "beef_kebab_raw")), 80000, 450, EnumItem.food_smeltable.instance(1, "beef_kebab")));
		SmeltingRecipes.addSmeltingRecipe("fle:chicken_kebab", 
				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "chicken_kebab_raw")), 80000, 450, EnumItem.food_smeltable.instance(1, "chicken_kebab")));
		SmeltingRecipes.addSmeltingRecipe("fle:fish_kebab", 
				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "fish_kebab_raw")), 80000, 450, EnumItem.food_smeltable.instance(1, "fish_kebab")));
		SmeltingRecipes.addSmeltingRecipe("fle:pork_kebab", 
				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "pork_kebab_raw")), 80000, 450, EnumItem.food_smeltable.instance(1, "pork_kebab")));
		SmeltingRecipes.addSmeltingRecipe("fle:squid_kebab", 
				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "squid_kebab_raw")), 80000, 450, EnumItem.food_smeltable.instance(1, "squid_kebab")));
	}
}