/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader;

import farcore.data.M;
import farcore.data.MC;
import farcore.instances.MaterialTextureLoader;
import fle.api.client.TileEntityItemCustomRender;
import fle.core.client.render.TileEntityItemRenderDust;
import nebula.common.stack.OreStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class SpecialRender
{
	public static void init()
	{
		TileEntityItemRenderDust render1 = new TileEntityItemRenderDust();
		
		render1.registerRender(new OreStack(MC.pile.getOreName(M.limestone)), MaterialTextureLoader.getResource(M.limestone, MC.stone, ""));
		render1.registerRender(new OreStack(MC.pile.getOreName(M.marble)), MaterialTextureLoader.getResource(M.marble, MC.stone, ""));
		
		TileEntityItemCustomRender.SIMPLY_KILN.registerItemRender(render1);
	}
}
