/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeAdder
{
	public static void addPolishRecipe(AbstractStack input, String map, ItemStack output)
	{
		PolishRecipe.addPolishRecipe(input, map, output);
	}
}