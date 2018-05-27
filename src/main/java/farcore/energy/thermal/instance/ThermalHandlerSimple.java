/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.thermal.instance;

import nebula.common.util.Direction;
import nebula.common.world.ICoord;

/**
 * @author ueyudiud
 */
public class ThermalHandlerSimple extends ThermalHandlerAbstract
{
	public ThermalHandlerSimple(ICoord coord)
	{
		super(coord);
	}
	
	@Override
	public float getTemperatureDifference(Direction direction)
	{
		return (float) (this.energy / this.material.heatCapacity);
	}
	
	@Override
	public double getHeatCapacity(Direction direction)
	{
		return this.material.heatCapacity;
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.material.thermalConductivity;
	}
}
