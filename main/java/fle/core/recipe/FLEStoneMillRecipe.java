package fle.core.recipe;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.config.JsonLoader;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.soild.SolidStack;
import fle.api.util.ConfigInfomation;
import fle.api.util.DropInfo;
import fle.api.util.FLEConfiguration;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;

public class FLEStoneMillRecipe extends IRecipeHandler<StoneMillRecipe>
{
	private static final FLEStoneMillRecipe instance = new FLEStoneMillRecipe();
	
	public static void init()
	{
		a(new StoneMillRecipe(new ItemBaseStack(ItemFleSub.a("millet")), 200, new SolidStack(IB.millet, 20)));
		a(new StoneMillRecipe(new ItemBaseStack(Items.wheat), 200, new SolidStack(IB.wheat, 20)));
		a(new StoneMillRecipe(new ItemBaseStack(ItemFleSub.a("defatted_crushed_bone")), 200, new SolidStack(IB.Ca_P_fertilizer, 108)));
	}
	
	public static void postInit(JsonLoader loader)
	{
		instance.reloadRecipes(loader);
	}
	
	public static FLEStoneMillRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(StoneMillRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	private FLEStoneMillRecipe() { }
	
	public static class StoneMillRecipe extends MachineRecipe
	{
		private ItemAbstractStack input;
		private int tick;
		private SolidStack sOutput;
		private FluidStack fOutput;

		public StoneMillRecipe(ItemAbstractStack stack, int aTick, FluidStack output)
		{
			this(stack, aTick, null, output);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, SolidStack output)
		{
			this(stack, aTick, output, null);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, SolidStack output, FluidStack o)
		{
			input = stack;
			tick = aTick;
			if(output != null)
				sOutput = output.copy();
			if(o != null)
				fOutput = o.copy();
		}
		
		public ItemAbstractStack getInput()
		{
			return input;
		}
		
		public boolean match(ItemStack aInput)
		{
			return input.isStackEqul(aInput);
		}
		
		public SolidStack getOutput()
		{
			return sOutput == null ? null : sOutput.copy();
		}
		
		public FluidStack getFluidOutput()
		{
			return fOutput == null ? null : fOutput.copy();
		}

		@Override
		public RecipeKey getRecipeKey()
		{
			return new StoneMillRecipeKey(input, tick);
		}
	}
	
	public static class StoneMillRecipeKey extends RecipeKey
	{
		private ItemAbstractStack stack;
		private ItemStack stack1;
		int tick;
		
		public StoneMillRecipeKey(ItemAbstractStack aStack, int aTick)
		{
			stack = aStack;
			tick = aTick;
		}
		public StoneMillRecipeKey(ItemStack aStack)
		{
			if(aStack != null)
				stack1 = aStack.copy();
		}
		
		@Override
		public int hashCode()
		{
			return 1;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			StoneMillRecipeKey key = (StoneMillRecipeKey) keyRaw;
			if((key.stack == null && key.stack1 == null) || (stack1 == null && stack == null)) return false;
			if(stack != null && key.stack != null) return stack.isStackEqul(key.stack);
			if(stack != null && (key.stack1 == null || !stack.isStackEqul(key.stack1))) return false;
			if(key.stack != null && (stack1 == null || !key.stack.isStackEqul(stack1))) return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:" + stack;
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
		
		public int getRecipeTick()
		{
			return tick;
		}
	}
}