package fle.core.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.solid.Solid;
import flapi.solid.SolidJsonStack;
import flapi.solid.SolidStack;
import flapi.util.io.JsonHandler;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLESifterRecipe.SifterInfo;
import fle.core.recipe.FLESifterRecipe.SifterRecipe;

public class FLESifterRecipe extends IRecipeHandler<SifterRecipe, SifterInfo>
{
	private static final FLESifterRecipe instance = new FLESifterRecipe();

	public static void init()
	{
		a(new SifterRecipe("Groats Wheat Sift", new BaseStack(ItemFleFood.a("groats_wheat_wholemeal")), new SolidStack(IB.wheat_b, 20), ItemFleSub.a("bran"), 0.25F));
		a(new SifterRecipe("Groats Millet Sift", new BaseStack(ItemFleFood.a("groats_millet_wholemeal")), new SolidStack(IB.millet_b, 20), ItemFleSub.a("bran"), 0.25F));
		a(new SifterRecipe("Groats Wheat Solid Shift", IB.wheat, new SolidStack(IB.wheat_b), ItemFleSub.a("bran"), 0.03F));
		a(new SifterRecipe("Groats Millet Solid Shift", IB.millet, new SolidStack(IB.millet_b), ItemFleSub.a("bran"), 0.03F));
	}
	
	public static void postInit(JsonHandler loader)
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
	
	@Override
	protected SifterRecipe readFromJson(SifterInfo element)
	{
		return element.itemInput != null ?
				new SifterRecipe(element.name, element.itemInput.toStack(), element.output1.getSolid(), element.output2.getStack(), element.chance) :
					new SifterRecipe(element.name, element.solidInput.getSolid().getSolid(), element.output1.getSolid(), element.output2.getStack(), element.chance);
	}
	
	public static class SifterInfo
	{
		@Expose
		public String name;
		@Expose
		@SerializedName("inputSolid")
		public SolidJsonStack solidInput;
		@Expose
		@SerializedName("inputItem")
		public StackInfomation itemInput;
		@Expose
		@SerializedName("outputSolid")
		public SolidJsonStack output1;
		@Expose
		@SerializedName("outputItem")
		public StackInfomation output2;
		@Expose
		@SerializedName("outputItemChance")
		public float chance;
	}
	
	public static class SifterRecipe extends MachineRecipe<SifterInfo>
	{
		private static final Random rand = new Random();   
		
		private final String name;
		private Object input;
		public SolidStack output1;
		public ItemStack output2;
		private float chance;

		public SifterRecipe(String name, ItemAbstractStack aInput, SolidStack aOutput, ItemStack aOutput1, float aChance)
		{
			this.name = name;
			input = aInput;
			output1 = aOutput.copy();
			if(aOutput1 != null)
				output2 = aOutput1.copy();
			chance = aChance;
		}
		public SifterRecipe(String name, Solid aInput, SolidStack aOutput, ItemStack aOutput1, float aChance)
		{
			this.name = name;
			input = aInput;
			output1 = aOutput.copy();
			if(aOutput1 != null)
				output2 = aOutput1.copy();
			chance = aChance;
		}
		
		@Override
		protected SifterInfo makeInfo()
		{
			SifterInfo info = new SifterInfo();
			info.name = name;
			if(input instanceof ItemAbstractStack)
				info.itemInput = new JsonStack((ItemAbstractStack) input).getInfomation();
			else if(input instanceof Solid)
				info.solidInput = new SolidJsonStack(new SolidStack((Solid) input));
			else throw new RuntimeException("Sifter input must be item or solid");
			if(output2 != null)
			{
				info.output2 = new JsonStack(output2).getInfomation();
				info.chance = chance;
			}
			info.output1 = new SolidJsonStack(output1);
			return info;
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
			return input instanceof ItemAbstractStack ? new SifterKey(name, (ItemAbstractStack) input) : new SifterKey(name, (Solid) input);
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
		private String name;
		private ItemAbstractStack inputKey;
		private Solid inputKey1;
		private ItemStack inputTarget;
		private SolidStack inputTarget1;

		public SifterKey(String name, ItemAbstractStack aInput)
		{
			this.name = name;
			inputKey = aInput;
		}
		public SifterKey(ItemStack aInput)
		{
			if(aInput != null)
				inputTarget = aInput.copy();
		}
		public SifterKey(String name, Solid aInput)
		{
			this.name = name;
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
			if(inputKey != null || inputTarget != null) i += 3875918;
			return i;
		}
		
		public boolean isStandartKey()
		{
			return name != null;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			SifterKey key = (SifterKey) keyRaw;
			if(key.isStandartKey() && isStandartKey())
				return name.equals(key.name);
			if(key.inputTarget != null && inputTarget1 != null) return false;
			if(isStandartKey())
			{
				if(key.inputTarget != null && inputKey != null)
				{
					if(!inputKey.equal(key.inputTarget)) return false;
				}
				else if(key.inputTarget != null ^ inputKey != null) return false;
				if(key.inputTarget1 != null && inputKey1 != null)
				{
					if(!key.inputTarget1.contain(inputKey1)) return false;
				}
				else if(key.inputTarget1 != null ^ inputKey1 != null) return false;
				return true;
			}
			else if(key.isStandartKey()) return key.isEqual(this);
			else return false;
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
	}
}
