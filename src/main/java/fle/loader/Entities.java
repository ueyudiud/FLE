/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import fle.core.FLE;
import fle.core.client.render.entity.RenderFLECreeper;
import fle.core.client.render.entity.RenderFLESkeleton;
import fle.core.client.render.entity.RenderFLESpider;
import fle.core.client.render.entity.RenderFLEZombie;
import fle.core.entity.monster.EntityFLECreeper;
import fle.core.entity.monster.EntityFLESkeleton;
import fle.core.entity.monster.EntityFLESpider;
import fle.core.entity.monster.EntityFLEZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityFLEZombie.class, manager -> new RenderFLEZombie(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityFLESkeleton.class, manager -> new RenderFLESkeleton(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityFLESpider.class, manager -> new RenderFLESpider(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityFLECreeper.class, manager -> new RenderFLECreeper(manager));
	}
}