package fle.core.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityColdForgingPlatform;

public class TESRColdForging extends TESRBase<TileEntityColdForgingPlatform>
{
	@Override
	public void renderTileEntityAt(TileEntityColdForgingPlatform tile,
			double xPos, double yPos, double zPos)
	{
		EntityItem customitem = new EntityItem(getWorldObj());
		customitem.hoverStart = 0f;
		float blockScale = 1.0F;
		if(((TileEntityColdForgingPlatform) tile).getStackInSlot(4) != null)
		{
			GL11.glPushMatrix(); //start
			GL11.glTranslatef((float)xPos + 0.5F, (float)yPos + 0.0625F, (float)zPos + 0.325F);
			if (RenderManager.instance.options.fancyGraphics)
			{
				GL11.glTranslatef(0.0F, 0.0625F, 0.0F);
				GL11.glRotatef(180F, 0.0F, 0.5F, 0.5F);
			}
			GL11.glScalef(blockScale, blockScale, blockScale);
			ItemStack target = ((TileEntityColdForgingPlatform) tile).getStackInSlot(4).copy();
			customitem.setEntityItemStack(target);
			try
			{
				itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			GL11.glPopMatrix(); //end
		}
	}	
}