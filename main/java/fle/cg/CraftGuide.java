package fle.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.ItemAbstractStack;

public final class CraftGuide
{
	public static final CraftGuide instance = new CraftGuide();
	
	Map<RecipesTab, List<RecipeHandler>> recipeList = new HashMap();
	
	private CraftGuide()
	{
		
	}
	
	public void registerRecipe(RecipesTab tab, RecipeHandler handler)
	{
		if(!recipeList.containsKey(tab)) RecipesTab.registerNewTab(tab);
		recipeList.get(tab).add(handler);
	}
	
	public RecipeHandler[] getAllRecipes(RecipesTab tab)
	{
		if(!recipeList.containsKey(tab)) RecipesTab.registerNewTab(tab);
		return recipeList.get(tab).toArray(new RecipeHandler[recipeList.size()]);
	}
	
	public RecipeHandler[] getRecipes(RecipesTab tab, ItemAbstractStack stack)
	{
		if(!recipeList.containsKey(tab)) RecipesTab.registerNewTab(tab);
		List<RecipeHandler> ret = new ArrayList();
		for(RecipeHandler handler : recipeList.get(tab))
		{
			for(ItemStack tStack : stack.toArray())
			{
				if(handler.match(tStack))
					ret.add(handler);
			}
		}
		return ret.toArray(new RecipeHandler[ret.size()]);
	}
	
	public RecipeHandler[] getRecipes(RecipesTab tab, FluidStack stack)
	{
		if(!recipeList.containsKey(tab)) RecipesTab.registerNewTab(tab);
		List<RecipeHandler> ret = new ArrayList();
		for(RecipeHandler handler : recipeList.get(tab))
		{
			if(handler.match(stack))
				ret.add(handler);
		}
		return ret.toArray(new RecipeHandler[ret.size()]);
	}
}