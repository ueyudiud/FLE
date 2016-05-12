package fle.api.recipe.smelting;

import java.util.HashMap;
import java.util.Map;

import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.lib.stack.AbstractStack;
import fle.api.util.TemperatureHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SmeltingRecipes
{
	private static final Map<String, SmeltingRecipe> recipes = new HashMap();
	
	public static void addSmeltingRecipe(String name, SmeltingRecipe recipe)
	{
		recipes.put(name, recipe);
		recipe.name = name;
	}
	
	public static SmeltingRecipe loadRecipe(NBTTagCompound nbt, String tag)
	{
		return !nbt.hasKey(tag) ? null : recipes.get(nbt.getString(tag));
	}
	
	public static void saveRecipe(NBTTagCompound nbt, String tag, SmeltingRecipe recipe)
	{
		if(recipe != null)
		{
			nbt.setString(tag, recipe.name);
		}
	}
	
	public static SmeltingRecipe getMatchedRecipe(ItemStack input)
	{
		for(SmeltingRecipe recipe : recipes.values())
		{
			if(recipe.matchInput(input))
			{
				return recipe;
			}
		}
		return null;
	}
	
	public static class SmeltingRecipe
	{
		private String name;
		public final AbstractStack input;
		public final int energy;
		public final int minTemp1;
		ItemStack output1;
		int minTemp2;
		ItemStack output2;

		public SmeltingRecipe(AbstractStack input, int energy, int temp1, ItemStack output1)
		{
			this(input, energy, temp1, output1, -1, null);
		}
		public SmeltingRecipe(AbstractStack input, int energy, int temp1, ItemStack output1, int temp2, ItemStack output2)
		{
			this.input = input;
			this.energy = energy;
			this.minTemp1 = temp1;
			this.minTemp2 = temp2;
			this.output1 = output1.copy();
			if(output2 != null)
			{
				this.output2 = output2.copy();
			}
		}
		
		public String name()
		{
			return name;
		}
		
		public boolean matchInput(ItemStack stack)
		{
			if(!input.contain(stack)) return false;
			if(stack.getItem() instanceof IThermalItem)
			{
				return ((IThermalItem) stack.getItem()).getTemperature(stack) >= minTemp1;
			}
			else
			{
				return TemperatureHandler.getTemperature(stack) >= minTemp1;
			}
		}
		
		public ItemStack getOutput(ItemStack stack)
		{
			float temp;
			if(stack.getItem() instanceof IThermalItem)
			{
				temp = ((IThermalItem) stack.getItem()).getTemperature(stack);
			}
			else
			{
				temp = TemperatureHandler.getTemperature(stack);
			}
			return temp >= minTemp2 ? ItemStack.copyItemStack(output2) : output1.copy();
		}
	}
}