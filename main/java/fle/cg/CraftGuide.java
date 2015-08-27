package fle.cg;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.ItemAbstractStack;

public final class CraftGuide
{
	public static final CraftGuide instance = new CraftGuide();
	
	private List<RecipeHandler> recipeList = new ArrayList();
	
	private CraftGuide()
	{
		
	}
	
	public void registerRecipe(RecipeHandler handler)
	{
		recipeList.add(handler);
	}
	
	public RecipeHandler[] getAllRecipes()
	{
		return recipeList.toArray(new RecipeHandler[recipeList.size()]);
	}
	
	public RecipeHandler[] getRecipes(ItemAbstractStack stack)
	{
		List<RecipeHandler> ret = new ArrayList();
		for(RecipeHandler handler : recipeList)
		{
			for(ItemStack tStack : stack.toArray())
			{
				if(handler.match(tStack))
					ret.add(handler);
			}
		}
		return ret.toArray(new RecipeHandler[ret.size()]);
	}
	
	public RecipeHandler[] getRecipes(FluidStack stack)
	{
		List<RecipeHandler> ret = new ArrayList();
		for(RecipeHandler handler : recipeList)
		{
			if(handler.match(stack))
				ret.add(handler);
		}
		return ret.toArray(new RecipeHandler[ret.size()]);
	}
}