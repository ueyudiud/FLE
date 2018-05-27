/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.compat.jei.ToolDisplayRecipeMap.ToolDisplayRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import nebula.base.A;
import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ToolDisplayRecipeWrapper extends BlankRecipeWrapper
{
	final ToolDisplayRecipe recipe;
	
	public ToolDisplayRecipeWrapper(ToolDisplayRecipe recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		List<List<ItemStack>> list1 = new ArrayList<>(this.recipe.tools.length + 1);
		list1.add(this.recipe.input.display());
		list1.addAll(A.argument(A.transform(this.recipe.tools, AbstractStack::display)));
		ingredients.setInputLists(ItemStack.class, getInputs());
		List<ItemStack> list2 = new ArrayList<>(A.argument(A.transform(this.recipe.outputs, AbstractStack::instance)));
		ingredients.setOutputs(ItemStack.class, list2);
	}
	
	@Override
	public List getInputs()
	{
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();
		builder.add(this.recipe.input.display());
		builder.add(A.transform(this.recipe.tools, List.class, AbstractStack::display));
		return builder.build();
	}
	
	@Override
	public List getOutputs()
	{
		return ImmutableList.copyOf(A.transform(this.recipe.outputs, AbstractStack::instance));
	}
}
