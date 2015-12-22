package flapi.recipe;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.util.FleLog;
import flapi.util.io.IJsonLoader;
import flapi.util.io.JsonHandler;

public abstract class IRecipeHandler<T extends MachineRecipe<E>, E> implements IJsonLoader
{
	private List<T> recipeCache = new ArrayList();
	protected Set<T> recipeSet = new HashSet();
	protected Map<String, RecipeKey> recipeKeys = new HashMap();
	protected Map<RecipeKey, T> recipeMap = new HashMap();
	protected Class<E> saveRecipeType;
	
	public IRecipeHandler()
	{
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		saveRecipeType = (Class) type.getActualTypeArguments()[1];
	}
	
	@Override
	public final Class<?> getSaveClass()
	{
		return saveRecipeType;
	}
	
	public void reloadRecipes(JsonHandler handler)
	{
		if(handler.read(this))
		{
			FleLog.getLogger().info("Detected the recipes from json.");
			for(T t : recipeCache)
				reloadRecipe(t);
		}
		else
		{
			handler.register(getSaveRecipes(recipeCache));
			for(T t : recipeCache)
				reloadRecipe(t);
			handler.write(this);
		}
	}

	/**
	 * Register a new recipe, info: the recipe key can't be similar from other recipes,
	 * because it will cause more than one recipe will be return by handler.
	 * @param recipe The new recipe.
	 * @return if recipe has successful been registered.
	 */
	public boolean registerRecipe(T recipe)
	{
		if(recipeCache == null)
		{
			FleLog.getLogger().error("Some mod register a recipe after reload recipes, please check your mods. Name " + recipe.toString());
			return false;
		}
		recipeCache.add(recipe);
		return true;
	}
	
	boolean reloadRecipe(T recipe)
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
	
	/**
	 * Get recipe with recipeKey, you can use this to find a recipe with unknown recipe
	 * key with tile information to find recipe can use, or find same recipe with NBT save.
	 * @see MachineRecipe
	 * @param key The key of recipe.
	 * @return The recipe.
	 */
	public T getRecipe(RecipeKey key)
	{
		return recipeMap.containsKey(key) ? recipeMap.get(key) : null;
	}
	
	/**
	 * Get recipe by standard name of recipe. Use <code> key.toString </code> to find key
	 * of recipe.
	 * @param str The recipe key name.
	 * @return the key of recipe.
	 */
	public RecipeKey getRecipeKey(String str)
	{
		return recipeKeys.containsKey(str) ? recipeKeys.get(str) : null;
	}
	
	/**
	 * Get all recipe with a map.
	 * @return
	 */
	public Map<RecipeKey, T> getRecipeMap()
	{
		return recipeMap;
	}
	
	/**
	 * Get all recipe with a collection.
	 * @return
	 */
	public Set<T> getRecipes()
	{
		return recipeSet;
	}
	
	protected List<E> getSaveRecipes(List<T> cache)
	{
		List<E> ret = new ArrayList();
		for(T recipe : cache)
		{
			ret.add(recipe.makeInfo());
		}
		return ret;
	}
	
	protected abstract T readFromJson(E element);

	@Override
	public void readJson(Gson gson, List<String> list) throws IOException
	{
		recipeCache.clear();
		for(String json : list)
		{
			E info = gson.fromJson(json, saveRecipeType);
			registerRecipe(readFromJson(info));
		}
	}

	@Override
	public void writeJson(Gson gson, List<String> list) throws IOException
	{
		;
	}
	
	/**
	 * The standard recipe.<br>
	 * Use <code> recipe.getRecipeKey() </code> to get a key of recipe,
	 * and tile correct key. Saving name of key to NBT. <br>
	 * <code>
	 * nbt.setString("Recipe", key.toString());
	 * </code> 
	 * Get output information from here and match input from recipe key.
	 * @author ueyudiud
	 */
	public static abstract class MachineRecipe<E>
	{
		/**
		 * Get key of recipe.
		 * @return
		 */
		public abstract RecipeKey getRecipeKey();
		
		protected abstract E makeInfo();
	}
	
	/**
	 * 
	 * @author ueyudiud
	 */
	public static abstract class RecipeKey
	{
		public boolean equals(Object obj)
		{
			return getClass().isInstance(obj) ? isEqual(getClass().cast(obj)) : false;
		}
		
		protected abstract boolean isEqual(RecipeKey keyRaw);
		
		public abstract int hashCode();
		
		public abstract String toString();
	}
}