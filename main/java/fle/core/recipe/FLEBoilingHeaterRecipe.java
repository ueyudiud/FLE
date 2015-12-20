package fle.core.recipe;

import static fle.core.handler.FuelHandler.g;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.io.JsonLoader;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;

public class FLEBoilingHeaterRecipe extends IRecipeHandler<BHRecipe>
{
	private static final FLEBoilingHeaterRecipe instance = new FLEBoilingHeaterRecipe();
	
	public static void init()
	{
		a(new BHRecipe(new BaseStack(Items.porkchop), new FluidStack(IB.plant_ash_mortar, 160), g(1.25F), ItemFleSub.a("plant_ash_soap", 2)));
		a(new BHRecipe(new BaseStack(Items.beef), new FluidStack(IB.plant_ash_mortar, 160), g(1.25F), ItemFleSub.a("plant_ash_soap", 2)));
		a(new BHRecipe(new BaseStack(ItemFleSub.a("ramie_fiber_dry")), new FluidStack(IB.plant_ash_mortar, 200), g(1.0F), ItemFleSub.a("ramie_fiber_debonded")));
		a(new BHRecipe(new BaseStack(ItemFleSub.a("cotton_gauze")), new FluidStack(IB.sugarcane_juice, 200), g(0.8F), ItemFleFood.a("brown_sugar", 3)).setType(3));
		a(new BHRecipe(new BaseStack(ItemFleSub.a("charred_log")), new FluidStack(IB.brown_sugar_aqua, 200), g(0.8F), ItemFleFood.a("sugar", 3)).setType(3));
		a(new BHRecipe(new BaseStack(ItemFleSub.a("crushed_bone")), new FluidStack(FluidRegistry.WATER, 200), g(1.25F), ItemFleSub.a("defatted_crushed_bone")));
	}
	
	public static void postInit(JsonLoader loader)
	{
		instance.reloadRecipes(loader);
	}
	
	public static FLEBoilingHeaterRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(BHRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static class BHRecipe extends MachineRecipe
	{
		private ItemAbstractStack toolRequire;
		private FluidStack input;
		private int energyRequire;
		public ItemStack output;
		private byte type = 0;

		public BHRecipe(FluidStack aInput, int aEnergyRequire, ItemStack aOutput)
		{
			this(null, aInput, aEnergyRequire, aOutput);
		}
		public BHRecipe(ItemAbstractStack aTool, FluidStack aInput, int aEnergyRequire, ItemStack aOutput)
		{
			toolRequire = aTool;
			input = aInput.copy();
			energyRequire = aEnergyRequire;
			output = aOutput.copy();
		}
		
		public BHRecipe setType(int aType)
		{
			type = (byte) aType;
			return this;
		}
		
		private static Random rand = new Random();
		
		public ItemStack onOutput(ItemStack input)
		{
			switch(type)
			{
			case 0 : input.stackSize--;
			return input.stackSize <= 0 ? null : input;
			case 1 : FleAPI.damageItem(null, input, EnumDamageResource.UseTool, 0.5F);
			return input;
			case 2 : return input;
			case 3 : if(rand.nextInt(8) == 0) input.stackSize--;
			return input.stackSize <= 0 ? null : input;
			}
			return null;
		}
		
		public int getFluidRequire()
		{
			return input.amount;
		}
		
		@Override
		public RecipeKey getRecipeKey()
		{
			return new BHKey(toolRequire, energyRequire, input);
		}
	}
	
	public static class BHKey extends RecipeKey
	{
		public int energyNeed;
		private ItemAbstractStack toolRequire;
		private ItemStack tool;
		private FluidStack key;
		private FluidStack target;

		public BHKey(ItemAbstractStack aInput, int aEnergy, FluidStack aKey)
		{
			toolRequire = aInput;
			energyNeed = aEnergy;
			key = aKey.copy();
		}
		public BHKey(ItemStack tool, FluidStack target)
		{
			if(tool != null)
				this.tool = tool.copy();
			if(target != null)
				this.target = target.copy();
		}
		
		@Override
		public int hashCode()
		{
			return key == null && target == null ? 57109228 : toolRequire == null && tool == null ? 28494719 : 92749190;
		}
		
		@Override
		protected boolean isEqual(RecipeKey keyRaw)
		{
			BHKey tKey = (BHKey) keyRaw;
			boolean flag = true;
			boolean flag1 = false;
			if(toolRequire != null && tKey.toolRequire != null)
			{
				flag &= toolRequire.equal(tKey.toolRequire);
				flag1 = true;
			}
			if(key != null && tKey.key != null)
			{
				flag &= FluidStack.areFluidStackTagsEqual(key, tKey.key);
				flag1 = true;
			}
			if(flag1) return flag;
			if(key != null && tKey.target != null)
			{
				if(!tKey.target.containsFluid(key)) return false;
			}
			if(target != null && tKey.key != null)
			{
				if(!target.containsFluid(tKey.key)) return false;
			}
			if(toolRequire != null && tKey.tool != null)
			{
				if(!toolRequire.equal(tKey.tool)) return false;
			}
			else if(toolRequire != null ^ tKey.tool != null) return false;
			if(tKey.toolRequire != null && tool != null)
			{
				if(!tKey.toolRequire.equal(tool)) return false;
			}
			else if(tKey.toolRequire != null ^ tool != null) return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			try
			{
				return "recipe.input.item:" + toolRequire.toString() + ".fluid:" + key.getUnlocalizedName();
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
	}
}