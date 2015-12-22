package fle.core.recipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.minecraft.item.ItemStack;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.util.io.JsonHandler;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEDryingRecipe.DryingInfo;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;

public class FLEDryingRecipe extends IRecipeHandler<DryingRecipe, DryingInfo>
{
	private static FLEDryingRecipe instance = new FLEDryingRecipe();
	
	public static void init()
	{
		a(new DryingRecipe("Leaves Drying", new BaseStack(ItemFleSub.a("leaves")), 50000, ItemFleSub.a("leaves_dry")));
		a(new DryingRecipe("Ramie Fiber Drying", new BaseStack(ItemFleSub.a("ramie_fiber")), 30000, ItemFleSub.a("ramie_fiber_dry")));
		a(new DryingRecipe("Straw Drying", new BaseStack(ItemFleSub.a("straw")), 30000, ItemFleSub.a("straw_dry")));
	}
	
	public static void postInit(JsonHandler loader)
	{
		instance.reloadRecipes(loader);
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
	
	@Override
	protected DryingRecipe readFromJson(DryingInfo element)
	{
		return new DryingRecipe(element.name, element.input.toStack(), element.recipeTime, element.output.getStack());
	}
	
	public static class DryingInfo
	{
		@Expose
		public String name;
		@Expose
		public StackInfomation input;
		@Expose
		@SerializedName("time")
		public int recipeTime;
		@Expose
		public StackInfomation output;
	}
	
	public static class DryingRecipe extends MachineRecipe<DryingInfo>
	{
		public final String name;
		public ItemAbstractStack input;
		public int recipeTime;
		public ItemStack output;
		
		public DryingRecipe(String name, ItemAbstractStack input, int time, ItemStack output)
		{
			this.name = name;
			this.input = input;
			this.output = output.copy();
			recipeTime = time;
		}
		
		@Override
		protected DryingInfo makeInfo()
		{
			DryingInfo info = new DryingInfo();
			info.name = name;
			info.input = new JsonStack(input).getInfomation();
			info.recipeTime = recipeTime;
			info.output = new JsonStack(output).getInfomation();
			return info;
		}
		
		public boolean matchRecipe(ItemStack target)
		{
			if(input.equal(target))
			{
				return true;
			}
			return false;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return new DryingRecipeKey(name, input, recipeTime);
		}
	}
	
	public static class DryingRecipeKey extends RecipeKey
	{
		private String name;
		private ItemAbstractStack input;
		private ItemStack input1;
		private int tick;

		public DryingRecipeKey(String name, ItemAbstractStack aInput, int aTick)
		{
			this.name = name;
			input = aInput;
			tick = aTick;
		}
		public DryingRecipeKey(ItemStack aInput)
		{
			if(aInput != null)
				input1 = aInput.copy();
		}
		
		private boolean isStandardKey()
		{
			return name != null;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			DryingRecipeKey key = (DryingRecipeKey) keyRaw;
			if(isStandardKey())
			{
				if(key.isStandardKey())
					return input.equal(key.input);
				if(key.input1 == null) return false;
				return input.equal(key.input1);
			}
			else if(key.isStandardKey())
			{
				return key.isEqual(this);
			}
			return false;
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
				return "recipe." + name.toLowerCase();
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