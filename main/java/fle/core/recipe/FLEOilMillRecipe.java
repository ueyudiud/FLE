package fle.core.recipe;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEOilMillRecipe.OilMillRecipe;

public class FLEOilMillRecipe extends IRecipeHandler<OilMillRecipe>
{
	private static FLEOilMillRecipe instance = new FLEOilMillRecipe();
	
	static
	{
		a(new OilMillRecipe(new ItemBaseStack(ItemFleSeed.a("soybean")), ItemFleSub.a("plant_waste"), 0.08F, new FluidStack(IB.plantOil, 25)));
		a(new OilMillRecipe(new ItemBaseStack(ItemFleSeed.a("suger_cances")), ItemFleSub.a("plant_waste"), 0.2F, new FluidStack(IB.sugarcane_juice, 50)));
	}
	
	public static FLEOilMillRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(OilMillRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static class OilMillRecipe implements MachineRecipe
	{
		private static final Random rand = new Random();
		
		private ItemAbstractStack input;
		public ItemStack output1;
		private float outputChance;
		public FluidStack output2;

		public OilMillRecipe(ItemAbstractStack aInput, FluidStack aOutput2)
		{
			this(aInput, null, 0.0F, aOutput2);
		}
		public OilMillRecipe(ItemAbstractStack aInput, ItemStack aOutput1, float aOutputChance, FluidStack aOutput2)
		{
			input = aInput;
			if(aOutput1 != null)
				output1 = aOutput1.copy();
			outputChance = aOutputChance;
			output2 = aOutput2.copy();
		}
		
		public ItemStack getRandOutput()
		{
			return output1 != null && rand.nextFloat() < outputChance ? output1.copy() : null;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return new OilMillKey(input);
		}
	}
	
	public static class OilMillKey implements RecipeKey
	{
		private ItemAbstractStack key;
		private ItemStack target;
		
		public OilMillKey(ItemAbstractStack aKey)
		{
			key = aKey;
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
		public boolean equals(Object obj)
		{
			if(!(obj instanceof OilMillKey)) return false;
			OilMillKey key = (OilMillKey) obj;
			if(key.key != null && this.key != null) return this.key.isStackEqul(key.key);
			if(key.key != null && target != null)
					return key.key.isStackEqul(target);
			if(key.target != null && this.key != null)
					return this.key.isStackEqul(key.target);
			return false;
		}
		
		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:" + key.toString();
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
	}
}