package fle.api.fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fle.api.util.IChemCondition.EnumPH;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class FluidDictionary 
{
	public abstract void registerFluid(String fluidName, Fluid fluid);
	public abstract void registerFluidPH(Fluid fluid, EnumPH ph);
	
	public abstract List<Fluid> getFluidList(String fluidName);
	
	public abstract List<String> getFluidDictionaryName(Fluid fluid);
	
	public abstract boolean removeFluid(Fluid fluid);
	
	public abstract boolean matchFluid(String fluidName, FluidStack target);
	
	public abstract EnumPH getFluidPH(Fluid fluid);

	public boolean isFluidWater(FluidStack aStack)
	{
		return matchFluid("water", aStack);
	}
	
	public boolean isFluidLava(FluidStack aStack)
	{
		return matchFluid("lava", aStack);
	}
	
	public boolean isFluidSteam(FluidStack aStack)
	{
		return matchFluid("steam", aStack);
	}
}