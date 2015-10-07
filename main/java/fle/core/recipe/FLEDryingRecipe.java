package fle.core.recipe;

import net.minecraft.item.ItemStack;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.ConfigInfomation;
import fle.api.util.FLEConfiguration;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;

public class FLEDryingRecipe extends IRecipeHandler<DryingRecipe>
{
	private static FLEDryingRecipe instance = new FLEDryingRecipe();
	
	public static void init()
	{
		a(new DryingRecipe(new ItemBaseStack(ItemFleSub.a("leaves")), 50000, ItemFleSub.a("leaves_dry")));
		a(new DryingRecipe(new ItemBaseStack(ItemFleSub.a("ramie_fiber")), 30000, ItemFleSub.a("ramie_fiber_dry")));
	}
	
	public static void postInit(FLEConfiguration cfg)
	{
		instance.reloadRecipes(cfg);
	}
	
	public static void a(DryingRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static FLEDryingRecipe getInstance()
	{
		return instance;
	}
	
	private FLEDryingRecipe(){}
	
	public static class DryingRecipe extends MachineRecipe
	{
		private String name;
		public ItemAbstractStack input;
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
			if(input.isStackEqul(target))
			{
				return true;
			}
			return false;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return new DryingRecipeKey(input, recipeTime);
		}

		@Override
		public void reloadRecipe(ConfigInfomation ci)
		{
			recipeTime = ci.readInteger(0, recipeTime);
		}
	}
	
	public static class DryingRecipeKey extends RecipeKey
	{
		private ItemAbstractStack input;
		private ItemStack input1;
		private int tick;

		public DryingRecipeKey(ItemAbstractStack aInput, int aTick)
		{
			input = aInput;
			tick = aTick;
		}
		public DryingRecipeKey(ItemStack aInput)
		{
			if(aInput != null)
				input1 = aInput.copy();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof DryingRecipeKey)) return false;
			DryingRecipeKey key = (DryingRecipeKey) obj;
			if((input == null && input1 == null) || (key.input1 == null && key.input == null)) return false;
			if(key.input != null && (input1 == null || !key.input.isStackEqul(input1))) return false;
			if(input != null && (key.input1 == null || input.isStackEqul(key.input1))) return false;
			return true;
		}
		
		@Override
		public int hashCode()
		{
			return 43859242;
		}
		
		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:" + input.toString();
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
		
		public int getDryingTick() 
		{
			return tick;
		}
	}
}