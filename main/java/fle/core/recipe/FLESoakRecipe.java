package fle.core.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;
import flapi.util.io.JsonLoader;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLESoakRecipe.SoakRecipe;

public class FLESoakRecipe extends IRecipeHandler<SoakRecipe>
{
	private static final FLESoakRecipe instance = new FLESoakRecipe();

	public static void init()
	{
		a(new SoakRecipe(new FluidStack(IB.plantOil, 100), new OreStack("plankWood"), 400, ItemFleSub.a("rotproof_plank")));
		a(new SoakRecipe(new FluidStack(IB.plantOil, 100), new OreStack("stickWood"), 100, ItemFleSub.a("rotproof_stick")));
		a(new SoakRecipe(new FluidStack(FluidRegistry.WATER, 100), new BaseStack(ItemFleFood.a("groats_wheat")), 2000, ItemFleSub.a("aspergillus")));
	}
	
	public static void postInit(JsonLoader loader)
	{
		instance.reloadRecipes(loader);
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
		public int tick;
		private ItemStack output;
		
		public SoakRecipe(FluidStack aFluid, ItemAbstractStack aStack, int recipeTick, ItemStack aOutput)
		{
			fluid = aFluid.copy();
			stack = aStack;
			tick = recipeTick;
			output = aOutput.copy();
		}
		
		@Override
		public SoakRecipeKey getRecipeKey()
		{
			return new SoakRecipeKey(fluid, tick, stack, output.stackSize, output.getMaxStackSize());
		}
		
		public ItemStack getOutput(ItemStack input)
		{
			ItemStack output = this.output.copy();
			output.stackSize *= input.stackSize;
			return output;
		}

		public FluidStack getFluidInput()
		{
			return fluid;
		}

		public ItemAbstractStack getItemInput()
		{
			return stack;
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
				return FluidStack.areFluidStackTagsEqual(key2, key.key2) && key1.equal(key.key1);
			}
			if(isStandardKey())
			{
				if(key.item == null) return false;
				if(!key1.equal(key.item)) return false;
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
			return fluid.containsFluid(fluid1) && key1.equal(input);
		}
	}
}