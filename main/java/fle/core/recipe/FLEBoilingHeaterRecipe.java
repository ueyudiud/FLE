package fle.core.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.ConfigInfomation;
import fle.api.util.FLEConfiguration;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;

public class FLEBoilingHeaterRecipe extends IRecipeHandler<BHRecipe>
{
	private static final FLEBoilingHeaterRecipe instance = new FLEBoilingHeaterRecipe();
	
	public static void init()
	{
		a(new BHRecipe(new ItemBaseStack(ItemFleSub.a("ramie_fiber_dry")), new FluidStack(IB.plant_ash_mortar, 200), 12500, ItemFleSub.a("ramie_fiber_debonded")));
		a(new BHRecipe(new ItemBaseStack(ItemFleSub.a("cotton_gauze")), new FluidStack(IB.sugarcane_juice, 200), 12500, ItemFleFood.a("brown_sugar", 3)).setType(3));
		a(new BHRecipe(new ItemBaseStack(ItemFleSub.a("charred_log")), new FluidStack(IB.brown_sugar_aqua, 200), 12500, ItemFleFood.a("sugar", 3)).setType(3));
		a(new BHRecipe(new ItemBaseStack(ItemFleSub.a("crushed_bone")), new FluidStack(FluidRegistry.WATER, 200), 30000, ItemFleSub.a("defatted_crushed_bone")));
	}
	
	public static void postInit(FLEConfiguration cfg)
	{
		instance.reloadRecipes(cfg);
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
		
		@Override
		public void reloadRecipe(ConfigInfomation ci)
		{
			
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
		public boolean equals(Object obj)
		{
			if(!(obj instanceof BHKey)) return false;
			BHKey tKey = (BHKey) obj;
			boolean flag = true;
			boolean flag1 = false;
			if(toolRequire != null && tKey.toolRequire != null)
			{
				flag &= toolRequire.isStackEqul(tKey.toolRequire);
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
				if(!toolRequire.isStackEqul(tKey.tool)) return false;
			}
			else if(toolRequire != null ^ tKey.tool != null) return false;
			if(tKey.toolRequire != null && tool != null)
			{
				if(!tKey.toolRequire.isStackEqul(tool)) return false;
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