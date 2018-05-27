/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.screen;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.tile.abstracts.TEScreenLineChart;
import nebula.common.NebulaSynchronizationHandler;

/**
 * @author ueyudiud
 */
public class TEScreenTemperature extends TEScreenLineChart
{
	public TEScreenTemperature()
	{
		super(256, 1, 270, 500);
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		this.marker.onUpdate();
	}
	
	@Override
	protected int getChartValue()
	{
		return (int) ThermalNet.getTemperature(this);
	}
	
	@Override
	protected void updateChart()
	{
		super.updateChart();
		NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
	}
}
