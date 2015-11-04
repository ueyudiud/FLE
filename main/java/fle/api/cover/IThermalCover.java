package fle.api.cover;

import fle.api.energy.IThermalTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalCover
{
	/**
	 * Get temperature of cover.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public int getTemperature(IThermalTileEntity tile, ForgeDirection dir);
	
	/**
	 * Get cover conduct thermal energy speed, high value
	 * cause more quickly.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public double getThermalConductivity(IThermalTileEntity tile, ForgeDirection dir);
	
	/**
	 * Get total energy of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public double getThermalEnergyCurrect(IThermalTileEntity tile, ForgeDirection dir);
	
	/**
	 * On thermal energy receive by tile.
	 * @param dir
	 * @param heatValue
	 */
	public void onHeatReceive(IThermalTileEntity tile, ForgeDirection dir, double heatValue);

	/**
	 * On thermal energy emit by tile.
	 * @param dir
	 * @param heatValue
	 */
	public void onHeatEmit(IThermalTileEntity tile, ForgeDirection dir, double heatValue);
}