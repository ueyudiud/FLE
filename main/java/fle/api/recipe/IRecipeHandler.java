package fle.api.recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.util.FleLog;

public abstract class IRecipeHandler<T extends MachineRecipe>
{
	protected Set<T> recipeSet = new HashSet();
	protected Map<String, RecipeKey> recipeKeys = new HashMap();
	protected Map<RecipeKey, T> recipeMap = new HashMap();
	
	public boolean registerRecipe(T recipe)
	{
		if(recipeMap.containsKey(recipe.getRecipeKey()) || recipeSet.contains(recipe) || recipeKeys.containsKey(recipe.getRecipeKey().toString()))
		{
			FleLog.getLogger().warn("Some mod register a same recipe, name " + recipe.toString());
			return false;
		}
		recipeMap.put(recipe.getRecipeKey(), recipe);
		recipeKeys.put(recipe.getRecipeKey().toString(), recipe.getRecipeKey());
		recipeSet.add(recipe);
		return true;
	}
	
	public T getRecipe(RecipeKey key)
	{
		return recipeMap.containsKey(key) ? recipeMap.get(key) : null;
	}
	
	public RecipeKey getRecipeKey(String str)
	{
		return recipeKeys.containsKey(str) ? recipeKeys.get(str) : null;
	}
	
	public Map<RecipeKey, T> getRecipeMap()
	{
		return recipeMap;
	}
	
	public Set<T> getRecipes()
	{
		return recipeSet;
	}
	
	public static interface MachineRecipe
	{		
		public RecipeKey getRecipeKey();
	}
	
	public static interface RecipeKey
	{
		@Override
		public abstract boolean equals(Object obj);
		
		@Override
		public abstract int hashCode();
		
		@Override
		public abstract String toString();
	}
}