/*
 * copyright 2016-2018 ueyudiud
 */

package fle.core.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class RenderFLESkeleton extends RenderSkeleton
{
	public RenderFLESkeleton(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
}
