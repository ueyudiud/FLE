package fle.api.material;

import net.minecraftforge.fluids.FluidStack;
import fle.api.util.IChemCondition.EnumOxide;
import fle.api.util.IChemCondition.EnumPH;

public interface IFluidChemInfo
{
	EnumPH getFluidPH(FluidStack aStack);
	
	EnumOxide getFluidOxide(FluidStack aStack);
}
