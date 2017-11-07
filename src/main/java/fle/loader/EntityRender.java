/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import fle.core.client.render.entity.RenderFLECreeper;
import fle.core.client.render.entity.RenderFLESkeleton;
import fle.core.client.render.entity.RenderFLESpider;
import fle.core.client.render.entity.RenderFLEZombie;
import fle.core.entity.monster.EntityFLECreeper;
import fle.core.entity.monster.EntityFLESkeleton;
import fle.core.entity.monster.EntityFLESlime;
import fle.core.entity.monster.EntityFLESpider;
import fle.core.entity.monster.EntityFLEZombie;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class EntityRender
{
	public static void clientInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityFLEZombie.class, RenderFLEZombie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFLESkeleton.class, RenderFLESkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFLESpider.class, RenderFLESpider::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFLECreeper.class, RenderFLECreeper::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFLESlime.class, manager -> new RenderSlime(manager, new ModelSlime(16), 0.25F));
	}
}
