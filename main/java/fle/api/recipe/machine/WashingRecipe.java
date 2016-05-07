package fle.api.recipe.machine;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.recipe.DropHandler;
import farcore.lib.stack.AbstractStack;
import farcore.util.U.Mod;
import net.minecraft.item.ItemStack;

public class WashingRecipe
{
	private static Map<String, $Recipe> recipeMap = new HashMap();
	
	public static void addRecipe(String name, AbstractStack stack, DropHandler output)
	{
		$Recipe recipe = new $Recipe();
		if(name.indexOf(':') != -1)
		{
			recipe.name = name;
		}
		else
		{
			recipe.name = Mod.getActiveModID() + ":" + name;
		}
		recipe.input = stack;
		recipe.output = output;
		recipeMap.put(name, recipe);
	}
	
	public static $Recipe matchRecipe(ItemStack input)
	{
		for($Recipe recipe : recipeMap.values())
		{
			if(recipe.input.contain(input))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static $Recipe getRecipe(String name)
	{
		return recipeMap.get(name);
	}
	
	public static class $Recipe
	{
		String name;
		public AbstractStack input;
		public DropHandler output;
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}