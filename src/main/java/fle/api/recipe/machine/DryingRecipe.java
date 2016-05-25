package fle.api.recipe.machine;

import java.util.HashMap;

import farcore.lib.stack.AbstractStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DryingRecipe
{
	private static final HashMap<String, $Recipe> recipes = new HashMap();
	
	public static void addRecipe(String name, $Recipe recipe)
	{
		recipe.name = name;
		recipes.put(name, recipe);
	}
	
	public static $Recipe loadRecipe(NBTTagCompound nbt, String tag)
	{
		return recipes.get(nbt.getString(tag));
	}
	
	public static NBTTagCompound saveRecipe(NBTTagCompound nbt, String tag, $Recipe recipe)
	{
		if(recipe != null)
		{
			nbt.setString(tag, recipe.name);
		}
		return nbt;
	}
	
	public static $Recipe matchRecipe(ItemStack input)
	{
		if(input == null) return null;
		for($Recipe recipe : recipes.values())
		{
			if(recipe.input.contain(input))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static class $Recipe
	{
		String name;
		public final AbstractStack input;
		public final float maxTemp;
		public final float maxWatering;
		public final int tick;
		public final ItemStack output;
		
		public $Recipe(AbstractStack input, float maxT, float maxW, int tick, ItemStack output)
		{
			this.input = input;
			this.maxTemp = maxT;
			this.maxWatering = maxW;
			this.tick = tick;
			this.output = output;
		}
	}
}