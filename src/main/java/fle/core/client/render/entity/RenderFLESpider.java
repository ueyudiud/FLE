/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class RenderFLESpider extends RenderSpider
{
	public RenderFLESpider(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
}
