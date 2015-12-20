package fle.core.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import fle.core.init.IB;
import fle.core.te.TileEntityBarrel;

public class BarrelDrinkMixRecipe
{
	private static final List<BarrelDrinkMixRecipe> recipes = new ArrayList();
	public static final BarrelDrinkMixRecipe UNKNOWN = new BarrelDrinkMixRecipe("Water", FluidRegistry.WATER);
	
	public static void addRecipe(BarrelDrinkMixRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static BarrelDrinkMixRecipe getRecipeInfo(TileEntityBarrel tile)
	{
		float count = tile.getAmount();
		for(BarrelDrinkMixRecipe recipe : recipes)
		{
			if(recipe.list.isEmpty())
			{
				if(!recipe.list.contains(tile.drinkName)) continue;
			}
			float alco = (float) tile.alcoholContent / count;
			float sugar = (float) tile.sugarContent / count;
			float eth = (float) tile.ethanicAcidContent / count;
			//float star = (float) tile.starchContent / count;
			float acet = (float) tile.acetaldehydeContent / count;
			if(recipe.maxA < alco || recipe.minA > alco) continue;
			if(recipe.maxB < sugar || recipe.minB > sugar) continue;
			if(recipe.maxC < eth || recipe.minC > eth) continue;
			if(recipe.maxD < acet || recipe.minD > acet) continue;
			return recipe;
		}
		return count > 0 ? UNKNOWN : null;
	}
	
	public static void init()
	{
		addRecipe(new BarrelDrinkMixRecipe("Wheat Alcohol", new FluidStack(IB.wheat_alcohol, 10))
		.setAccessDrinkType("wheat")
		.setAlcoholContent(0.1F, 0.15F)
		.setSugarContent(0.0F, 0.4F));
	}
	
	List<String> list = new ArrayList<String>();
	float minA = 0F;
	float maxA = 1F;
	float minB = 0F;
	float maxB = 1F;
	float minC = 0F;
	float maxC = 1F;
	float minD = 0F;
	float maxD = 1F;
	float minE = 0F;
	float maxE = 1F;
	FluidStack output;
	String checkInfo;

	public BarrelDrinkMixRecipe(String info, Fluid output)
	{
		this(info, new FluidStack(output, 1));
	}
	public BarrelDrinkMixRecipe(String info, FluidStack output)
	{
		this.output = output.copy();
		this.checkInfo = info;
	}
	
	public FluidStack getOutput(int size)
	{
		FluidStack fluid = output.copy();
		fluid.amount *= size;
		return fluid;
	}
	
	public BarrelDrinkMixRecipe setAccessDrinkType(String...strings)
	{
		list.addAll(Arrays.asList(strings));
		return this;
	}
	
	public BarrelDrinkMixRecipe setAcetaldehydeContent(float a, float b)
	{
		maxD = Math.max(a, b);
		minD = Math.min(a, b);
		return this;
	}
	
	public BarrelDrinkMixRecipe setAlcoholContent(float a, float b)
	{
		maxA = Math.max(a, b);
		minA = Math.min(a, b);
		return this;
	}
	
	public BarrelDrinkMixRecipe setSugarContent(float a, float b)
	{
		maxB = Math.max(a, b);
		minB = Math.min(a, b);
		return this;
	}
	
	public BarrelDrinkMixRecipe setEthanicAcidContent(float a, float b)
	{
		maxC = Math.max(a, b);
		minC = Math.min(a, b);
		return this;
	}

	public String getInfo()
	{
		return checkInfo;
	}
}