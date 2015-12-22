package fle.core.recipe;

import java.util.Random;

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
import flapi.util.io.JsonHandler;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEOilMillRecipe.OMInfo;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;

public class FLEOilMillRecipe extends IRecipeHandler<OilMillRecipe, OMInfo>
{
	private static FLEOilMillRecipe instance = new FLEOilMillRecipe();
	
	public static void init()
	{
		a(new OilMillRecipe("Soybean Oil", new BaseStack(ItemFleSeed.a("soybean")), ItemFleSub.a("plant_waste"), 0.08F, new FluidStack(IB.plantOil, 25)));
		a(new OilMillRecipe("Sugar Cabces Juice", new BaseStack(ItemFleSeed.a("sugar_cances")), ItemFleSub.a("plant_waste"), 0.2F, new FluidStack(IB.sugarcane_juice, 50)));
		a(new OilMillRecipe("Apple Juice", new BaseStack(Items.apple), ItemFleSub.a("plant_waste"), 0.15F, new FluidStack(IB.apple_juice, 30)));
	}
	
	public static void postInit(JsonHandler loader)
	{
		instance.reloadRecipes(loader);
	}
	
	public static FLEOilMillRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(OilMillRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	protected OilMillRecipe readFromJson(OMInfo element) 
	{
		return element.output1 != null ? 
				new OilMillRecipe(element.name, element.input.toStack(), element.output1.getStack(), element.outputChance, element.output2.getFluid()) :
					new OilMillRecipe(element.name, element.input.toStack(), element.output2.getFluid());
	}
	
	public static class OMInfo
	{
		@Expose
		public String name;
		@Expose
		public StackInfomation input;
		@Expose
		@SerializedName("outputItem")
		public StackInfomation output1;
		@Expose
		@SerializedName("outputItemChance")
		public float outputChance;
		@Expose
		@SerializedName("outputFluid")
		public FluidJsonStack output2;
	}
	
	public static class OilMillRecipe extends MachineRecipe<OMInfo>
	{
		private static final Random rand = new Random();
		
		private final String name;
		private ItemAbstractStack input;
		public ItemStack output1;
		private float outputChance;
		public FluidStack output2;

		public OilMillRecipe(String name, ItemAbstractStack input, FluidStack output)
		{
			this(name, input, null, 0.0F, output);
		}
		public OilMillRecipe(String name, ItemAbstractStack aInput, ItemStack aOutput1, float aOutputChance, FluidStack aOutput2)
		{
			this.name = name;
			input = aInput;
			if(aOutput1 != null)
				output1 = aOutput1.copy();
			outputChance = aOutputChance;
			output2 = aOutput2.copy();
		}
		
		@Override
		protected OMInfo makeInfo()
		{
			OMInfo info = new OMInfo();
			info.name = name;
			info.input = new JsonStack(input).getInfomation();
			if(output1 != null)
			{
				info.output1 = new JsonStack(output1).getInfomation();
				info.outputChance = outputChance;
			}
			info.output2 = new FluidJsonStack(output2);
			return info;
		}
		
		public ItemStack getRandOutput()
		{
			return output1 != null && rand.nextFloat() < outputChance ? output1.copy() : null;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return new OilMillKey(name, input);
		}
		
		public ItemAbstractStack getInput()
		{
			return input;
		}
		
		public float getChance()
		{
			return outputChance;
		}
	}
	
	public static class OilMillKey extends RecipeKey
	{
		private String name;
		private ItemAbstractStack key;
		private ItemStack target;
		
		public OilMillKey(String name, ItemAbstractStack key)
		{
			this.name = name;
			this.key = key;
		}
		public OilMillKey(ItemStack aTarget)
		{
			if(aTarget != null)
				target = aTarget.copy();
		}
		
		@Override
		public int hashCode()
		{
			return 457252929;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			OilMillKey key = (OilMillKey) keyRaw;
			if(key.key != null && this.key != null) return this.key.equal(key.key);
			if(key.key != null && target != null)
					return key.key.equal(target);
			if(key.target != null && this.key != null)
					return this.key.equal(key.target);
			return false;
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