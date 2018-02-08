/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.compat.jei.recipe;

import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.SingleInputMatch;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

/**
 * @author ueyudiud
 */
public class ShapedCraftingRecipeHandler implements IRecipeHandler<ShapedFleRecipe>
{
	public ShapedCraftingRecipeHandler(IJeiHelpers helpers)
	{
	}
	
	@Override
	public Class<ShapedFleRecipe> getRecipeClass()
	{
		return ShapedFleRecipe.class;
	}
	
	@Override
	public String getRecipeCategoryUid()
	{
		return VanillaRecipeCategoryUid.CRAFTING;
	}
	
	@Override
	public String getRecipeCategoryUid(ShapedFleRecipe recipe)
	{
		return VanillaRecipeCategoryUid.CRAFTING;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(ShapedFleRecipe recipe)
	{
		return new ShapedCraftingRecipeWrapper(recipe);
	}
	
	@Override
	public boolean isRecipeValid(ShapedFleRecipe recipe)
	{
		if (!recipe.output.valid())
			return false;
		for (SingleInputMatch[] matches : recipe.inputs)
			for (SingleInputMatch match : matches)
				if (match != null && !match.input.valid())
					return false;
		return true;
	}
}
