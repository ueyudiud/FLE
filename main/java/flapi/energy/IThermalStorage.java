package flapi.energy;

import farcore.util.Direction;

public interface IThermalStorage extends IEnergyStorage
{
	/**
	 * Get temperature of tile.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	int getTemperature(Direction dir);
	
	/**
	 * Get tile conduct thermal energy speed, high value
	 * cause more quickly.
	 * @param dir which side of tile is asking.
	 * @return
	 */
	double getThermalConductivity(Direction dir);
	
	long emmitThermalTo(Direction dir, EnergyPacket pkt, boolean process);
	
	long recieveThermalFrom(Direction dir, EnergyPacket pkt, boolean process);
}