/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import nebula.common.data.DataSerializers;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * @author ueyudiud
 */
public final class Entities
{
	private Entities() {}
	
	public static double velocitySq(Entity entity)
	{
		return entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
	}
	
	public static double velocity(Entity entity)
	{
		return Math.sqrt(velocitySq(entity));
	}
	
	public static <E extends Enum<E>> DataParameter<E> createKey(Class<? extends Entity> entityClass, Class<E> enumClass)
	{
		return EntityDataManager.createKey(entityClass, DataSerializers.create(enumClass));
	}
}