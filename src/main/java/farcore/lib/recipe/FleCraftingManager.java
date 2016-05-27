package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.List;

public class FleCraftingManager
{
//	private static final Comparator<IFleRecipe> COMPARATOR = 
//			(IFleRecipe recipe1, IFleRecipe recipe2) -> 
//	{
//		return 0;
//	};
	
	private static List<IFleRecipe> recipes = new ArrayList();
	
	public static List<IFleRecipe> getRecipes()
	{
		return recipes;
	}
	
	public static void registerRecipe(IFleRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static IFleRecipe findRecipe(ICraftingInventoryMatching inventory)
	{
		for(IFleRecipe recipe : recipes)
		{
			if(recipe.matchRecipe(inventory))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static void postInit()
	{
		
	}
}