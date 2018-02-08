/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.compat.jei.recipe;

import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.SingleInputMatch;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

/**
 * @author ueyudiud
 */
public class ShapelessCraftingRecipeHandler implements IRecipeHandler<ShapelessFleRecipe>
{
	private final IJeiHelpers helpers;
	
	public ShapelessCraftingRecipeHandler(IJeiHelpers helpers)
	{
		this.helpers = helpers;
	}
	
	@Override
	public Class<ShapelessFleRecipe> getRecipeClass()
	{
		return ShapelessFleRecipe.class;
	}
	
	@Override
	public String getRecipeCategoryUid()
	{
		return VanillaRecipeCategoryUid.CRAFTING;
	}
	
	@Override
	public String getRecipeCategoryUid(ShapelessFleRecipe recipe)
	{
		return VanillaRecipeCategoryUid.CRAFTING;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(ShapelessFleRecipe recipe)
	{
		return new ShapelessCraftingRecipeWrapper(recipe, this.helpers.getGuiHelper());
	}
	
	@Override
	public boolean isRecipeValid(ShapelessFleRecipe recipe)
	{
		if (!recipe.output.valid())
			return false;
		for (SingleInputMatch match : recipe.inputs)
			if (!match.input.valid())
				return false;
		return true;
	}
}
