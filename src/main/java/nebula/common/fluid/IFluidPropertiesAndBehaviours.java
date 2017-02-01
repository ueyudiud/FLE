package nebula.common.fluid;

import net.minecraft.item.ItemStack;

public interface IFluidPropertiesAndBehaviours
{
	public static interface IFP_Solutability
	{
		boolean isItemSolutable(FluidStackExt fluidStack, ItemStack solute);
	}
	
	public static interface IFP_Temperature
	{
		int regetTemperature(FluidStackExt stack, int temperatureRaw);
	}
}
