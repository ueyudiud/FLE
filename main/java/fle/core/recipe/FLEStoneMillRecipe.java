package fle.core.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import flapi.fluid.FluidJsonStack;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.solid.SolidJsonStack;
import flapi.solid.SolidStack;
import flapi.util.io.JsonHandler;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillInfo;
import fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe;

public class FLEStoneMillRecipe extends IRecipeHandler<StoneMillRecipe, StoneMillInfo>
{
	private static final FLEStoneMillRecipe instance = new FLEStoneMillRecipe();
	
	public static void init()
	{
		a(new StoneMillRecipe(new BaseStack(ItemFleSub.a("millet")), 200, new SolidStack(IB.millet, 20)));
		a(new StoneMillRecipe(new BaseStack(Items.wheat), 200, new SolidStack(IB.wheat, 20)));
		a(new StoneMillRecipe(new BaseStack(ItemFleSub.a("defatted_crushed_bone")), 200, new SolidStack(IB.Ca_P_fertilizer, 108)));
	}
	
	public static void postInit(JsonHandler loader)
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
	
	@Override
	protected StoneMillRecipe readFromJson(StoneMillInfo element)
	{
		return element.fOutput != null && element != null ?
				new StoneMillRecipe(element.input.toStack(), element.tick, element.sOutput.getSolid(), element.fOutput.getFluid()) :
					element.fOutput != null ? new StoneMillRecipe(element.input.toStack(), element.tick, element.fOutput.getFluid()) :
						new StoneMillRecipe(element.input.toStack(), element.tick, element.sOutput.getSolid());
	}
	
	private FLEStoneMillRecipe() { }
	
	public static class StoneMillInfo
	{
		@Expose
		@SerializedName("input")
		private StackInfomation input;
		@Expose
		@SerializedName("time")
		private int tick;
		@Expose
		@SerializedName("outputSolid")
		private SolidJsonStack sOutput;
		@Expose
		@SerializedName("outputFluid")
		private FluidJsonStack fOutput;
	}
	
	public static class StoneMillRecipe extends MachineRecipe<StoneMillInfo>
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
		
		@Override
		protected StoneMillInfo makeInfo()
		{
			StoneMillInfo info = new StoneMillInfo();
			info.input = new JsonStack(input).getInfomation();
			info.tick = tick;
			if(sOutput != null)
				info.sOutput = new SolidJsonStack(sOutput);
			if(fOutput != null)
				info.fOutput = new FluidJsonStack(fOutput);
			return info;
		}
		
		public ItemAbstractStack getInput()
		{
			return input;
		}
		
		public boolean match(ItemStack aInput)
		{
			return input.equal(aInput);
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
			if(stack != null && key.stack != null) return stack.equal(key.stack);
			if(stack != null && (key.stack1 == null || !stack.equal(key.stack1))) return false;
			if(key.stack != null && (stack1 == null || !key.stack.equal(stack1))) return false;
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