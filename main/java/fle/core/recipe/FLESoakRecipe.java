package fle.core.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ItemOreStack;
import fle.api.util.ConfigInfomation;
import fle.api.util.FLEConfiguration;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLESoakRecipe.SoakRecipe;

public class FLESoakRecipe extends IRecipeHandler<SoakRecipe>
{
	private static final FLESoakRecipe instance = new FLESoakRecipe();

	public static void init()
	{
		a(new SoakRecipe(new FluidStack(IB.plantOil, 100), new ItemOreStack("plankWood"), 400, ItemFleSub.a("rotproof_plank")));
		a(new SoakRecipe(new FluidStack(IB.plantOil, 100), new ItemOreStack("stickWood"), 100, ItemFleSub.a("rotproof_stick")));
	}
	
	public static void postInit(FLEConfiguration cfg)
	{
		instance.reloadRecipes(cfg);
	}
	
	public static FLESoakRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(SoakRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	private FLESoakRecipe(){}
	
	public static class SoakRecipe extends MachineRecipe
	{
		private FluidStack fluid;
		private ItemAbstractStack stack;
		private final int defaultTick;
		public int tick;
		private ItemStack output;
		
		public SoakRecipe(FluidStack aFluid, ItemAbstractStack aStack, int recipeTick, ItemStack aOutput)
		{
			fluid = aFluid.copy();
			stack = aStack;
			defaultTick = tick = recipeTick;
			output = aOutput.copy();
		}
		
		@Override
		public SoakRecipeKey getRecipeKey()
		{
			return new SoakRecipeKey(fluid, tick, stack, output.stackSize, output.getMaxStackSize());
		}

		@Override
		public void reloadRecipe(ConfigInfomation ci)
		{
			tick = ci.readInteger(0, defaultTick);
		}
		
		public ItemStack getOutput(ItemStack input)
		{
			ItemStack output = this.output.copy();
			output.stackSize *= input.stackSize;
			return output;
		}
	}
	
	public static class SoakRecipeKey extends RecipeKey
	{
		private ItemStack item;
		private FluidStack fluid;
		private ItemAbstractStack key1;
		private FluidStack key2;
		public int tick;
		private int outputSize;
		private int maxSize;
		
		public SoakRecipeKey(FluidStack aFluid, int aTick, ItemAbstractStack stack, int size, int mSize)
		{
			key2 = aFluid.copy();
			key1 = stack;
			tick = aTick;
			outputSize = size;
			maxSize = mSize;
		}
		public SoakRecipeKey(FluidStack aFluid, ItemStack stack)
		{
			item = stack;
			fluid = aFluid;
		}
		
		public int getRecipeTick(ItemStack target)
		{
			return tick;
		}
		
		public int getFluidUse(ItemStack target)
		{
			return key2.amount * (target == null ? 0 : target.stackSize);
		}
		
		private boolean isStandardKey()
		{
			return key1 != null && key2 != null;
		}

		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			SoakRecipeKey key = (SoakRecipeKey) keyRaw;
			if(isStandardKey() && key.isStandardKey())
			{
				return FluidStack.areFluidStackTagsEqual(key2, key.key2) && key1.isStackEqul(key.key1);
			}
			if(isStandardKey())
			{
				if(key.item == null) return false;
				if(!key1.isStackEqul(key.item)) return false;
				if(key.item.stackSize * outputSize > maxSize) return false;
				if(key.fluid == null) return false;
				FluidStack fluid = key2.copy();
				fluid.amount *= key.item.stackSize;
				return key.fluid.containsFluid(fluid);
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
			return 241851951;
		}

		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:item." + key1.toString() + ".fluid." + key2.toString();
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
		
		public boolean match(FluidStack fluid, ItemStack input)
		{
			if(input == null || fluid == null) return false;
			FluidStack fluid1 = key2.copy();
			fluid1.amount *= input.stackSize;
			return fluid.containsFluid(fluid1) && key1.isStackEqul(input);
		}
	}
}