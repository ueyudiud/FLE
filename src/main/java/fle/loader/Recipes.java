/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import fle.api.client.PolishingStateIconLoader;
import fle.api.recipes.instance.FlamableItems;
import fle.loader.recipe.RecipePolish;
import nebula.common.stack.BaseStack;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class Recipes
{
	public static void init()
	{
		FlamableItems.addFlamableItem(new BaseStack(Items.PAPER), 60F, 200F);
		RecipePolish.init();
	}
	
	@SideOnly(Side.CLIENT)
	public static void addRenderStates()
	{
		PolishingStateIconLoader.addState(' ', "fle:states/default");
		PolishingStateIconLoader.addState('c', "fle:states/crush");
		PolishingStateIconLoader.addState('p', "fle:states/polish");
	}
}