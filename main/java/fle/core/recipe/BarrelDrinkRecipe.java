package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.item.ItemFleSub;

public class BarrelDrinkRecipe
{
	private static final Map<ItemAbstractStack, BarrelDrinkRecipe> recipes = new HashMap();
	private static final Map<Fluid, BarrelDrinkRecipe> frecipes = new HashMap();

	public static void addRecipe(ItemAbstractStack stack, BarrelDrinkRecipe recipe)
	{
		recipes.put(stack, recipe);
	}
	public static void addRecipe(Fluid fluid, BarrelDrinkRecipe recipe)
	{
		frecipes.put(fluid, recipe);
	}
	
	public static BarrelDrinkRecipe getRecipeInfo(ItemStack stack)
	{
		for(Entry<ItemAbstractStack, BarrelDrinkRecipe> e : recipes.entrySet())
		{
			if(e.getKey().equal(stack)) return e.getValue();
		}
		return null;
	}
	
	public static BarrelDrinkRecipe getRecipeInfo(FluidStack fluid)
	{
		for(Entry<Fluid, BarrelDrinkRecipe> e : frecipes.entrySet())
		{
			if(fluid.getFluid() == e.getKey()) return e.getValue();
		}
		return null;
	}
	
	public static boolean isFluidAddable(Fluid fluid)
	{
		return frecipes.containsKey(fluid);
	}
	
	public static void init()
	{
		addRecipe(FluidRegistry.WATER, new BarrelDrinkRecipe().setWaterContent(1));
		addRecipe(IB.wheat_dextrin, new BarrelDrinkRecipe("wheat").setStarchContent(10));
		addRecipe(IB.millet_dextrin, new BarrelDrinkRecipe().setStarchContent(10));
		addRecipe(IB.potato_dextrin, new BarrelDrinkRecipe("potato").setStarchContent(10));
		addRecipe(IB.sweet_potato_dextrin, new BarrelDrinkRecipe().setStarchContent(10));
		addRecipe(IB.saccharified_dextrin, new BarrelDrinkRecipe().setStarchContent(5).setSugarContent(2));
		addRecipe(IB.apple_juice, new BarrelDrinkRecipe().setSugarContent(6));
		addRecipe(IB.sugarcane_juice, new BarrelDrinkRecipe("sugarcane").setSugarContent(8));
		addRecipe(new BaseStack(ItemFleFood.a("groats_wheat")), new BarrelDrinkRecipe("wheat").setStarchContent(125));
		addRecipe(new BaseStack(ItemFleFood.a("sugar")), new BarrelDrinkRecipe("sugarcane").setSugarContent(75));
		addRecipe(new BaseStack(ItemFleSub.a("yeast_strain")), new BarrelDrinkRecipe().setMicrozymeLevel(100));
		addRecipe(new BaseStack(ItemFleSub.a("malt")), new BarrelDrinkRecipe().setGlucanGlucohydrolaceLevel(100).setSugarContent(25));
		addRecipe(new BaseStack(ItemFleSub.a("aspergillus")), new BarrelDrinkRecipe().setGlucanGlucohydrolaceLevel(25).setStarchContent(5));
	}
	
	private String drinkName;
	private int waterContent;//H2O
	private int starchContent;//(C6H10O5)n
	private int glucanGlucohydrolaceLevel;//(C6H10O5)n -> C6H12O6
	private int sugarContent;//C6H12O6
	private int microzymeLevel;//C6H12O6 -> CH3-CH2-OH
	private int alcoholContent;//CH3-CH2-OH
	private int acetaldehydeContent;//CH3-CHO
	private int ethanicAcidContent;//CH3-COOH

	public BarrelDrinkRecipe(String name)
	{
		setDrinkName(name);
	}
	public BarrelDrinkRecipe()
	{
		
	}
	
	public BarrelDrinkRecipe setDrinkName(String drinkName)
	{
		this.drinkName = drinkName;
		return this;
	}
	
	public BarrelDrinkRecipe setWaterContent(int waterContent)
	{
		this.waterContent = waterContent;
		return this;
	}
	
	public BarrelDrinkRecipe setAcetaldehydeContent(int acetaldehydeContent)
	{
		this.acetaldehydeContent = acetaldehydeContent;
		return this;
	}
	
	public BarrelDrinkRecipe setAlcoholContent(int alcoholContent)
	{
		this.alcoholContent = alcoholContent;
		return this;
	}
	
	public BarrelDrinkRecipe setGlucanGlucohydrolaceLevel(int glucanGlucohydrolaceLevel)
	{
		this.glucanGlucohydrolaceLevel = glucanGlucohydrolaceLevel;
		return this;
	}
	
	public BarrelDrinkRecipe setMicrozymeLevel(int microzymeLevel)
	{
		this.microzymeLevel = microzymeLevel;
		return this;
	}
	
	public BarrelDrinkRecipe setStarchContent(int starchContent)
	{
		this.starchContent = starchContent;
		return this;
	}
	
	public BarrelDrinkRecipe setSugarContent(int sugarContent)
	{
		this.sugarContent = sugarContent;
		return this;
	}
	
	public BarrelDrinkRecipe setEthanicAcidContent(int ethanicAcidContent)
	{
		this.ethanicAcidContent = ethanicAcidContent;
		return this;
	}
	
	public String getDrinkName()
	{
		return drinkName == null ? "" : drinkName;
	}

	public int getWaterContent(ItemStack stack)
	{
		return waterContent * stack.stackSize;
	}
	public int getWaterContent(FluidStack stack)
	{
		return waterContent * stack.amount;
	}

	public int getAcetaldehydeContent(ItemStack stack)
	{
		return acetaldehydeContent * stack.stackSize;
	}
	public int getAcetaldehydeContent(FluidStack stack)
	{
		return acetaldehydeContent * stack.amount;
	}

	public int getAlcoholContent(ItemStack stack)
	{
		return alcoholContent * stack.stackSize;
	}
	public int getAlcoholContent(FluidStack stack)
	{
		return alcoholContent * stack.amount;
	}

	public int getGlucanGlucohydrolaceLevel(ItemStack stack)
	{
		return glucanGlucohydrolaceLevel * stack.stackSize;
	}
	public int getGlucanGlucohydrolaceLevel(FluidStack stack)
	{
		return glucanGlucohydrolaceLevel * stack.amount;
	}

	public int getMicrozymeLevel(ItemStack stack)
	{
		return microzymeLevel * stack.stackSize;
	}
	public int getMicrozymeLevel(FluidStack stack)
	{
		return microzymeLevel * stack.amount;
	}

	public int getStarchContent(ItemStack stack)
	{
		return starchContent * stack.stackSize;
	}
	public int getStarchContent(FluidStack stack)
	{
		return starchContent * stack.amount;
	}

	public int getSugarContent(ItemStack stack)
	{
		return sugarContent * stack.stackSize;
	}
	public int getSugarContent(FluidStack stack)
	{
		return sugarContent * stack.amount;
	}

	public int getEthanicAcidContent(ItemStack stack)
	{
		return ethanicAcidContent * stack.stackSize;
	}
	public int getEthanicAcidContent(FluidStack stack)
	{
		return ethanicAcidContent * stack.amount;
	}
	
	public int getAmount(FluidStack resource)
	{
		return getWaterContent(resource) + getStarchContent(resource) + 
				getSugarContent(resource) + getAlcoholContent(resource) +
				getAcetaldehydeContent(resource) + getEthanicAcidContent(resource);
	}
	
	private static final FluidStack aStack = new FluidStack(FluidRegistry.WATER, 1);
	
	public int getAmount()
	{
		return getAmount(aStack);
	}
}