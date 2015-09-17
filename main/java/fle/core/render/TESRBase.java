package fle.core.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.SignedBytes;

public abstract class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer
{
	protected static RenderBlocks renderBlocks = new RenderBlocks();
	protected static RenderItem itemRenderer;
	
	public abstract void renderTileEntityAt(T tile, double xPos, double yPos, double zPos);

	public void renderTileEntityAt(TileEntity tile, double xPos, double yPos, double zPos, float aLevel)
	{
		try
		{
			if(tile != null)
				renderTileEntityAt((T) tile, xPos, yPos, zPos);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	protected World getWorldObj()
	{
		return field_147501_a.field_147550_f;
	}
	
	protected void renderFont(String[] strings, float f1, float f2, float f3)
	{
		FontRenderer fontrenderer = this.func_147498_b();
		f3 = 0.016666668F * f1;
		GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
		GL11.glScalef(f3, -f3, f3);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GL11.glDepthMask(false);
		byte b0 = 0;
		
		int i = 0;
		for (String s : strings)
		{
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, i++ * 10 - strings.length * 5, b0);
		}

		GL11.glDepthMask(true);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glPopMatrix();
	}
	
	static
	{
		itemRenderer = new RenderItem()
		{
			@Override
			public byte getMiniBlockCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 16, 3) + 1);
			}

			@Override
			public byte getMiniItemCount(ItemStack stack, byte original)
			{
				return SignedBytes.saturatedCast(Math.min(stack.stackSize / 8, 7) + 1);
			}

			@Override
			public boolean shouldBob()
			{
				return true;
			}

			@Override
			public boolean shouldSpreadItems()
			{
				return false;
			}
		};
		itemRenderer.setRenderManager(RenderManager.instance);
	}
}