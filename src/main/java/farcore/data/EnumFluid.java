/*
 * copyright© 2016 ueyudiud
 */

package farcore.data;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @author ueyudiud
 */
public enum EnumFluid
{
	water;

	static
	{
		water.setFluid(FluidRegistry.WATER);
	}
	
	public Fluid fluid;
	
	public void setFluid(Fluid fluid)
	{
		this.fluid = fluid;
	}
}