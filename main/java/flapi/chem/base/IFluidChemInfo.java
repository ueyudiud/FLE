package flapi.chem.base;

import net.minecraftforge.fluids.FluidStack;
import flapi.chem.base.IChemCondition.EnumOxide;
import flapi.chem.base.IChemCondition.EnumPH;

public interface IFluidChemInfo
{
	EnumPH getFluidPH(FluidStack aStack);
	
	EnumOxide getFluidOxide(FluidStack aStack);
}
