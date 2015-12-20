package fle.core.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.solid.Solid;
import flapi.solid.SolidStack;
import flapi.util.io.JsonLoader;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;

public class FLESifterRecipe extends IRecipeHandler<SifterRecipe>
{
	private static final FLESifterRecipe instance = new FLESifterRecipe();

	public static void init()
	{
		a(new SifterRecipe(new BaseStack(ItemFleFood.a("groats_wheat_wholemeal")), new SolidStack(IB.wheat_b, 20), ItemFleSub.a("bran"), 0.25F));
		a(new SifterRecipe(new BaseStack(ItemFleFood.a("groats_millet_wholemeal")), new SolidStack(IB.millet_b, 20), ItemFleSub.a("bran"), 0.25F));
		a(new SifterRecipe(IB.wheat, new SolidStack(IB.wheat_b), ItemFleSub.a("bran"), 0.03F));
		a(new SifterRecipe(IB.millet, new SolidStack(IB.millet_b), ItemFleSub.a("bran"), 0.03F));
	}
	
	public static void postInit(JsonLoader loader)
	{
		instance.reloadRecipes(loader);
	}
	
	public static FLESifterRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(SifterRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static class SifterRecipe extends MachineRecipe
	{
		private static final Random rand = new Random();   
		
		private Object input;
		public SolidStack output1;
		public ItemStack output2;
		private float chance;

		public SifterRecipe(ItemAbstractStack aInput, SolidStack aOutput, ItemStack aOutput1, float aChance)
		{
			input = aInput;
			output1 = aOutput.copy();
			if(aOutput1 != null)
				output2 = aOutput1.copy();
			chance = aChance;
		}
		public SifterRecipe(Solid aInput, SolidStack aOutput, ItemStack aOutput1, float aChance)
		{
			input = aInput;
			output1 = aOutput.copy();
			if(aOutput1 != null)
				output2 = aOutput1.copy();
			chance = aChance;
		}
				
		public ItemStack getRandOutput()
		{
			if(output2 != null)
			{
				return rand.nextFloat() < chance ? output2.copy() : null;
			}
			return null;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return input instanceof ItemAbstractStack ? new SifterKey((ItemAbstractStack) input) : new SifterKey((Solid) input);
		}
		
		public Object getInput()
		{
			return input;
		}
		
		public float getOutputChance()
		{
			return chance;
		}
	}
	
	public static class SifterKey extends RecipeKey
	{
		private ItemAbstractStack inputKey;
		private Solid inputKey1;
		private ItemStack inputTarget;
		private SolidStack inputTarget1;

		public SifterKey(ItemAbstractStack aInput)
		{
			inputKey = aInput;
		}
		public SifterKey(ItemStack aInput)
		{
			if(aInput != null)
				inputTarget = aInput.copy();
		}
		public SifterKey(Solid aInput)
		{
			inputKey1 = aInput;
		}
		public SifterKey(SolidStack aInput)
		{
			if(aInput != null)
				inputTarget1 = aInput.copy();
		}
		
		@Override
		public int hashCode()
		{
			int i = 31;
			if(inputKey1 != null)
			{
				i += inputKey1.hashCode() * 31 + 3842941;
			}
			if(inputTarget1 != null)
			{
				i += inputTarget1.get().hashCode() * 31 + 3842941;
			}
			else if(inputKey != null || inputTarget != null) i += 3875918;
			return i;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			SifterKey key = (SifterKey) keyRaw;
			if(key.inputKey != null && inputKey != null) return inputKey.equal(key.inputKey);
			if(key.inputKey1 != null && inputKey1 != null) return key.inputKey1 == inputKey1;
			if(key.inputTarget != null && inputTarget != null) return false;
			if(key.inputTarget != null && inputTarget1 != null) return false;
			if(key.inputKey != null && inputTarget != null)
				if(!key.inputKey.equal(inputTarget)) return false;
			if(key.inputTarget != null && inputKey != null)
				if(!inputKey.equal(key.inputTarget)) return false;
			if(key.inputKey1 != null && inputTarget1 != null)
				if(!inputTarget1.contain(key.inputKey1)) return false;
			if(key.inputTarget1 != null && inputKey1 != null)
				if(!key.inputTarget1.contain(inputKey1)) return false;
			return true;
		}
	
		@Override
		public String toString()
		{
			try
			{
				return inputKey != null ? "recipe.input:" + inputKey.toString() : "recipe.input:" + inputKey1.getUnlocalizedName();
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
	}
}
