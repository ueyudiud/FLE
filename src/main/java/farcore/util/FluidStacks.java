/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import farcore.lib.fluid.FluidStackExt;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class FluidStacks
{
	public static int getTemperature(FluidStack stack, int def)
	{
		return stack instanceof FluidStackExt ?
				((FluidStackExt) stack).getTemperature() :
					stack != null ? stack.getFluid().getTemperature(stack) : def;
	}
	
	public static int getColor(FluidStack stack)
	{
		return stack.getFluid().getColor(stack);
	}
}