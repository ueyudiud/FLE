package fle.core.render;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityDryingTable;
import fle.core.te.argil.TileEntityArgilItems;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TESRArgilItems extends TESRBase
{
	@Override
	public void renderTileEntityAt(TileEntity tile, double xPos, double yPos,
			double zPos, float aLevel)
	{
		if (tile.getWorldObj() != null && tile instanceof TileEntityArgilItems)
		{
			EntityItem customitem = new EntityItem(getWorldObj());
			customitem.hoverStart = 0f;
			float blockScale = 1.0F;
			if(((TileEntityArgilItems) tile).stack != null)
			{
				GL11.glPushMatrix(); //start
				GL11.glTranslatef((float)xPos + 0.5F, (float)yPos + 0.0625F, (float)zPos + 0.325F);
				if (RenderManager.instance.options.fancyGraphics)
				{
					GL11.glRotatef(180F, 0.0F, 0.5F, 0.5F);
				}
				GL11.glScalef(blockScale, blockScale, blockScale);
				ItemStack target = ((TileEntityArgilItems) tile).stack.copy();
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
}