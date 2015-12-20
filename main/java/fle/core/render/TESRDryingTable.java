package fle.core.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityDryingTable;

public class TESRDryingTable extends TESRBase<TileEntityDryingTable>
{
	@Override
	public void renderTileEntityAt(TileEntityDryingTable tile, double xPos,
			double yPos, double zPos)
	{
		EntityItem customitem = new EntityItem(getWorldObj());
		customitem.hoverStart = 0f;
		float blockScale = 1.0F;
		
		if(((TileEntityDryingTable) tile).getStackInSlot(0) != null)
		{
			GL11.glPushMatrix(); //start
			GL11.glTranslatef((float)xPos + 0.5F, (float)yPos + 0.5F, (float)zPos + 0.5F);
			if (RenderManager.instance.options.fancyGraphics)
			{
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			}
			GL11.glScalef(blockScale, blockScale, blockScale);
			ItemStack target = ((TileEntityDryingTable) tile).getStackInSlot(0).copy();
			target.stackSize = 1;
			customitem.setEntityItemStack(target);
			itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
			GL11.glPopMatrix(); //end
		}

		if(((TileEntityDryingTable) tile).getStackInSlot(1) != null)
		{
			GL11.glPushMatrix(); //start
			GL11.glTranslatef((float)xPos + 0.5F, (float)yPos + 0.2F, (float)zPos + 0.5F);
			if (RenderManager.instance.options.fancyGraphics)
			{
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			}
			GL11.glScalef(blockScale, blockScale, blockScale);
			ItemStack target = ((TileEntityDryingTable) tile).getStackInSlot(1).copy();
			target.stackSize = 1;
			customitem.setEntityItemStack(target);
			itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
			GL11.glPopMatrix(); //end
		}
	}
}