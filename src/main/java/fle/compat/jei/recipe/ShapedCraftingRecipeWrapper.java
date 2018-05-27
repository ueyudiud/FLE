/*
 * copyright 2016-2018 ueyudiud
 */
package fle.compat.jei.recipe;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fle.api.recipes.ShapedFleRecipe;
import fle.api.recipes.SingleInputMatch;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ShapedCraftingRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper
{
	private final ShapedFleRecipe recipe;
	
	public ShapedCraftingRecipeWrapper(ShapedFleRecipe recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		for (SingleInputMatch[] matches : this.recipe.inputs)
		{
			for (SingleInputMatch match : matches)
			{
				if (match.input != null)
				{
					builder.add(ImmutableList.copyOf(Lists.transform(match.input.display(), ItemStacks::valid)));
				}
				else
				{
					builder.add(ImmutableList.of());
				}
			}
		}
		ingredients.setInputLists(ItemStack.class, builder.build());
		ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
	}
	
	@Override
	public int getWidth()
	{
		return this.recipe.width;
	}
	
	@Override
	public int getHeight()
	{
		return this.recipe.height;
	}
}
