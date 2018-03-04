/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import fle.api.client.TileEntityItemCustomRender;
import fle.core.tile.rocky.TESimplyKiln;
import nebula.client.render.TESRBase;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRSimplyKiln extends TESRBase<TESimplyKiln>
{
	@Override
	public void renderTileEntityAt(TESimplyKiln tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if (tile.stack == null)
			return;
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		if (!TileEntityItemCustomRender.SIMPLY_KILN.renderContainItem(tile.stack, tile))
		{
			if (tile.stack.getItem() instanceof ItemBlock)
			{
				renderItem(tile.stack, .5, 0, .5);
			}
			else
			{
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				int size = Math.min(8, tile.stack.stackSize);
				for (int i = 0; i < size; ++i)
				{
					renderItem(ItemStacks.setSizeOf(tile.stack, 1), .5, 0, - 0.032 * i - 0.03125);
				}
			}
		}
		
		GL11.glPopMatrix();
	}
}
