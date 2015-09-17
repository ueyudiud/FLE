package fle.core.recipe;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.DropInfo;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;

public class FLEStoneMillRecipe extends IRecipeHandler<StoneMillRecipe>
{
	private static final FLEStoneMillRecipe instance = new FLEStoneMillRecipe();
	
	static
	{
		a(new StoneMillRecipe(new ItemBaseStack(ItemFleSub.a("millet")), 200, ItemFleFood.a("groats_millet_wholemeal")));
		a(new StoneMillRecipe(new ItemBaseStack(Items.wheat), 200, ItemFleFood.a("groats_wheat_wholemeal")));
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
	
	public static class StoneMillRecipe implements MachineRecipe
	{
		private ItemAbstractStack input;
		private int tick;
		private DropInfo iOutputs;
		private FluidStack fOutput;

		public StoneMillRecipe(ItemAbstractStack stack, int aTick, ItemStack output)
		{
			this(stack, aTick, new DropInfo(output), null);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, ItemStack output, FluidStack o)
		{
			this(stack, aTick, new DropInfo(output), o);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, Map<ItemStack, Integer> output)
		{
			this(stack, aTick, new DropInfo(output), null);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, Map<ItemStack, Integer> output, FluidStack o)
		{
			this(stack, aTick, new DropInfo(output), o);
		}
		public StoneMillRecipe(ItemAbstractStack stack, int aTick, DropInfo wh, FluidStack o)
		{
			input = stack;
			tick = aTick;
			iOutputs = wh;
			if(fOutput != null)
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
		
		public ItemStack[] getOutput()
		{
			ArrayList<ItemStack> output = iOutputs.getDrops();
			return output.toArray(new ItemStack[output.size()]);
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
	
	public static class StoneMillRecipeKey implements RecipeKey
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
		public boolean equals(Object obj)
		{
			if(!(obj instanceof StoneMillRecipeKey)) return false;
			StoneMillRecipeKey key = (StoneMillRecipeKey) obj;
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