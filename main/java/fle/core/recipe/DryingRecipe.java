package fle.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemSub;

public class DryingRecipe 
{
	private static Map<String, DryingRecipe> map = new HashMap();
	private static List<DryingRecipe> list = new ArrayList();
	
	static
	{
		registerRecipe(new DryingRecipe(new ItemBaseStack(ItemFleSub.a("leaves")), 50000, ItemFleSub.a("leaves_dry")));
		registerRecipe(new DryingRecipe(new ItemBaseStack(ItemFleSub.a("ramie_fiber")), 30000, ItemFleSub.a("ramie_fiber_dry")));
	}
	public static void registerRecipe(DryingRecipe recipe)
	{
		list.add(recipe);
		map.put(recipe.name, recipe);
	}
	
	public static int getRecipeTime(String recipe)
	{
		return !map.containsKey(recipe) ? -1 : 
			map.get(recipe).recipeTime;
	}
	
	public static ItemStack getRecipeResult(String recipe)
	{
		return !map.containsKey(recipe) ? null : map.get(recipe).output.copy();
	}
	
	public static String canDrying(ItemStack input) 
	{
		for(DryingRecipe recipe : list)
		{
			if(recipe.matchRecipe(input))
			{
				return recipe.name;
			}
		}
		return null;
	}
	
	private String name;
	private ItemAbstractStack input;
	public int recipeTime;
	public ItemStack output;
	public DryingRecipe(ItemAbstractStack input, int time, ItemStack output)
	{
		this.input = input;
		this.output = output.copy();
		this.recipeTime = time;
		name = "recipe.input:" + input.toString() + ".output:" + output.toString();
	}
	
	public boolean matchRecipe(ItemStack target)
	{
		if(this.input.isStackEqul(target))
		{
			return true;
		}
		return false;
	}
}