package farcore.fluid;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public interface IFlashableFluid
{
	int getFlashPoint(World world, int x, int y, int z);
	
	int getFlashPoint(FluidStack stack);
	
	/**
	 * The explosion level of fluid.<br>
	 * The explosion level per 1000L fluid.
	 * 
	 * Ex : TNT level is 400.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	int getExplosionLevel(World world, int x, int y, int z);
	
	int getExplosionLevel(FluidStack stack);
}