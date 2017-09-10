/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.thermal;

import nebula.common.world.IObjectInWorld;

/**
 * Object in world with temperature property.
 * @author ueyudiud
 *
 */
public interface IThermalObjectInWorld extends IObjectInWorld
{
	float getDetTemp(double distanceSq, float tempBase);
}