package farcore.energy.thermal;

import farcore.lib.world.IObjectInWorld;

public interface IThermalObjectInWorld extends IObjectInWorld
{
	float getDetTemp(double distanceSq, float tempBase);
}