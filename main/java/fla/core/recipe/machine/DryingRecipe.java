package fla.core.recipe.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import fla.api.recipe.IItemChecker;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.core.item.ItemSub;

public class DryingRecipe 
{
	private static Map<String, DryingRecipe> map = new HashMap();
	private static List<DryingRecipe> list = new ArrayList();
	
	static
	{
		registerRecipe(new DryingRecipe(new ItemChecker(ItemSub.a("leaves")), 5000, ItemSub.a("leaves_dry")));
		registerRecipe(new DryingRecipe(new ItemChecker(ItemSub.a("ramie_fiber")), 3000, ItemSub.a("ramie_fiber_dry")));
	}
	public static void registerRecipe(DryingRecipe recipe)
	{
		list.add(recipe);
		map.put(recipe.name, recipe);
	}
	
	public static int getRecipeTime(String recipe)
	{
		return recipe == null ? 200 : 
			map.get(recipe).recipeTime;
	}
	
	public static ItemStack getRecipeResult(String recipe)
	{
		return map.get(recipe).output.copy();
	}
	
	public static String canDrying(ItemStack input) 
	{
		for(DryingRecipe recipe : list)
		{
			if(recipe.input.match(input))
			{
				return recipe.name;
			}
		}
		return null;
	}
	
	private String name;
	private IItemChecker input;
	public int recipeTime;
	public ItemStack output;
	public DryingRecipe(IItemChecker input, int time, ItemStack output)
	{
		this.input = input;
		this.output = output.copy();
		this.recipeTime = time;
		name = "recipe.input:" + input.toString() + ".output:" + output.toString();
	}
	
	public boolean matchRecipe(ItemStack target)
	{
		if(this.input.match(target))
		{
			return true;
		}
		return false;
	}
}