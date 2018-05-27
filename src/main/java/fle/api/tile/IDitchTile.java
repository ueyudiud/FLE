/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.tile;

import farcore.lib.material.Mat;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraftforge.fluids.Fluid;

/**
 * @author ueyudiud
 */
public interface IDitchTile extends IModifiableCoord
{
	default Fluid getFluidContain()
	{
		return getTank().getFluid() != null ? getTank().getFluid().getFluid() : null;
	}
	
	Mat getMaterial();
	
	FluidTankN getTank();
	
	float getFlowHeight();
	
	boolean isLinked(Direction direction);
	
	void setLink(Direction direction, boolean state);
}
