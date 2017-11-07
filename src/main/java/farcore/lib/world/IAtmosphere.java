/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import farcore.lib.material.Mat;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IAtmosphere
{
	float getPartialPressure(@Nonnull Mat material);
	
	float getPressure();
	
	@Nullable
	FluidStack createFluid(int amount);
}
