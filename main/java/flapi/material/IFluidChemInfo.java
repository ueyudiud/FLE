package flapi.material;

import net.minecraftforge.fluids.FluidStack;
import flapi.material.IChemCondition.EnumOxide;
import flapi.material.IChemCondition.EnumPH;

public interface IFluidChemInfo
{
	EnumPH getFluidPH(FluidStack aStack);
	
	EnumOxide getFluidOxide(FluidStack aStack);
}
