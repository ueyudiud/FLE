/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;

public class TECircuitInvert extends TECircuitSpatial
{
	@Override
	protected void updateCircuit()
	{
		byte power = 0;
		power = getPowerHigherThan(this.powerFB, power, Facing.FRONT);
		power = getPowerHigherThan(this.powerFB, power, Facing.BACK);
		this.powerFB = power;
		boolean flag = power > 0;
		power = 0;
		power = getPowerHigherThan(this.powerLR, power, Facing.LEFT);
		power = getPowerHigherThan(this.powerLR, power, Facing.RIGHT);
		this.powerLR = !flag ? 15 : power;
	}
	
	@Override
	public String getState()
	{
		return (this.powerFB > 0 ? "e" : "d") + (this.powerLR > 0 ? "e" : "d");
	}
}
