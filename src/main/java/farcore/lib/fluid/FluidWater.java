/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.fluid;

import farcore.energy.thermal.ThermalNet;
import nebula.common.fluid.FluidBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidWater extends FluidBase
{
	public FluidWater(String fluidName, String localName, ResourceLocation still, ResourceLocation flowing)
	{
		super(fluidName, localName, still, flowing);
	}
	
	@Override
	public int getTemperature(World world, BlockPos pos)
	{
		return (int) ThermalNet.getWorldTemperature(world, pos);
	}
}
