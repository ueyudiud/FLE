/*
 * copyright 2016-2018 ueyudiud
 */
package fle.compat.jei;

import fle.compat.jei.recipe.ShapedCraftingRecipeHandler;
import fle.compat.jei.recipe.ShapelessCraftingRecipeHandler;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

/**
 * @author ueyudiud
 */
@JEIPlugin
public class JEICompat extends BlankModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		final IJeiHelpers helpers = registry.getJeiHelpers();
		registry.addRecipeHandlers(
				new ShapedCraftingRecipeHandler(helpers),
				new ShapelessCraftingRecipeHandler(helpers));
	}
}
