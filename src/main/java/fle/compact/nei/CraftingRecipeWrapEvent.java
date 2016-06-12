package fle.compact.nei;

import cpw.mods.fml.common.eventhandler.Event;
import farcore.lib.nei.FarTemplateRecipeHandler.BaseCachedRecipe;
import farcore.lib.recipe.IFleRecipe;

public class CraftingRecipeWrapEvent extends Event
{
	public final IFleRecipe recipe;
	public BaseCachedRecipe neiDisplay;
	
	public CraftingRecipeWrapEvent(IFleRecipe recipe)
	{
		this.recipe = recipe;
	}
}
