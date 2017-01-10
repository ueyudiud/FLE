/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.util;

import net.minecraft.entity.Entity;

/**
 * @author ueyudiud
 */
public class Entities
{
	public static double movementSpeedSq(Entity entity)
	{
		return entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
	}
	
	public static double movementSpeed(Entity entity)
	{
		return Math.sqrt(movementSpeedSq(entity));
	}
}