package farcore.energy.thermal;

import farcore.lib.util.Direction;
import farcore.lib.world.ICoord;

/**
 * The thermal handler.
 * Implements by tile entity.
 * @author ueyudiud
 *
 */
public interface IThermalHandler extends ICoord
{
	/**
	 * Check if this tile can connect to another side, called
	 * by thermal net.
	 * @param direction
	 * @return
	 */
	boolean canConnectTo(Direction direction);
	
	/**
	 * This method is only get machine delta temperature.
	 * DO NOT GET WORLD TEMPERATURE IN THIS METHOD!
	 * @param direction
	 * @return
	 */
	float getTemperatureDifference(Direction direction);
	
	/**
	 * An basic constant of heat conduct. Called by thermal net.<br>
	 * Use P = dT * k.<br>
	 * The k is this value.
	 * @param direction
	 * @return
	 */
	double getThermalConductivity(Direction direction);
	
	/**
	 * Called when heat changed at this tile.
	 * @param direction
	 * @param value
	 */
	void onHeatChange(Direction direction, double value);
}