package fle.core.util;

import net.minecraftforge.fluids.FluidStack;
import fle.api.FleAPI;
import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumIons;
import fle.api.material.Matter;
import fle.api.util.IFuelHandler;

public class FleFuelHandler implements IFuelHandler
{
	@Override
	public float getFuelCalorificValue(FluidStack aStack, Matter aAirBase)
	{
		if(FleAPI.fluidDictionary.isFluidLava(aStack))
		{
			return aStack.amount * 100;
		}
		else if(FleAPI.fluidDictionary.matchFluid("oilAnimal", aStack))
		{
			return (float) ((double) aStack.amount * (2 + aAirBase.getIconContain(EnumAtoms.O)) / 3);
		}
		return -1;
	}

	@Override
	public boolean getFuelRequireSmoke(FluidStack aStack, Matter aAirBase)
	{
		return false;
	}
}