/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.data;

import nebula.common.fluid.FluidStackExt;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * The fluid list.
 * 
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
	
	public FluidStackExt stack(int amount, int temp)
	{
		return new FluidStackExt(this.fluid, amount, temp, null);
	}
}
