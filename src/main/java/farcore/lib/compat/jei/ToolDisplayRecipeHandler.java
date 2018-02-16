/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import farcore.lib.compat.jei.ToolDisplayRecipeMap.ToolDisplayRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import nebula.base.A;
import nebula.common.stack.AbstractStack;

/**
 * @author ueyudiud
 */
public class ToolDisplayRecipeHandler implements IRecipeHandler<ToolDisplayRecipe>
{
	@Override
	public Class<ToolDisplayRecipe> getRecipeClass()
	{
		return ToolDisplayRecipe.class;
	}
	
	@Override
	public String getRecipeCategoryUid()
	{
		return "farcore.tool";
	}
	
	@Override
	public String getRecipeCategoryUid(ToolDisplayRecipe recipe)
	{
		return "farcore.tool";
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(ToolDisplayRecipe recipe)
	{
		return new ToolDisplayRecipeWrapper(recipe);
	}
	
	@Override
	public boolean isRecipeValid(ToolDisplayRecipe recipe)
	{
		return recipe.input.valid() && A.and(recipe.outputs, AbstractStack::valid) && A.and(recipe.tools, AbstractStack::valid);
	}
	
}
