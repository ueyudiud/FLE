/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.screen;

import farcore.lib.tile.abstracts.TEScreenLineChart;
import nebula.common.NebulaSynchronizationHandler;

/**
 * @author ueyudiud
 */
public class TEScreenLight extends TEScreenLineChart
{
	public TEScreenLight()
	{
		super(256, 2, 0, 16);
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
		return this.world.getLight(this.pos, true);
	}
	
	@Override
	protected void updateChart()
	{
		super.updateChart();
		NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
	}
}
