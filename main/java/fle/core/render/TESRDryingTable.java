package fle.core.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityDryingTable;

public class TESRDryingTable extends TESRBase
{
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) 
	{
		if (tileentity.getWorldObj() != null && tileentity instanceof TileEntityDryingTable)
		{
			EntityItem customitem = new EntityItem(getWorldObj());
			customitem.hoverStart = 0f;
			float blockScale = 1.0F;
			
			if(((TileEntityDryingTable) tileentity).getStackInSlot(0) != null)
			{
				GL11.glPushMatrix(); //start
				GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.5F);
				if (RenderManager.instance.options.fancyGraphics)
				{
					GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				}
				GL11.glScalef(blockScale, blockScale, blockScale);
				ItemStack target = ((TileEntityDryingTable) tileentity).getStackInSlot(0).copy();
				target.stackSize = 1;
				customitem.setEntityItemStack(target);
				itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
				GL11.glPopMatrix(); //end
			}

			if(((TileEntityDryingTable) tileentity).getStackInSlot(1) != null)
			{
				GL11.glPushMatrix(); //start
				GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 0.2F, (float)d2 + 0.5F);
				if (RenderManager.instance.options.fancyGraphics)
				{
					GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				}
				GL11.glScalef(blockScale, blockScale, blockScale);
				ItemStack target = ((TileEntityDryingTable) tileentity).getStackInSlot(1).copy();
				target.stackSize = 1;
				customitem.setEntityItemStack(target);
				itemRenderer.doRender(customitem, 0, 0, 0, 0, 0);
				GL11.glPopMatrix(); //end
			}
		}
	}
}