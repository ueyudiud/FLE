package fle.core.recipe;

import static fle.core.handler.FuelHandler.g;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.fluid.FluidJsonStack;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.util.io.JsonHandler;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipe;
import fle.core.recipe.FLEBoilingHeaterRecipe.BHRecipeInfo;

public class FLEBoilingHeaterRecipe extends IRecipeHandler<BHRecipe, BHRecipeInfo>
{
	private static final FLEBoilingHeaterRecipe instance = new FLEBoilingHeaterRecipe();
	
	public static void init()
	{
		a(new BHRecipe("Soap Porkchop", new BaseStack(Items.porkchop), new FluidStack(IB.plant_ash_mortar, 160), g(1.25F), ItemFleSub.a("plant_ash_soap", 2)));
		a(new BHRecipe("Soap Beef", new BaseStack(Items.beef), new FluidStack(IB.plant_ash_mortar, 160), g(1.25F), ItemFleSub.a("plant_ash_soap", 2)));
		a(new BHRecipe("Ramine Fiber Debond", new BaseStack(ItemFleSub.a("ramie_fiber_dry")), new FluidStack(IB.plant_ash_mortar, 200), g(1.0F), ItemFleSub.a("ramie_fiber_debonded")));
		a(new BHRecipe("Brown Sugar", new BaseStack(ItemFleSub.a("cotton_gauze")), new FluidStack(IB.sugarcane_juice, 200), g(0.8F), ItemFleFood.a("brown_sugar", 3)).setType(3));
		a(new BHRecipe("Wight Sugar", new BaseStack(ItemFleSub.a("charred_log")), new FluidStack(IB.brown_sugar_aqua, 200), g(0.8F), ItemFleFood.a("sugar", 3)).setType(3));
		a(new BHRecipe("Crushed Bone Defat", new BaseStack(ItemFleSub.a("crushed_bone")), new FluidStack(FluidRegistry.WATER, 200), g(1.25F), ItemFleSub.a("defatted_crushed_bone")));
	}
	
	public static void postInit(JsonHandler loader)
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
	
	@Override
	protected BHRecipe readFromJson(BHRecipeInfo element)
	{
		return new BHRecipe(element);
	}
	
	public static class BHRecipeInfo
	{
		@Expose
		public String name;
		@Expose
		@SerializedName("tool")
		public StackInfomation toolRequire;
		@Expose
		@SerializedName("inputFluid")
		public FluidJsonStack input;
		@Expose
		@SerializedName("energyUse")
		public int energyRequire;
		@Expose
		@SerializedName("outputItem")
		public StackInfomation output;
		@Expose
		@SerializedName("toolUseType")
		public int type = 0;
	}
	
	public static class BHRecipe extends MachineRecipe<BHRecipeInfo>
	{
		private final String name;
		private ItemAbstractStack toolRequire;
		private FluidStack input;
		private int energyRequire;
		public ItemStack output;
		private byte type = 0;

		public BHRecipe(String aName, FluidStack aInput, int aEnergyRequire, ItemStack aOutput)
		{
			this(aName, null, aInput, aEnergyRequire, aOutput);
		}
		public BHRecipe(String aName, ItemAbstractStack aTool, FluidStack aInput, int aEnergyRequire, ItemStack aOutput)
		{
			name = aName;
			toolRequire = aTool;
			input = aInput.copy();
			energyRequire = aEnergyRequire;
			output = aOutput.copy();
		}
		
		public BHRecipe(BHRecipeInfo info)
		{
			name = info.name;
			if(info.toolRequire != null)
				toolRequire = info.toolRequire.toStack();
			input = info.input.getFluid();
			energyRequire = info.energyRequire;
			if(info.output == null) throw new RuntimeException();
			output = info.output.toStack().toList()[0];
		}
		
		protected BHRecipeInfo makeInfo()
		{
			BHRecipeInfo info = new BHRecipeInfo();
			info.name = name;
			if(toolRequire != null)
				info.toolRequire = new JsonStack(toolRequire).getInfomation();
			info.energyRequire = energyRequire;
			info.input = new FluidJsonStack(input);
			info.output = new JsonStack(output).getInfomation();
			info.type = type;
			return info;
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
			return new BHKey(name, toolRequire, energyRequire, input);
		}
	}
	
	public static class BHKey extends RecipeKey
	{
		private String name;
		public int energyNeed;
		private ItemAbstractStack toolRequire;
		private ItemStack tool;
		private FluidStack key;
		private FluidStack target;

		public BHKey(String name, ItemAbstractStack aInput, int aEnergy, FluidStack aKey)
		{
			this.name = name;
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
			if(tKey.name != null && name != null)
			{
				return name.equals(tKey.name);
			}
			if(name != null)
			{
				if(key != null && tKey.target != null)
				{
					if(!tKey.target.containsFluid(key)) return false;
				}
				if(toolRequire != null && tKey.tool != null)
				{
					if(!toolRequire.equal(tKey.tool)) return false;
				}
				else if(toolRequire != null ^ tKey.tool != null) return false;
				return true;
			}
			else if(tKey.name != null) return tKey.isEqual(this);
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