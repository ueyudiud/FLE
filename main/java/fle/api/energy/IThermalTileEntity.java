package fle.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalTileEntity 
{	
	/**
	 * Get temperature of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public int getTemperature(ForgeDirection dir);
	
	/**
	 * Get tile conduct thermal energy speed, high value
	 * cause more quickly.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public double getThermalConductivity(ForgeDirection dir);
	
	/**
	 * Get total energy of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	public double getThermalEnergyCurrect(ForgeDirection dir);
	
	/**
	 * On thermal energy receive by tile.
	 * @param dir
	 * @param heatValue
	 */
	public void onHeatReceive(ForgeDirection dir, double heatValue);

	/**
	 * On thermal energy emit by tile.
	 * @param dir
	 * @param heatValue
	 */
	public void onHeatEmit(ForgeDirection dir, double heatValue);
	
	/**
	 * Get total heat emitted by tile last tick, use in debug tool.
	 * @return The heat all emit, negative number mean tile receive
	 * heat more than emit heat.
	 */
	public double getPreHeatEmit();
}