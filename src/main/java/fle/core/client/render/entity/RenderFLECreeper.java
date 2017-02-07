/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.render.entity;

import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class RenderFLECreeper extends RenderCreeper
{
	public RenderFLECreeper(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
}