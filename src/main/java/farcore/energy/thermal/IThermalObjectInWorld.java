package farcore.energy.thermal;

import farcore.lib.world.IObjectInWorld;

/**
 * Object in world with temperature property.
 * @author ueyudiud
 *
 */
public interface IThermalObjectInWorld extends IObjectInWorld
{
	float getDetTemp(double distanceSq, float tempBase);
}