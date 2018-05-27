/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import nebula.client.render.Drawer;
import nebula.common.world.ICoord;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TileEntityItemRenderDust extends TileEntityItemRenderRegisteration
{
	private static final float px = 0x1.0p-4F;
	private static final int[] HEAP_SIZE = { 0, 3, 4, 4, 5, 5, 6, 6, 6 };
	
	@Override
	public boolean access(ItemStack stack)
	{
		return super.access(stack) && stack.stackSize >= 0 && stack.stackSize <= 8;
	}
	
	@Override
	public void render(ICoord tile, ItemStack stack)
	{
		Drawer drawer = Drawer.INSTANCE;
		final int size = HEAP_SIZE[stack.stackSize];
		drawer.bindIcon(icon(getResource(stack)[0]));
		drawer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		int i, j, k;
		float x1, y1, z1, x2, y2, z2;
		for (i = 0; i < size; ++i)
		{
			final int size1 = size - i;
			for (j = 0; j < size1; ++j)
			{
				final int size2 = size1 - j;
				for (k = 0; k < size2; ++k)
				{
					y1 =  i          * px;
					y2 = (i     + 1) * px;
					x1 = (8 - j - 1) * px;
					x2 = (8 - j    ) * px;
					z1 = (8 - k - 1) * px;
					z2 = (8 - k    ) * px;
					renderYPos(drawer, x1, y1, z1, x2, y2, z2);
					renderXNeg(drawer, x1, y1, z1, x2, y2, z2);
					renderZNeg(drawer, x1, y1, z1, x2, y2, z2);
					x1 = (8 + j    ) * px;
					x2 = (8 + j + 1) * px;
					renderYPos(drawer, x1, y1, z1, x2, y2, z2);
					renderXPos(drawer, x1, y1, z1, x2, y2, z2);
					renderZNeg(drawer, x1, y1, z1, x2, y2, z2);
					z1 = (8 + k    ) * px;
					z2 = (8 + k + 1) * px;
					renderYPos(drawer, x1, y1, z1, x2, y2, z2);
					renderXPos(drawer, x1, y1, z1, x2, y2, z2);
					renderZPos(drawer, x1, y1, z1, x2, y2, z2);
					x1 = (8 - j - 1) * px;
					x2 = (8 - j    ) * px;
					renderYPos(drawer, x1, y1, z1, x2, y2, z2);
					renderXNeg(drawer, x1, y1, z1, x2, y2, z2);
					renderZPos(drawer, x1, y1, z1, x2, y2, z2);
				}
			}
		}
		drawer.draw();
	}
	
	protected void renderYPos(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x2, y2, z2, x2, z2, x2, y2, z1, x2, z1, x1, y2, z1, x1, z1, x1, y2, z2, x1, z2, 0, 1, 0);
	}
	
	protected void renderYNeg(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x2, y1, z1, x2, 1F - z1, x2, y1, z2, x2, 1F - z2, x1, y1, z2, x1, 1F - z2, x1, y1, z1, x1, 1F - z1, 0, -1, 0);
	}
	
	protected void renderZPos(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x1, y2, z2, x1, 1F - y2, x1, y1, z2, x1, 1F - y1, x2, y1, z2, x2, 1F - y1, x2, y2, z2, x2, 1F - y2, 0, 0, 1);
	}
	
	protected void renderZNeg(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x2, y2, z1, 1F - x2, 1F - y2, x2, y1, z1, 1F - x2, 1F - y1, x1, y1, z1, 1F - x1, 1F - y1, x1, y2, z1, 1F - x1, 1F - y2, 0, 0, -1);
	}
	
	protected void renderXPos(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x2, y2, z2, 1F - z2, 1F - y2, x2, y1, z2, 1F - z2, 1F - y1, x2, y1, z1, 1F - z1, 1F - y1, x2, y2, z1, 1F - z1, 1F - y2, 1, 0, 0);
	}
	
	protected void renderXNeg(Drawer drawer, float x1, float y1, float z1, float x2, float y2, float z2)
	{
		drawer.face_ptn(x1, y2, z1, z1, 1F - y2, x1, y1, z1, z1, 1F - y1, x1, y1, z2, z2, 1F - y1, x1, y2, z2, z2, 1F - y2, -1, 0, 0);
	}
}
