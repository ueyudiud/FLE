package fle.core.world.climate.surface;

import fle.core.world.climate.Climate;

public class ClimateIceSpikesLand extends Climate
{
	public ClimateIceSpikesLand(int id, String name)
	{
		super(id, name);
		setEnableSnow();
	}
}