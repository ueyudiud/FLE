/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import fle.api.client.PolishingStateIconLoader;
import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.instance.FlamableItems;
import fle.loader.recipe.RecipeCraftingTool;
import fle.loader.recipe.RecipeDirtMixture;
import fle.loader.recipe.RecipeDrying;
import fle.loader.recipe.RecipeMachines;
import fle.loader.recipe.RecipePolish;
import fle.loader.recipe.RecipeResource1;
import fle.loader.recipe.RecipeWashingBarGrizzly;
import nebula.common.stack.BaseStack;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

/**
 * @author ueyudiud
 */
public class Recipes
{
	public static void init()
	{
		FlamableItems.addFlamableItem(new BaseStack(Items.PAPER), 60F, 200F);
		RecipeCraftingTool.init();
		RecipePolish.init();
		RecipeWashingBarGrizzly.init();
		RecipeDrying.init();
		RecipeResource1.init();
		RecipeMachines.init();
		RecipeDirtMixture.init();
		
		RecipeSorter.register("fle.shaped", ShapedFleRecipe.class, Category.SHAPED, "after:minecraft:shaped before:forge:shapedore before:minecraft:shapeless");
		RecipeSorter.register("fle.shapeless", ShapelessFleRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless before:forge:shapelessore");
	}
	
	@SideOnly(Side.CLIENT)
	public static void addRenderStates()
	{
		PolishingStateIconLoader.addState(' ', "fle:states/default");
		PolishingStateIconLoader.addState('c', "fle:states/crush");
		PolishingStateIconLoader.addState('p', "fle:states/polish");
	}
}