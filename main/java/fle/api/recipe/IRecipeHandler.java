package fle.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.util.ConfigInfomation;
import fle.api.util.FLEConfiguration;
import fle.api.util.FleLog;

public abstract class IRecipeHandler<T extends MachineRecipe>
{
	private List<T> recipeCache = new ArrayList();
	protected Set<T> recipeSet = new HashSet();
	protected Map<String, RecipeKey> recipeKeys = new HashMap();
	protected Map<RecipeKey, T> recipeMap = new HashMap();
	
	public void reloadRecipes(FLEConfiguration config)
	{
		recipeSet.clear();
		recipeKeys.clear();
		recipeMap.clear();
		for(T recipe : recipeCache)
			reloadRecipe(config, recipe);
		recipeCache = null;
	}
	
	private void reloadRecipe(FLEConfiguration config, T recipe)
	{
		RecipeKey key = recipe.getRecipeKey();
		try
		{
			recipe.reloadRecipe(config.getConfigInfomation(key.getClass().getName() + "@" +key.toString()));
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		key = recipe.getRecipeKey();
		recipeMap.put(key, recipe);
		recipeKeys.put(key.toString(), key);
		recipeSet.add(recipe);
	}
	
	public boolean registerRecipe(T recipe)
	{
		recipeCache.add(recipe);
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
	
	public static abstract class MachineRecipe
	{		
		public abstract RecipeKey getRecipeKey();
		
		public abstract void reloadRecipe(ConfigInfomation ci);
	}
	
	public static abstract class RecipeKey
	{
		public abstract boolean equals(Object obj);
		
		public abstract int hashCode();
		
		public abstract String toString();
	}
}