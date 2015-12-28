package flapi.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.base.Matter;

public interface IFuelHandler
{
	float getFuelCalorificValue(FluidStack aStack, Matter aAirBase);
	
	boolean getFuelRequireSmoke(FluidStack aStack, Matter aAirBase);
	
	long getFuelCalorificValue(ItemStack aStack, Matter aAirBase);
	
	ItemStack getFuelBurnedOutput(ItemStack aStack);
}