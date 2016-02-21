package flapi.recipe.machine;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.Loader;
import flapi.recipe.machine.RecipeHandler.Recipe;
import flapi.recipe.machine.RecipeHandler.RecipeInfo;
import flapi.util.FleLog;
import net.minecraft.nbt.NBTTagCompound;

/**
 * The new recipe handler.
 * @author ueyudiud
 *
 * @param <R> The recipe.
 * @param <I> The input.
 * @param <C> The container.
 * @param <A> The recipe infomation.
 */
public class RecipeHandler<R extends Recipe<C, A>, C, A extends RecipeInfo>
{
	/**
	 * The handler name.
	 */
	public final String name;
	/**
	 * The registered name collection.
	 * Can't save two same name in it.
	 */
	protected List<String> registeredNameList = new ArrayList();
	/**
	 * The cache of list.
	 * Cached during loading recipes.
	 * And get cache during reloading and read or save to json.
	 */
	//protected List<R> recipeCache = new ArrayList();
	/**
	 * The recipes map.
	 */
	protected Map<String, R> recipes = new HashMap();
	
	public RecipeHandler(String handlerName)
	{
		name = handlerName;
	}
	
	/**
	 * Register a new recipe, info: the recipe key can't be similar from other recipes,
	 * because it will cause more than one recipe will be return by handler.
	 * @param recipe The new recipe.
	 * @return if recipe has successful been registered.
	 */
	public void registerRecipe(R recipe)
	{
		if(registeredNameList.contains(recipe.getRecipeName()))
		{
			FleLog.getLogger().warn("Some mod register a recipe with same name " + recipe.getRecipeName());
			recipe.name = getFreeName(recipe.getRecipeName());
		}
		//if(recipeCache != null)
		//{
			//recipeCache.add(recipe);
		//}
		//else
		{
			//FleLog.getLogger().warn("Some mod register a recipe after reload recipes, please check your mods. Name " + recipe.toString());
		}
		recipes.put(recipe.getRecipeName(), recipe);
		registeredNameList.add(recipe.getRecipeName());
	}
	
	private String getFreeName(String name)
	{
		int i = 1;
		while(registeredNameList.contains(name + "|" + i++));
		return name + "|" + i;
	}
	
	public boolean output(A info, C container)
	{
		return info.recipe.output(info, container);
	}
	
	/**
	 * Match recipe and input recipe.
	 * @param input The input key.
	 * @param container The input container.
	 * @return The recipe matched and created an information, return {@code null} means no recipe matched.
	 */
	public A matchAndInput(C container)
	{
		R r = match(container);
		if(r != null)
		{
			return r.input(container);
		}
		return null;
	}
	
	/**
	 * Match recipe.
	 * @param input
	 * @return The recipe matched.
	 */
	public R match(C input)
	{
		for(R recipe : recipes.values())
		{
			if(recipe.match(input)) return recipe;
		}
		return null;
	}
	
	/**
	 * Match recipe during output.
	 * @param input
	 * @return The recipe matched.
	 */
	public boolean match(A info, C container)
	{
		return info.recipe.match(container, info);
	}
	
	/**
	 * Get recipe from name.
	 * Save recipe by name when read from NBT, and get recipe by this method.
	 * @param name
	 * @return The recipe with this name.
	 */
	public R get(String name)
	{
		return recipes.get(name);
	}
	
	/**
	 * Get all registered recipes.
	 * @return The list of recipes.
	 */
	public List<R> getRecipes()
	{
		return new ArrayList(recipes.values());
	}
	
	/**
	 * Remove an recipe by name.
	 * @param name
	 * @return
	 */
	public R removeRecipe(String name)
	{
		if(registeredNameList.contains(name))
		{
			R ret = recipes.remove(name);
			registeredNameList.remove(name);
			return ret;
		}
		return null;
	}
	
	/**
	 * Remove an recipe collection by collection name.
	 * @param name
	 * @return
	 */
	public List<R> removeRecipes(String name)
	{
		List<R> ret = new ArrayList();
		for(R recipe : new ArrayList<R>(recipes.values()))
		{
			if(name.equals(recipe.group))
			{
				registeredNameList.remove(recipe.name);
				recipes.remove(recipe.name);
				ret.add(recipe);
			}
		}
		return ret;
	}
	
	public static abstract class Recipe<C, A extends RecipeInfo>
	{
		String group;
		String name;

		public Recipe(String group, String name)
		{
			this.group = group;
			this.name = name;
		}
		public Recipe(String name)
		{
			this(Loader.instance().activeModContainer() == null ?
					"default" :
					Loader.instance().activeModContainer().getModId(), name);
		}

		/**
		 * Get real name of recipe, may cause the same name in resisted list.
		 * @return
		 */
		public final String getRecipeName()
		{
			return group + ":" + name;
		}
		
		public abstract boolean match(C input);
		
		public abstract A input(C container);
		
		public abstract boolean match(C container, A info);
		
		public abstract boolean output(A info, C container);
		
		public abstract A loadFromNBT(NBTTagCompound nbt);
		
		public abstract NBTTagCompound saveToNBT(A info, NBTTagCompound nbt);
	}
	
	public static abstract class RecipeInfo<R extends Recipe>
	{
		protected R recipe;
		
		public String getRecipeName()
		{
			return recipe.getRecipeName();
		}
		
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			return recipe.saveToNBT(this, nbt);
		}
	}
}