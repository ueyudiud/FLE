/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.compat.jei;

import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import fle.api.recipes.instance.FlamableRecipes;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.ModCompator.ICompatible;

/**
 * @author ueyudiud
 */
public class JEIRecipes implements ICompatible
{
	@Override
	public void call(String phase) throws Exception
	{
		switch (phase)
		{
		case "ar" ://add_recipe
			for (AbstractStack stack : FlamableRecipes.getFlamables())
			{
				ToolDisplayRecipeMap.addToolDisplayRecipe(stack,
						new AbstractStack[] { EnumToolTypes.FIRESTARTER.stack() },
						new AbstractStack[] { new BaseStack(EnumBlock.fire.block) });
			}
			break;
		}
	}
}
