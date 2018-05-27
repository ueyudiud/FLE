/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader;

import fle.core.FLE;
import fle.core.entity.monster.EntityFLECreeper;
import fle.core.entity.monster.EntityFLESkeleton;
import fle.core.entity.monster.EntityFLESlime;
import fle.core.entity.monster.EntityFLESpider;
import fle.core.entity.monster.EntityFLEZombie;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author ueyudiud
 */
public class Entities
{
	public static void commonInit()
	{
		int id = 0;
		EntityRegistry.registerModEntity(EntityFLEZombie.class, "FLEZombie", id++, FLE.MODID, 40, 1, true);
		EntityRegistry.registerModEntity(EntityFLESkeleton.class, "FLESkeleton", id++, FLE.MODID, 40, 1, true);
		EntityRegistry.registerModEntity(EntityFLESpider.class, "FLESpider", id++, FLE.MODID, 40, 1, true);
		EntityRegistry.registerModEntity(EntityFLECreeper.class, "FLECreeper", id++, FLE.MODID, 40, 1, true);
		EntityRegistry.registerModEntity(EntityFLESlime.class, "FLESlime", id++, FLE.MODID, 40, 1, true);
	}
}
