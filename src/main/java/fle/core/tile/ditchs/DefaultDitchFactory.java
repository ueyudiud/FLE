/*
 * copyright© 2016 ueyudiud
 */

package fle.core.tile.ditchs;

import farcore.data.EnumBlock;
import farcore.data.MP;
import farcore.energy.thermal.ThermalNet;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyBasic;
import farcore.util.U;
import fle.api.tile.IDitchTile;
import fle.api.tile.IDitchTile.DitchBlockHandler;
import fle.api.tile.IDitchTile.DitchFactory;
import net.minecraftforge.fluids.FluidTank;

/**
 * @author ueyudiud
 */
public class DefaultDitchFactory implements DitchFactory
{
	public DefaultDitchFactory()
	{
		DitchBlockHandler.rawFactory = this;
	}

	@Override
	public boolean access(Mat material) { return true; }

	@Override
	public FluidTank apply(IDitchTile tile)
	{
		return new FluidTank(1000);
	}

	@Override
	public void onUpdate(IDitchTile tile)
	{
		FluidTank tank = tile.getTank();
		PropertyBasic property = tile.getMaterial().getProperty(MP.property_basic);
		if(property.meltingPoint < U.FluidStacks.getTemperature(tank.getFluid(), (int) ThermalNet.getTemperature(tile)))
		{
			if(EnumBlock.fire.block.canPlaceBlockAt(tile.world(), tile.pos()))
			{
				tile.setBlockState(EnumBlock.fire.block.getDefaultState(), 3);
			}
		}
	}

	@Override
	public int getSpeedMultiple(IDitchTile tile)
	{
		return 32000;
	}

	@Override
	public int getMaxTransferLimit(IDitchTile tile)
	{
		return 100;
	}
}