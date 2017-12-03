/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.energy.thermal;

import nebula.common.util.Direction;
import nebula.common.world.ICoord;

/**
 * The thermal handler. Implements by tile entity.
 * 
 * @author ueyudiud
 * @see farcore.energy.thermal.ThermalNet
 */
public interface IThermalHandler extends ICoord
{
	/**
	 * Check if this tile can connect to another side, called by thermal net.
	 * 
	 * @param direction
	 * @return
	 */
	default boolean canConnectTo(Direction direction)
	{
		return true;
	}
	
	/**
	 * This method is only get machine delta temperature.
	 * <p>
	 * DO NOT GET WORLD TEMPERATURE IN THIS METHOD!
	 * 
	 * @param direction
	 * @return
	 */
	float getTemperatureDifference(Direction direction);
	
	/**
	 * The Thermal Net use adiabat heat conduct model.
	 * <p>
	 * Sometimes heat conductivity is too big and cause heat conduct more than
	 * it temperature difference can conduct, so the formula needed a revision
	 * term, which needed this coefficient.
	 * 
	 * @param direction
	 * @return
	 */
	// double getHeatCapacity(Direction direction);
	
	/**
	 * An basic constant of heat conduct. Called by thermal net.<br>
	 * Use <tt>P=kdT</tt> that:
	 * <li><tt>P</tt> is power (sending heat per tick).
	 * <li><tt>dT</tt> is difference of temperature.
	 * <li><tt>k</tt> is thermal conductivity, when calculate between two
	 * handler, use logarithmic mean value as conductivity.
	 * 
	 * @param direction
	 * @return the thermal conductivity.
	 */
	double getThermalConductivity(Direction direction);
	
	/**
	 * Heat change helper method.
	 * 
	 * @param values the changes heat from each direction.
	 */
	default void onHeatChange(long[] values)
	{
		for (Direction direction : Direction.DIRECTIONS_3D)
		{
			onHeatChange(direction, values[direction.ordinal()]);
		}
	}
	
	/**
	 * Called when heat changed at this tile.
	 * 
	 * @param direction
	 * @param value
	 */
	void onHeatChange(Direction direction, long value);
}
