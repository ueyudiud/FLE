/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import nebula.common.fluid.FluidTankN;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class FluidSlotN extends FluidSlot<FluidTankN>
{
	public FluidSlotN(FluidTankN tank, int x, int y, int u, int v)
	{
		super(tank, x, y, u, v);
	}
	
	@Override
	public void putStack(FluidStack stack)
	{
		this.tank.setFluid(stack);
	}
}