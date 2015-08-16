package fle.api.util;

import net.minecraftforge.fluids.FluidStack;
import fle.api.material.Matter;

public interface IFuelHandler
{
	float getFuelCalorificValue(FluidStack aStack, Matter aAirBase);
	
	boolean getFuelRequireSmoke(FluidStack aStack, Matter aAirBase);
}