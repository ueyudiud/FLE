package fle.api.recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import fle.api.recipe.AbstractRecipe.GetRecipeMap;
import fle.api.recipe.AbstractRecipe.GetRecipeName;
import fle.api.recipe.AbstractRecipe.OnInput;
import fle.api.recipe.AbstractRecipe.OnOutput;
import fle.api.recipe.AbstractRecipe.RecipeMatch;

public class AbstractRecipeManager
{
	private static Map<String, List<RecipeInfo>> map = new HashMap();

	public static void registerAbstractRecipe(Class<?> clazz)
	{
		registerAbstractRecipe(Loader.instance().activeModContainer().getModId(), clazz);
	}
	public static void registerAbstractRecipe(String modid, Class<?> clazz)
	{
		try
		{
			if(!clazz.isAnnotationPresent(AbstractRecipe.class)) 
				throw new NullPointerException();
			AbstractRecipe recipeInfo = clazz.getAnnotation(AbstractRecipe.class);
			String recipeName;
			if(modid != null)
				recipeName = modid + ":" + recipeInfo.recipeName();
			else
				recipeName = recipeInfo.recipeName();
			boolean requireCraftingTime = recipeInfo.requireCraftingTime();
			Method[] methods = clazz.getMethods();
			
			Method getRecipe = null;
			Method onInput = null;
			Method onOutput = null;
			Method getRecipeMap = null;
			Method getRecipeName = null;
	        for(Method method : methods)
	        {
	        	if(method.isAnnotationPresent(RecipeMatch.class))
	        	{
	        		if(getRecipe != null) throw new RuntimeException("FLE : find more than one interface!");
	        		if((method.getModifiers() & 8) == 0) throw new RuntimeException("FLE : recipe is not static method!");
	        		getRecipe = method;
	        	}
	        	if(method.isAnnotationPresent(OnInput.class))
	        	{
	        		if(onInput != null) throw new RuntimeException("FLE : find more than one interface!");
	        		if((method.getModifiers() & 8) == 0) throw new RuntimeException("FLE : recipe is not static method!");
	        		onInput = method;
	        	}
	        	if(method.isAnnotationPresent(OnOutput.class))
	        	{
	        		if(onOutput != null) throw new RuntimeException("FLE : find more than one interface!");
	        		if((method.getModifiers() & 8) == 0) throw new RuntimeException("FLE : recipe is not static method!");
	        		onOutput = method;
	        	}
	        	if(method.isAnnotationPresent(GetRecipeMap.class))
	        	{
	        		if(getRecipeMap != null) throw new RuntimeException("FLE : find more than one interface!");
	        		if((method.getModifiers() & 8) == 0) throw new RuntimeException("FLE : recipe is not static method!");
	        		if(method.getReturnType().isAssignableFrom(Iterable.class)) throw new RuntimeException("FLE : recipe return class is not iterable!");
	        		getRecipeMap = method;
	        	}
	        	if(method.isAnnotationPresent(GetRecipeName.class))
	        	{
	        		if(getRecipeName != null) throw new RuntimeException("FLE : find more than one interface!");
	        		if((method.getModifiers() & 8) == 0) throw new RuntimeException("FLE : recipe is not static method!");
	        		getRecipeName = method;
	        	}
	        }
	        if(!map.containsKey(recipeName))
	        {
	        	map.put(recipeName, new ArrayList());
	        }
	        map.get(recipeName).add(new RecipeInfo(getRecipe, onInput, onOutput, getRecipeMap, getRecipeName));
		}
		catch(Throwable e)
		{
			new RuntimeException("FLE some mod fail to register recipe " + clazz.toString() + ", pleace report "
					+ "this bug.", e).printStackTrace();
		}
	}
	
	public static Object getRecipe(String recipeID, Object...objects)
	{
		if(!map.containsKey(recipeID)) return null;
		for(RecipeInfo info : map.get(recipeID))
		{
			try
			{
				return info.getRecipe.invoke(null, objects);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static RecipeInfo a(String recipeID, Object...objects)
	{
		if(!map.containsKey(recipeID)) return null;
		for(RecipeInfo info : map.get(recipeID))
		{
			try
			{
				if(info.getRecipe.getReturnType() == Boolean.class)
				{
					if((Boolean) info.getRecipe.invoke(null, objects))
						return info;
				}
				else
				{
					if(info.getRecipe.invoke(null, objects) != null)
						return info;
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean matchRecipe(String recipeID, Object...objects) 
	{
		return a(recipeID, objects) != null;
	}
	
	public static boolean onInput(String recipeID, Object...objects)
	{
		if(!map.containsKey(recipeID)) return false;
		for(RecipeInfo info : map.get(recipeID))
		{
			try
			{
				info.onInput.invoke(null, objects);
				return true;
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean onOutput(String recipeID, Object...objects)
	{
		if(!map.containsKey(recipeID)) return false;
		for(RecipeInfo info : map.get(recipeID))
		{
			try
			{
				info.onOutput.invoke(null, objects);
				return true;
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static Iterable<?> getRecipeMap(String recipeID, Object...objects)
	{
		if(!map.containsKey(recipeID)) return null;
		for(RecipeInfo info : map.get(recipeID))
		{
			try
			{
				if(info.getRecipeMap != null)
				{
					return (Iterable) info.getRecipeMap.invoke(null, objects);
				}
				else
				{
					return null;
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static class RecipeInfo
	{
		Method getRecipe;
		Method onInput;
		Method onOutput;
		Method getRecipeMap;
		Method getRecipeName;
		
		private RecipeInfo(Method f1, Method f2, Method f3, Method f4, Method f5)
		{
			getRecipe = f1;
			onInput = f2;
			onOutput = f3;
			getRecipeMap = f4;
			getRecipeName = f5;
		}
	}
}