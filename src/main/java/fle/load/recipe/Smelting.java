package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.lib.stack.BaseStack;
import fle.api.recipe.smelting.DiviableFoodSmeltingRecipe;
import fle.api.recipe.smelting.SmeltingRecipes;
import fle.api.recipe.smelting.SmeltingRecipes.SmeltingRecipe;

public class Smelting
{
	public static void init()
	{
		SmeltingRecipes.addSmeltingRecipe("fle:beef", 
				new DiviableFoodSmeltingRecipe(new BaseStack(EnumItem.food_divide_smeltable.instance(1, "beef_raw")), 4800000, 450, EnumItem.food_divide_smeltable.instance(1, "beef")));
		SmeltingRecipes.addSmeltingRecipe("fle:chicken", 
				new DiviableFoodSmeltingRecipe(new BaseStack(EnumItem.food_divide_smeltable.instance(1, "chicken_raw")), 4800000, 450, EnumItem.food_divide_smeltable.instance(1, "chicken")));
//		SmeltingRecipes.addSmeltingRecipe("fle:fish", 
//				new SmeltingRecipe(new BaseStack(EnumItem.food_smeltable.instance(1, "fish_raw")), 120000, 450, EnumItem.food_smeltable.instance(1, "fish_kebab")));
		SmeltingRecipes.addSmeltingRecipe("fle:pork", 
				new DiviableFoodSmeltingRecipe(new BaseStack(EnumItem.food_divide_smeltable.instance(1, "pork_raw")), 4800000, 450, EnumItem.food_divide_smeltable.instance(1, "pork")));
		SmeltingRecipes.addSmeltingRecipe("fle:squid", 
				new DiviableFoodSmeltingRecipe(new BaseStack(EnumItem.food_divide_smeltable.instance(1, "squid_raw")), 4800000, 450, EnumItem.food_divide_smeltable.instance(1, "squid")));
		SmeltingRecipes.addSmeltingRecipe("fle:lamb", 
				new DiviableFoodSmeltingRecipe(new BaseStack(EnumItem.food_divide_smeltable.instance(1, "lamb_raw")), 4800000, 450, EnumItem.food_divide_smeltable.instance(1, "lamb")));
		
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