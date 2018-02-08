/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.compat.jei.recipe;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fle.api.recipes.ShapelessFleRecipe;
import fle.api.recipes.SingleInputMatch;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.plugins.vanilla.crafting.AbstractShapelessRecipeWrapper;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ShapelessCraftingRecipeWrapper extends AbstractShapelessRecipeWrapper
{
	private final ShapelessFleRecipe recipe;
	
	public ShapelessCraftingRecipeWrapper(ShapelessFleRecipe recipe, IGuiHelper guiHelper)
	{
		super(guiHelper);
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		for (SingleInputMatch match : this.recipe.inputs)
		{
			builder.add(ImmutableList.copyOf(Lists.transform(match.input.display(), ItemStacks::valid)));
		}
		ingredients.setInputLists(ItemStack.class, builder.build());
		ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
	}
	
	@Override
	public List getInputs()
	{
		return this.recipe.inputs;
	}
	
	@Override
	public List getOutputs()
	{
		return ImmutableList.of(this.recipe.output);
	}
}
