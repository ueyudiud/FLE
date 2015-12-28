package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.ChemReaction;
import flapi.chem.base.ContainInput;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMatterInputHatch;

public class FLECrucibleRecipe
{
	private static Map<String, FLECrucibleRecipe> recipes = new HashMap<String, FLECrucibleRecipe>();
	
	public static Object[] matchAndInputRecipe(IChemCondition condition, IMatterInputHatch hatch)
	{
		for(FLECrucibleRecipe recipe : recipes.values())
		{
			int i = recipe.matchAndInput(condition, hatch);
			if(i != -1) return new Object[]{recipe, i};
		}
		return null;
	}
	
	public static void addRecipe(FLECrucibleRecipe recipe)
	{
		recipes.put(recipe.name, recipe);
	}
	
	public static FLECrucibleRecipe getRecipe(String name)
	{
		return recipes.containsKey(name) ? recipes.get(name) : null;
	}
	
	String name;
	public ContainInput input;
	public int perEnergyRequire;
	public FluidStack output;
	public float defaultSpeed;

	public FLECrucibleRecipe(String name, ContainInput input, int energyRequire, float power, Fluid output)
	{
		this(name, input, energyRequire, power, new FluidStack(output, 1));
	}
	public FLECrucibleRecipe(String name, ContainInput input, int energyRequire, float power, FluidStack output)
	{
		this.name = name;
		this.input = input;
		this.perEnergyRequire = energyRequire;
		this.defaultSpeed = power;
		this.output = output.copy();
	}
	
	public int matchAndInput(IChemCondition condition, IMatterInputHatch hatch)
	{
		return ChemReaction.matchAndInputRecipe(condition, hatch, input, output.amount);
	}
	
	public FluidStack getOutput(int scale)
	{
		FluidStack out = output.copy();
		out.amount *= scale;
		return out;
	}
	
	public String getName()
	{
		return name;
	}
}