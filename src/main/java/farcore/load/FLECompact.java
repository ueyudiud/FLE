/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.load;

import farcore.lib.block.instance.BlockFire;
import fle.api.recipes.instance.FlamableRecipes;
import nebula.common.fluid.FluidBase;
import nebula.common.util.ModCompator.ICompatible;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

/**
 * @author ueyudiud
 */
public class FLECompact implements ICompatible
{
	@Override
	public void call(String phase) throws Exception
	{
		switch (phase)
		{
		case "complete":
			FlamableRecipes.addFlameSource(coord -> {
				IBlockState state = coord.getBlockState();
				if (state.getBlock() instanceof BlockFire)
				{
					return state.getValue(BlockFire.STATE) * 30 + 400;// [400,
																		// 550]
				}
				if (state.getBlock() instanceof BlockFluidBase)
				{
					Fluid fluid = ((BlockFluidBase) state.getBlock()).getFluid();
					if (fluid instanceof FluidBase)
					{
						if (((FluidBase) fluid).isBurning()) return fluid.getTemperature(coord.world(), coord.pos());
					}
				}
				return -1;
			});
			break;
		default:
			break;
		}
	}
}
