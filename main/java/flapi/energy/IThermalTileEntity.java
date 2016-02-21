package flapi.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * A tile entity with thermal property.
 * @author ueyudiud
 *
 */
public interface IThermalTileEntity 
{	
	/**
	 * Get temperature of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	int getTemperature(ForgeDirection dir);
	
	/**
	 * Get tile conduct thermal energy speed, high value
	 * cause heat conduct more quickly.<br>
	 * Q = T * c * t<br>
	 * Unit : MJ / (K * m)
	 * @param dir which side of tile is asking.
	 * @return
	 */
	double getThermalConductivity(ForgeDirection dir);
	
	/**
	 * UNUSED, because thermal energy can not has a 0K or
	 * 273.15K for 0 point.
	 * 
	 * Get total energy of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	//double getThermalEnergyCurrect(ForgeDirection dir);
	
	/**
	 * On thermal energy receive by tile.
	 * @param dir
	 * @param heatValue (Unit : MJ)
	 */
	void onHeatReceive(ForgeDirection dir, double heatValue);

	/**
	 * On thermal energy emit by tile.
	 * @param dir
	 * @param heatValue (Unit : MJ)
	 */
	void onHeatEmit(ForgeDirection dir, double heatValue);
	
	/**
	 * Get total heat emitted by tile last tick, use in debug tool.
	 * @return The heat all emit, negative number mean tile receive
	 * heat more than emit heat. (Unit : MJ)
	 */
	double getPreHeatEmit();
}