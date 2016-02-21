package flapi.te.interfaces;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public interface IDitchTile extends IFluidHandler
{
	boolean canConnectWith(ForgeDirection dir);
	
	/**
	 * Get level of water, use 256 per block, if other water level is higher
	 * than this tile water will come here, else the water will to there.
	 * @return
	 */
	int getWaterLevel();
	
	/**
	 * This method is get the viscosity for ditch, see 
	 * {@link net.minecraftforge.fluids.Fluid}
	 * @return viscosity of tile.
	 */
	int getDitchViscosity();
}